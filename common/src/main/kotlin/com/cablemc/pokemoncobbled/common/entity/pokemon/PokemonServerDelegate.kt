package com.cablemc.pokemoncobbled.common.entity.pokemon

import com.cablemc.pokemoncobbled.common.api.entity.PokemonSideDelegate
import com.cablemc.pokemoncobbled.common.pokemon.Pokemon
import com.cablemc.pokemoncobbled.common.pokemon.activestate.ActivePokemonState
import com.cablemc.pokemoncobbled.common.pokemon.activestate.SentOutState
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

/** Handles purely server logic for a Pokémon */
class PokemonServerDelegate : PokemonSideDelegate {
    lateinit var entity: PokemonEntity
    override fun changePokemon(pokemon: Pokemon) {
        entity.initGoals()
    }

    override fun initialize(entity: PokemonEntity) {
        this.entity = entity
        with(entity) {
            speed = 0.35F
            entity.despawner.beginTracking(this)
        }
    }

    override fun tick(entity: PokemonEntity) {
        val state = entity.pokemon.state
        if (state !is ActivePokemonState || state.entity != entity) {
            if (state !is ActivePokemonState || state.entity != entity) {
                entity.pokemon.state = SentOutState(entity)
            }
        }
        if (entity.health.toInt() != entity.pokemon.currentHealth && entity.health > 0) {
            entity.health = entity.pokemon.currentHealth.toFloat()
        }
        if (entity.ownerUuid != entity.pokemon.getOwnerUUID()) {
            entity.ownerUuid = entity.pokemon.getOwnerUUID()
        }
        if (entity.pokemon.species.nationalPokedexNumber != entity.dexNumber.get()) {
            entity.dexNumber.set(entity.pokemon.species.nationalPokedexNumber)
        }
        if (entity.aspects.get() != entity.pokemon.aspects) {
            entity.aspects.set(entity.pokemon.aspects)
        }
        val isMoving = entity.velocity.length() > 0.1
        if (isMoving && !entity.isMoving.get()) {
            entity.isMoving.set(true)
        } else if (!isMoving && entity.isMoving.get()) {
            entity.isMoving.set(false)
        }
    }

    override fun drop(source: DamageSource?) {
        val player = source?.source as? ServerPlayerEntity
        if (entity.pokemon.isWild()) {
            val drops = entity.drops ?: return
            drops.drop(entity, entity.world as ServerWorld, entity.pos, player)
        }
    }
}