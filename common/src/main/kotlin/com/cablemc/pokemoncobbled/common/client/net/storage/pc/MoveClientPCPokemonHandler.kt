package com.cablemc.pokemoncobbled.common.client.net.storage.pc

import com.cablemc.pokemoncobbled.common.CobbledNetwork
import com.cablemc.pokemoncobbled.common.client.PokemonCobbledClient
import com.cablemc.pokemoncobbled.common.client.net.ClientPacketHandler
import com.cablemc.pokemoncobbled.common.net.messages.client.storage.pc.MoveClientPCPokemonPacket

object MoveClientPCPokemonHandler : ClientPacketHandler<MoveClientPCPokemonPacket> {
    override fun invokeOnClient(packet: MoveClientPCPokemonPacket, ctx: CobbledNetwork.NetworkContext) {
        PokemonCobbledClient.storage.moveInPC(packet.storeID, packet.pokemonID, packet.newPosition)
    }
}