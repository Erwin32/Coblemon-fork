/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common.api.events.battles

import com.cablemc.pokemod.common.api.battles.model.PokemonBattle
import com.cablemc.pokemod.common.api.battles.model.actor.BattleActor

/**
 * Event fired when a battle is won by some number of [BattleActor]s.
 *
 * @author MoeBoy76
 * @since November 3rd, 2022
 */
data class BattleVictoryEvent (
    val battle: PokemonBattle,
    val winners : List<BattleActor>
)