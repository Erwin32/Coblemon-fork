/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.storage.adapter.database

import com.cobblemon.mod.common.api.storage.PokemonStore
import com.cobblemon.mod.common.api.storage.StorePosition
import com.cobblemon.mod.common.api.storage.adapter.CobblemonAdapterParent
import com.cobblemon.mod.common.api.storage.adapter.flatfile.FileStoreAdapter
import com.cobblemon.mod.common.api.storage.adapter.flatfile.JSONStoreAdapter
import com.cobblemon.mod.common.api.storage.adapter.flatfile.NBTStoreAdapter
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore
import com.cobblemon.mod.common.api.storage.pc.PCStore
import com.cobblemon.mod.common.util.server
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReplaceOptions
import net.minecraft.util.WorldSavePath
import org.bson.Document
import java.util.UUID

/**
 * A [FileStoreAdapter] for MongoDB. This allows a [PokemonStore] to be serialized to a MongoDB database.
 *
 * @author Pebbles
 * @since August 23rd, 2023
 */
open class MongoDBStoreAdapter(
    private val mongoClient: MongoClient,
    private val databaseName: String,
) : CobblemonAdapterParent<JsonObject>(), FileStoreAdapter<JsonObject> {
    private fun getCollection(storeClass: Class<out PokemonStore<*>>): MongoCollection<Document> {
        val collectionName = when (storeClass) {
            PlayerPartyStore::class.java -> "PlayerPartyCollection"
            PCStore::class.java -> "PCCollection"
            else -> "OtherCollection"
        }
        return mongoClient.getDatabase(databaseName).getCollection(collectionName)
    }

    private val gson: Gson = Gson()

    override fun <E : StorePosition, T : PokemonStore<E>> serialize(store: T): JsonObject {
        return store.saveToJSON(JsonObject())
    }

    override fun save(storeClass: Class<out PokemonStore<*>>, uuid: UUID, serialized: JsonObject) {
        val document = Document.parse(gson.toJson(serialized))
        document.put("uuid", uuid.toString())

        val collection = getCollection(storeClass)
        val filter = Document("uuid", uuid.toString())
        collection.replaceOne(filter, document, ReplaceOptions().upsert(true))
    }

    override fun <E : StorePosition, T : PokemonStore<E>> provide(storeClass: Class<T>, uuid: UUID): T? {
        val server = server()!!
        val pokemonStoreRoot = server.getSavePath(WorldSavePath.ROOT).resolve("pokemon").toFile()
        val jsonAdapter = JSONStoreAdapter(
            pokemonStoreRoot.absolutePath,
            useNestedFolders = true,
            folderPerClass = true
        )
        val nbtAdapter = NBTStoreAdapter(pokemonStoreRoot.absolutePath, useNestedFolders = true, folderPerClass = true)

        // 1. Check if data exists in MongoDB
        val collection = getCollection(storeClass)
        val filter = Document("uuid", uuid.toString())
        val document = collection.find(filter).first()

        if (document != null) {
            val json = gson.fromJson(document.toJson(), JsonObject::class.java)
            val store = storeClass.getConstructor(UUID::class.java).newInstance(uuid)
            store.loadFromJSON(json)
            return store
        }

        // 2. Fallback to checking JSON and NBT
        val nbtStore = nbtAdapter.provide(storeClass, uuid)
        if (nbtStore != null) {
            save(storeClass, uuid, serialize(nbtStore))
            return nbtStore
        }

        val jsonStore = jsonAdapter.provide(storeClass, uuid)
        if (jsonStore != null) {
            save(storeClass, uuid, serialize(jsonStore))
            return jsonStore
        }

        return null
    }

}