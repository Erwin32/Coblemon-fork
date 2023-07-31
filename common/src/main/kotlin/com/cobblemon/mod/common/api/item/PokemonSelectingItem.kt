/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.item

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.callback.PartySelectCallbacks
import com.cobblemon.mod.common.api.text.red
import com.cobblemon.mod.common.battles.BagItemActionResponse
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.item.battle.BagItem
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.battleLang
import com.cobblemon.mod.common.util.getBattleState
import com.cobblemon.mod.common.util.isHeld
import com.cobblemon.mod.common.util.isLookingAt
import com.cobblemon.mod.common.util.party
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Box

/**
 * Interface to make it easier to define items that will, upon use, prompt you to
 * select a Pokémon for it to be used on. This can be by interacting with the Pokémon
 * directly (which will skip the Pokémon selection screen) or by interacting with air
 * which will first show a Pokémon selection screen.
 *
 * Works for battle items too.
 *
 * @author Hiroku
 * @since July 29th, 2023
 */
interface PokemonSelectingItem {
    fun use(player: ServerPlayerEntity, stack: ItemStack): TypedActionResult<ItemStack> {
        val entity = player.world
            .getOtherEntities(player, Box.of(player.pos, 16.0, 16.0, 16.0))
            .filter { player.isLookingAt(it, stepDistance = 0.1F) }
            .minByOrNull { it.distanceTo(player) } as? PokemonEntity?

        player.getBattleState()?.let { (_, actor) ->
            if (bagItem == null) return TypedActionResult.fail(stack)
            val battlePokemon = actor.pokemonList.find { it.effectedPokemon == entity?.pokemon }

            if (!actor.canFitForcedAction()) {
                player.sendMessage(battleLang("bagitem.cannot").red())
                return TypedActionResult.fail(stack)
            }

            if (entity == null) {
                return interactGeneralBattle(player, stack, actor)
            } else if (battlePokemon != null) {
                return interactWithSpecificBattle(player, stack, battlePokemon)
            }
        } ?: run {
            if (entity != null) {
                val pokemon = entity.pokemon
                if (entity.ownerUuid == player.uuid) {
                    return applyToPokemon(player, stack, pokemon) ?: TypedActionResult.pass(stack)
                } else {
                    return TypedActionResult.fail(stack)
                }
            } else {
                return interactGeneral(player, stack)
            }
        }

        return TypedActionResult.pass(stack)
    }

    val bagItem: BagItem?
    fun applyToPokemon(player: ServerPlayerEntity, stack: ItemStack, pokemon: Pokemon): TypedActionResult<ItemStack>?

    fun applyToBattlePokemon(player: ServerPlayerEntity, stack: ItemStack, battlePokemon: BattlePokemon) {
        val battle = battlePokemon.actor.battle
        val bagItem = bagItem
        if (!battlePokemon.actor.canFitForcedAction()) {
            player.sendMessage(battleLang("bagitem.cannot").red())
        } else if (!bagItem!!.canUse(battle, battlePokemon)) {
            player.sendMessage(battleLang("bagitem.invalid").red())
        } else {
            battlePokemon.actor.forceChoose(BagItemActionResponse(bagItem, battlePokemon))
            if (!player.isCreative) {
                stack.decrement(1)
            }
        }
    }

    fun canUseOnPokemon(pokemon: Pokemon): Boolean
    fun canUseOnBattlePokemon(battlePokemon: BattlePokemon): Boolean = bagItem!!.canUse(battlePokemon.actor.battle, battlePokemon)

    fun interactWithSpecificBattle(player: ServerPlayerEntity, stack: ItemStack, battlePokemon: BattlePokemon): TypedActionResult<ItemStack> {
        return if (canUseOnBattlePokemon(battlePokemon)) {
            applyToBattlePokemon(player, stack, battlePokemon)
            TypedActionResult.success(stack)
        } else {
            player.sendMessage(battleLang("bagitem.invalid").red())
            TypedActionResult.fail(stack)
        }
    }

    fun interactGeneral(player: ServerPlayerEntity, stack: ItemStack): TypedActionResult<ItemStack> {
        PartySelectCallbacks.createFromPokemon(
            player = player,
            pokemon = player.party().toList(),
            canSelect = ::canUseOnPokemon,
            handler = { pk ->
                if (stack.isHeld(player)) {
                    applyToPokemon(player, stack, pk)
                    if (!player.isCreative) {
                        stack.decrement(1)
                    }
                }
            }
        )

        return TypedActionResult.success(stack)
    }

    fun interactGeneralBattle(player: ServerPlayerEntity, stack: ItemStack, actor: BattleActor): TypedActionResult<ItemStack> {
        PartySelectCallbacks.createBattleSelect(
            player = player,
            pokemon = actor.pokemonList,
            canSelect = { pk -> canUseOnBattlePokemon(actor.pokemonList.find { it.effectedPokemon == pk.effectedPokemon }!!) },
            handler = { pk -> applyToBattlePokemon(player, stack, actor.pokemonList.find { it.effectedPokemon == pk.effectedPokemon }!!) }
        )

        return TypedActionResult.success(stack)
    }
}