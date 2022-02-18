package com.cablemc.pokemoncobbled.common.api.storage

import com.cablemc.pokemoncobbled.common.api.Priority
import com.cablemc.pokemoncobbled.common.api.storage.factory.PokemonStoreFactory
import com.cablemc.pokemoncobbled.common.api.storage.party.PartyStore
import com.cablemc.pokemoncobbled.common.net.PokemonCobbledNetwork.sendPacket
import com.cablemc.pokemoncobbled.common.net.messages.client.storage.party.SetPartyReferencePacket
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.Priority
import net.minecraftforge.eventbus.api.SubscribeEvent
import java.util.UUID

/**
 * Manages the providing of [PokemonStore]s for party, PC, and custom use. The main utilities of this class
 * include the getter functions to provide the stores you need, as well as registering custom [PokemonStoreFactory]
 * implementations at specific [Priority] levels to let Pokémon Cobbled use custom stores or custom store
 * factories.
 *
 * @author Hiroku
 * @since November 29th, 2021
 */
object PokemonStoreManager {
    private val factories = Array(Priority.values().size) { mutableListOf<PokemonStoreFactory>() }

    fun registerFactory(priority: Priority, factory: PokemonStoreFactory) {
        factories[priority.ordinal].add(factory)
    }

    fun unregisterFactory(factory: PokemonStoreFactory) {
        factories.forEach { it.remove(factory) }
    }

    fun getParty(player: ServerPlayer) = getParty(player.uuid)

    fun getParty(uuid: UUID): PartyStore {
        for (factoryList in factories) {
            for (factory in factoryList) {
                factory.getPlayerParty(uuid)?.run { return this }
            }
        }

        throw NoPokemonStoreException("No factory was able to provide a party for $uuid - this should not be possible unless someone has removed the default provider!")
    }

    fun getParties(uuid: UUID): Iterable<PartyStore> {
        val parties = mutableListOf<PartyStore>()
        for (factoryList in factories) {
            for (factory in factoryList) {
                factory.getPlayerParty(uuid)?.let { parties.add(it) }
            }
        }
        return parties.asIterable()
    }

    inline fun <E : StorePosition, reified T : PokemonStore<E>> getCustomStore(uuid: UUID) = getCustomStore(T::class.java, uuid)
    fun <E : StorePosition, T : PokemonStore<E>> getCustomStore(storeClass: Class<T>, uuid: UUID): T? {
        for (factoryList in factories) {
            for (factory in factoryList) {
                factory.getCustomStore(storeClass, uuid)?.run { return this }
            }
        }

        return null
    }

    @SubscribeEvent
    fun on(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.player
        if (player is ServerPlayer) {
            val parties = getParties(player.uuid)
            parties.forEach { party -> party.sendTo(player) }
            player.sendPacket(SetPartyReferencePacket(parties.first().uuid))
        }
    }
}