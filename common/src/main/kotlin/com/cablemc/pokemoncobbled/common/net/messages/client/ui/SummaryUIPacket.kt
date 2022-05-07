package com.cablemc.pokemoncobbled.common.net.messages.client.ui

import com.cablemc.pokemoncobbled.common.api.net.NetworkPacket
import com.cablemc.pokemoncobbled.common.pokemon.Pokemon
import net.minecraft.network.PacketByteBuf

class SummaryUIPacket internal constructor(): NetworkPacket {
    constructor(vararg pokemon: Pokemon, editable: Boolean = true) : this() {
        pokemonArray.addAll(pokemon)
        this.editable = editable
    }

    val pokemonArray = mutableListOf<Pokemon>()
    var editable = true

    override fun encode(buffer: PacketByteBuf) {
        buffer.writeBoolean(editable)
        buffer.writeInt(pokemonArray.size)
        pokemonArray.forEach {
            it.saveToBuffer(buffer)
        }
    }

    override fun decode(buffer: PacketByteBuf) {
        editable = buffer.readBoolean()
        val amount = buffer.readInt()
        for (i in 0 until amount) {
            pokemonArray.add(Pokemon().loadFromBuffer(buffer))
        }
    }
}