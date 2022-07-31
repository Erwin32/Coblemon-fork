package com.cablemc.pokemoncobbled.common.pokemon.properties

import com.cablemc.pokemoncobbled.common.api.properties.CustomPokemonPropertyType
import com.cablemc.pokemoncobbled.common.pokemon.Pokemon

/**
 * A type of [CustomPokemonPropertyType] handling a [FlagProperty] which, when
 * present, indicates that the Pokémon cannot be caught in- or out-of-battle.
 *
 * @author Hiroku
 * @since July 1st, 2022
 */
object UncatchableProperty : CustomPokemonPropertyType<FlagProperty> {
    override val keys = setOf("uncatchable")
    override val needsKey = true

    override fun fromString(value: String?) =
        when {
            value == null || value.lowercase() in listOf("true", "yes") -> uncatchable()
            value.lowercase() in listOf("false", "no") -> catchable()
            else -> null
        }

    fun catchable() = FlagProperty(keys.first(), true)
    fun uncatchable() = FlagProperty(keys.first(), false)

    fun isCatchable(pokemon: Pokemon) = pokemon.customProperties.none { it is FlagProperty && it.key in keys }
}