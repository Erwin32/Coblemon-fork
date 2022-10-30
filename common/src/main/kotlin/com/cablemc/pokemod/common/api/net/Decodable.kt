/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common.api.net

import net.minecraft.network.PacketByteBuf

interface Decodable {

    /**
     * Reads an updates this instance based on the given buffer.
     *
     * @param buffer The [PacketByteBuf] being read from.
     */
    fun decode(buffer: PacketByteBuf)

}