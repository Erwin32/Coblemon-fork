/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common.pokemon.status

import com.cablemc.pokemod.common.Pokemod
import com.cablemc.pokemod.common.api.pokemon.status.Status
import com.cablemc.pokemod.common.pokemon.Pokemon
import com.cablemc.pokemod.common.util.asTranslated
import kotlin.random.Random
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

/**
 * Represents a status that persists outside of battle.
 *
 * @author Deltric
 */
open class PersistentStatus(
    name: Identifier,
    showdownName: String,
    applyMessage: String,
    removeMessage: String?,
    private val defaultDuration: IntRange = 0..0
) : Status(name, showdownName, applyMessage, removeMessage) {
    /**
     * Called when a status duration is expired.
     */
    open fun onStatusExpire(player: ServerPlayerEntity, pokemon: Pokemon, random: Random) {
        if (removeMessage != null) {
            player.sendMessage(removeMessage.asTranslated(pokemon.displayName))
        }
    }

    /**
     * Called every second on the Pokémon for the status
     */
    open fun onSecondPassed(player: ServerPlayerEntity, pokemon: Pokemon, random: Random) {

    }

    /**
     * The random period that this status could last.
     * @return the random period of the status.
     */
    fun statusPeriod(): IntRange {
        return Pokemod.config.passiveStatuses[name.toString()] ?: defaultDuration
    }

    /**
     * The status's period as a config entry.
     * @return Status id with random period as a pair.
     */
    fun configEntry(): Pair<String, IntRange> {
        return name.toString() to defaultDuration
    }
}