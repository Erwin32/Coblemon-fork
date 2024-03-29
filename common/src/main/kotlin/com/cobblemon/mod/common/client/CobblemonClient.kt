/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client

import com.cobblemon.mod.common.*
import com.cobblemon.mod.common.Cobblemon.LOGGER
import com.cobblemon.mod.common.api.scheduling.ScheduledTaskTracker
import com.cobblemon.mod.common.api.text.gray
import com.cobblemon.mod.common.client.battle.ClientBattle
import com.cobblemon.mod.common.client.gui.PartyOverlay
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay
import com.cobblemon.mod.common.client.particle.BedrockParticleEffectRepository
import com.cobblemon.mod.common.client.render.block.BerryBlockRenderer
import com.cobblemon.mod.common.client.render.block.HealingMachineRenderer
import com.cobblemon.mod.common.client.render.item.CobblemonBuiltinItemRendererRegistry
import com.cobblemon.mod.common.client.render.item.PokemonItemRenderer
import com.cobblemon.mod.common.client.render.layer.PokemonOnShoulderRenderer
import com.cobblemon.mod.common.client.render.models.blockbench.bedrock.animation.BedrockAnimationRepository
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokeBallModelRepository
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository
import com.cobblemon.mod.common.client.render.pokeball.PokeBallRenderer
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer
import com.cobblemon.mod.common.client.starter.ClientPlayerData
import com.cobblemon.mod.common.client.storage.ClientStorageManager
import com.cobblemon.mod.common.client.trade.ClientTrade
import com.cobblemon.mod.common.data.CobblemonDataProvider
import com.cobblemon.mod.common.item.PokeBallItem
import com.cobblemon.mod.common.client.render.models.blockbench.repository.BerryModelRepository
import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.cobblemon.mod.common.util.DataKeys
import com.cobblemon.mod.common.util.asTranslated
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Language

object CobblemonClient {
    lateinit var implementation: CobblemonClientImplementation
    val storage = ClientStorageManager()
    var trade: ClientTrade? = null
    var battle: ClientBattle? = null
    var clientPlayerData = ClientPlayerData()
    /** If true then we won't bother them anymore about choosing a starter even if it's a thing they can do. */
    var checkedStarterScreen = false
    var requests = ClientPlayerActionRequests()


    val overlay: PartyOverlay by lazy { PartyOverlay() }
    val battleOverlay: BattleOverlay by lazy { BattleOverlay() }

    fun onLogin() {
        clientPlayerData = ClientPlayerData()
        requests = ClientPlayerActionRequests()
        storage.onLogin()
        CobblemonDataProvider.canReload = false
    }

    fun onLogout() {
        storage.onLogout()
        battle = null
        battleOverlay.onLogout()
        ScheduledTaskTracker.clear()
        checkedStarterScreen = false
        CobblemonDataProvider.canReload = true
    }

    fun initialize(implementation: CobblemonClientImplementation) {
        LOGGER.info("Initializing Cobblemon client")
        this.implementation = implementation

        PlatformEvents.CLIENT_PLAYER_LOGIN.subscribe { onLogin() }
        PlatformEvents.CLIENT_PLAYER_LOGOUT.subscribe { onLogout() }

        this.implementation.registerBlockEntityRenderer(CobblemonBlockEntities.HEALING_MACHINE, ::HealingMachineRenderer)
        this.implementation.registerBlockEntityRenderer(CobblemonBlockEntities.BERRY, ::BerryBlockRenderer)

        registerBlockRenderTypes()
        //registerColors()
        registerFlywheelRenderers()

        LOGGER.info("Registering custom BuiltinItemRenderers")
        CobblemonBuiltinItemRendererRegistry.register(CobblemonItems.POKEMON_MODEL, PokemonItemRenderer())

        PlatformEvents.CLIENT_ITEM_TOOLTIP.subscribe { event ->
            val stack = event.stack
            val lines = event.lines
            @Suppress("DEPRECATION")
            if (stack.item.registryEntry.key.isPresent && stack.item.registryEntry.key.get().value.namespace == Cobblemon.MODID) {
                if (stack.nbt?.getBoolean(DataKeys.HIDE_TOOLTIP) == true) {
                    return@subscribe
                }
                val language = Language.getInstance()
                val key = this.baseLangKeyForItem(stack)
                if (language.hasTranslation(key)) {
                    lines.add(key.asTranslated().gray())
                }
                var i = 1
                var listKey = "${key}_$i"
                while(language.hasTranslation(listKey)) {
                    lines.add(listKey.asTranslated().gray())
                    listKey = "${key}_${++i}"
                }
            }
        }
    }

    fun registerFlywheelRenderers() {
//        InstancedRenderRegistry
//            .configure(CobblemonBlockEntities.BERRY)
//            .alwaysSkipRender()
//            .factory(::BerryEntityInstance)
//            .apply()
    }

    /*
    fun registerColors() {
        this.implementation.registerBlockColors(BlockColorProvider { _, _, _, _ ->
            return@BlockColorProvider 0xE0A33A
        }, CobblemonBlocks.APRICORN_LEAVES)
        this.implementation.registerItemColors(ItemColorProvider { _, _ ->
            return@ItemColorProvider 0xE0A33A
        }, CobblemonItems.APRICORN_LEAVES)
    }
    */

    private fun registerBlockRenderTypes() {

        this.implementation.registerBlockRenderType(RenderLayer.getCutoutMipped(), CobblemonBlocks.APRICORN_LEAVES)

        this.implementation.registerBlockRenderType(
            RenderLayer.getCutout(),
            CobblemonBlocks.APRICORN_DOOR,
            CobblemonBlocks.APRICORN_TRAPDOOR,
            CobblemonBlocks.BLACK_APRICORN_SAPLING,
            CobblemonBlocks.BLUE_APRICORN_SAPLING,
            CobblemonBlocks.GREEN_APRICORN_SAPLING,
            CobblemonBlocks.PINK_APRICORN_SAPLING,
            CobblemonBlocks.RED_APRICORN_SAPLING,
            CobblemonBlocks.WHITE_APRICORN_SAPLING,
            CobblemonBlocks.YELLOW_APRICORN_SAPLING,
            CobblemonBlocks.BLACK_APRICORN,
            CobblemonBlocks.BLUE_APRICORN,
            CobblemonBlocks.GREEN_APRICORN,
            CobblemonBlocks.PINK_APRICORN,
            CobblemonBlocks.RED_APRICORN,
            CobblemonBlocks.WHITE_APRICORN,
            CobblemonBlocks.YELLOW_APRICORN,
            CobblemonBlocks.HEALING_MACHINE,
            CobblemonBlocks.MEDICINAL_LEEK,
            CobblemonBlocks.HEALING_MACHINE,
            CobblemonBlocks.RED_MINT,
            CobblemonBlocks.BLUE_MINT,
            CobblemonBlocks.CYAN_MINT,
            CobblemonBlocks.PINK_MINT,
            CobblemonBlocks.GREEN_MINT,
            CobblemonBlocks.WHITE_MINT,
            CobblemonBlocks.PASTURE,
            CobblemonBlocks.ENERGY_ROOT,
            CobblemonBlocks.BIG_ROOT,
            CobblemonBlocks.REVIVAL_HERB,
            CobblemonBlocks.VIVICHOKE_SEEDS,
            CobblemonBlocks.PEP_UP_FLOWER,
            CobblemonBlocks.POTTED_PEP_UP_FLOWER,
            CobblemonBlocks.REVIVAL_HERB,
            *CobblemonBlocks.berries().values.toTypedArray()
        )
    }

    fun beforeChatRender(context: DrawContext, partialDeltaTicks: Float) {
        if (battle == null) {
            overlay.render(context, partialDeltaTicks)
        } else {
            battleOverlay.render(context, partialDeltaTicks)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun onAddLayer(skinMap: Map<String, EntityRenderer<out PlayerEntity>>?) {
        var renderer: LivingEntityRenderer<PlayerEntity, PlayerEntityModel<PlayerEntity>>? = skinMap?.get("default") as LivingEntityRenderer<PlayerEntity, PlayerEntityModel<PlayerEntity>>
        renderer?.addFeature(PokemonOnShoulderRenderer(renderer))
        renderer = skinMap["slim"] as LivingEntityRenderer<PlayerEntity, PlayerEntityModel<PlayerEntity>>?
        renderer?.addFeature(PokemonOnShoulderRenderer(renderer))
    }

    fun registerPokemonRenderer(context: EntityRendererFactory.Context): PokemonRenderer {
        LOGGER.info("Registering Pokémon renderer")
        return PokemonRenderer(context)
    }

    fun registerPokeBallRenderer(context: EntityRendererFactory.Context): PokeBallRenderer {
        LOGGER.info("Registering PokéBall renderer")
        return PokeBallRenderer(context)
    }

    fun reloadCodedAssets(resourceManager: ResourceManager) {
        LOGGER.info("Loading assets...")
        BedrockParticleEffectRepository.loadEffects(resourceManager)
        BedrockAnimationRepository.loadAnimations(
            resourceManager = resourceManager,
            directories = PokemonModelRepository.animationDirectories + PokeBallModelRepository.animationDirectories
        )
        PokemonModelRepository.reload(resourceManager)
        PokeBallModelRepository.reload(resourceManager)
        BerryModelRepository.reload(resourceManager)
        LOGGER.info("Loaded assets")
//        PokeBallModelRepository.reload(resourceManager)
    }

    fun endBattle() {
        battle = null
    }

    private fun baseLangKeyForItem(stack: ItemStack): String {
        if (stack.item is PokeBallItem) {
            val asPokeball = stack.item as PokeBallItem
            return "item.${asPokeball.pokeBall.name.namespace}.${asPokeball.pokeBall.name.path}.tooltip"
        }
        return "${stack.translationKey}.tooltip"
    }

}
