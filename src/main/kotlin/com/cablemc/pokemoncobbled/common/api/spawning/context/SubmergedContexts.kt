package com.cablemc.pokemoncobbled.common.api.spawning.context

import com.cablemc.pokemoncobbled.common.api.spawning.WorldSlice
import com.cablemc.pokemoncobbled.common.api.spawning.influence.SpawningInfluence
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * A type of area based spawning context with a fluid base block.
 *
 * @author Hiroku
 * @since February 7th, 2022
 */
abstract class SubmergedSpawningContext(
    cause: Any,
    level: Level,
    position: BlockPos,
    light: Int,
    skyAbove: Boolean,
    influences: MutableList<SpawningInfluence>,
    width: Int,
    height: Int,
    val depth: Int,
    nearbyBlocks: Set<BlockState>,
    slice: WorldSlice
) : AreaSpawningContext(cause, level, position, light, skyAbove, influences, width, height, nearbyBlocks, slice) {
    val fluidBlock = slice.getBlockState(position.x, position.y, position.z).block
    val fluidState = slice.getBlockState(position.x, position.y, position.z).fluidState
}

/**
 * A spawning context that occurs in pools of water at least 2 blocks deep.
 *
 * @author Hiroku
 * @since February 7th, 2022
 */
open class UnderwaterSpawningContext(
    cause: Any,
    level: Level,
    position: BlockPos,
    light: Int,
    skyAbove: Boolean,
    influences: MutableList<SpawningInfluence>,
    width: Int,
    height: Int,
    depth: Int,
    nearbyBlocks: Set<BlockState>,
    slice: WorldSlice
): SubmergedSpawningContext(cause, level, position, light, skyAbove, influences, width, height, depth, nearbyBlocks, slice)

/**
 * A spawning context that occurs in pools of lava at least 2 blocks deep.
 *
 * @author Hiroku
 * @since February 7th, 2022
 */
open class UnderlavaSpawningContext(
    cause: Any,
    level: Level,
    position: BlockPos,
    light: Int,
    skyAbove: Boolean,
    influences: MutableList<SpawningInfluence>,
    width: Int,
    height: Int,
    depth: Int,
    nearbyBlocks: Set<BlockState>,
    slice: WorldSlice
): SubmergedSpawningContext(cause, level, position, light, skyAbove, influences, width, height, depth, nearbyBlocks, slice)