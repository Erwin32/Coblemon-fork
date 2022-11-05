/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common.api.spawning.mixins

import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkStatus

interface CachedOnlyChunkAccessor {

    fun `cobbled$request`(x: Int, z: Int, status: ChunkStatus): Chunk?

}