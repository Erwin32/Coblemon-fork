package com.cablemc.pokemoncobbled.common.net.serverhandling.storage.pc

import com.cablemc.pokemoncobbled.common.CobbledNetwork
import com.cablemc.pokemoncobbled.common.api.storage.pc.link.PCLinkManager
import com.cablemc.pokemoncobbled.common.net.messages.client.storage.pc.ClosePCPacket
import com.cablemc.pokemoncobbled.common.net.messages.server.storage.pc.MovePCPokemonPacket
import com.cablemc.pokemoncobbled.common.net.serverhandling.ServerPacketHandler
import net.minecraft.server.network.ServerPlayerEntity

object MovePCPokemonHandler : ServerPacketHandler<MovePCPokemonPacket> {
    override fun invokeOnServer(packet: MovePCPokemonPacket, ctx: CobbledNetwork.NetworkContext, player: ServerPlayerEntity) {
        val pc = PCLinkManager.getPC(player) ?: return run { ClosePCPacket().sendToPlayer(player) }
        val pokemon = pc[packet.oldPosition] ?: return
        if (pokemon.uuid != packet.pokemonID) {
            return
        }
        if (!pc.isValidPosition(packet.newPosition)) {
            return
        }
        val existingPokemon = pc[packet.newPosition]
        if (existingPokemon == null) {
            pc.move(pokemon, packet.newPosition)
        } else {
            // This should've been a swap if the slot is already taken. Seems like desync?
        }
    }
}