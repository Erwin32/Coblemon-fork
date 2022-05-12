package com.cablemc.pokemoncobbled.common.api.spawning.spawner

import com.cablemc.pokemoncobbled.common.PokemonCobbled.config
import com.cablemc.pokemoncobbled.common.api.spawning.SpawnerManager
import com.cablemc.pokemoncobbled.common.api.spawning.detail.SpawnPool
import com.cablemc.pokemoncobbled.common.util.getPlayer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.MathHelper.*
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 * A spawner that works around a single player. It will do basic tracking of a player's speed
 * and project that into the future and spawn in that direction.
 *
 * @author Hiroku
 * @since February 14th, 2022
 */
class PlayerSpawner(player: ServerPlayerEntity, spawns: SpawnPool, manager: SpawnerManager) : AreaSpawner(player.name.string, spawns, manager) {
    val uuid: UUID = player.uuid

    override fun getArea(): SpawningArea? {
        val player = uuid.getPlayer() ?: return null
        val sliceDiameter = config.worldSliceDiameter
        val sliceHeight = config.worldSliceHeight

        val rand = Random()

        val center = player.pos

        val r = nextBetween(rand, config.minimumSliceDistanceFromPlayer, config.maximumSliceDistanceFromPlayer)
        val thetatemp = atan(player.velocity.z / player.velocity.x) + nextBetween(rand, -PI/2, PI/2 )
        val theta = if (player.velocity.horizontalLength() < 0.1) {
            rand.nextDouble() * 2 * PI
        } else if (player.velocity.x < 0) {
            PI - thetatemp
        } else {
            thetatemp
        }
        val x = center.x + r * cos(theta)
        val z = center.z + r * sin(theta)

        return SpawningArea(
            cause = player,
            world = player.world,
            baseX = ceil(x - sliceDiameter / 2F),
            baseY = ceil(center.y - sliceHeight / 2F),
            baseZ = ceil(z - sliceDiameter / 2F),
            length = sliceDiameter,
            height = sliceHeight,
            width = sliceDiameter
        )
    }
}