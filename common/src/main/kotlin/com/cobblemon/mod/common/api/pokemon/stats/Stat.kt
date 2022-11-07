/*
 * Copyright (C) 2022 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.pokemon.stats

interface Stat {
    val id: String
    val name: String
        get() = "cobblemon.stat.$id.name"

    class Dummy(
        override val id: String,
        override val name: String = id
    ) : Stat
}