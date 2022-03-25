package com.cablemc.pokemoncobbled.common

import com.cablemc.pokemoncobbled.common.world.level.block.ApricornBlock
import com.cablemc.pokemoncobbled.common.world.level.block.ApricornSaplingBlock
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor
import java.util.function.Supplier

object CobbledBlocks {
    private val blockRegister = DeferredRegister.create(PokemonCobbled.MODID, Registry.BLOCK_REGISTRY)
    private fun <T : Block> queue(name: String, block: T) = blockRegister.register(name) { block }

    val APRICORN_LOG = queue("apricorn_log", log(MaterialColor.PODZOL, MaterialColor.COLOR_BROWN))
    val STRIPPED_APRICORN_LOG = queue("stripped_apricorn_log", log(MaterialColor.PODZOL, MaterialColor.PODZOL))
    val APRICORN_WOOD = queue("apricorn_wood", log(MaterialColor.PODZOL, MaterialColor.PODZOL))
    val STRIPPED_APRICORN_WOOD = queue("stripped_apricorn_wood", log(MaterialColor.PODZOL, MaterialColor.PODZOL))
    val APRICORN_PLANKS = queue("apricorn_planks", Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL)
        .strength(2.0f, 3.0f)
        .sound(SoundType.WOOD)))
    val APRICORN_LEAVES = queue("apricorn_leaves", leaves(SoundType.GRASS))

    private val PLANT_PROPERTIES = BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)
    val BLACK_APRICORN_SAPLING = queue("black_apricorn_sapling", ApricornSaplingBlock(PLANT_PROPERTIES, "black"))
    val BLUE_APRICORN_SAPLING = queue("blue_apricorn_sapling", ApricornSaplingBlock(PLANT_PROPERTIES, "blue"))
    val GREEN_APRICORN_SAPLING = queue("green_apricorn_sapling", ApricornSaplingBlock(PLANT_PROPERTIES, "green"))
    val PINK_APRICORN_SAPLING = queue("pink_apricorn_sapling", ApricornSaplingBlock(PLANT_PROPERTIES, "pink"))
    val RED_APRICORN_SAPLING = queue("red_apricorn_sapling", ApricornSaplingBlock(PLANT_PROPERTIES, "red"))
    val WHITE_APRICORN_SAPLING = queue("white_apricorn_sapling", ApricornSaplingBlock(PLANT_PROPERTIES, "white"))
    val YELLOW_APRICORN_SAPLING = queue("yellow_apricorn_sapling", ApricornSaplingBlock(PLANT_PROPERTIES, "yellow"))

    val BLACK_APRICORN = queue("black_apricorn", ApricornBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion()) { CobbledItems.BLACK_APRICORN.get() })
    val BLUE_APRICORN = queue("blue_apricorn", ApricornBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion()) { CobbledItems.BLUE_APRICORN.get() })
    val GREEN_APRICORN = queue("green_apricorn", ApricornBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion()) { CobbledItems.GREEN_APRICORN.get() })
    val PINK_APRICORN = queue("pink_apricorn", ApricornBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion()) { CobbledItems.PINK_APRICORN.get() })
    val RED_APRICORN = queue("red_apricorn", ApricornBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion()) { CobbledItems.RED_APRICORN.get() })
    val WHITE_APRICORN = queue("white_apricorn", ApricornBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion()) { CobbledItems.WHITE_APRICORN.get() })
    val YELLOW_APRICORN = queue("yellow_apricorn", ApricornBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion()) { CobbledItems.YELLOW_APRICORN.get() })

    fun register() {
        blockRegister.register()
    }

    /**
     * Helper method for creating logs
     * copied over from Vanilla
     */
    private fun log(arg: MaterialColor, arg2: MaterialColor): RotatedPillarBlock {
        return RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD) { arg3: BlockState ->
            if (arg3.getValue(RotatedPillarBlock.AXIS) === Direction.Axis.Y) arg else arg2
        }.strength(2.0f).sound(SoundType.WOOD))
    }

    /**
     * Helper method for creating leaves
     * copied over from Vanilla
     */
    private fun leaves(arg: SoundType): LeavesBlock {
        return LeavesBlock(
            BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2f).randomTicks().sound(arg).noOcclusion()
                .isValidSpawn { arg: BlockState, arg2: BlockGetter, arg3: BlockPos, arg4: EntityType<*> ->
                    ocelotOrParrot(arg, arg2, arg3, arg4)
                }.isSuffocating { arg: BlockState, arg2: BlockGetter, arg3: BlockPos ->
                    never(arg, arg2, arg3)
                }.isViewBlocking { arg: BlockState, arg2: BlockGetter, arg3: BlockPos ->
                    never(arg, arg2, arg3)
                })
    }

    private fun always(arg: BlockState, arg2: BlockGetter, arg3: BlockPos): Boolean {
        return true
    }

    private fun never(arg: BlockState, arg2: BlockGetter, arg3: BlockPos): Boolean {
        return false
    }

    private fun ocelotOrParrot(arg: BlockState, arg2: BlockGetter, arg3: BlockPos, arg4: EntityType<*>): Boolean {
        return arg4 === EntityType.OCELOT || arg4 === EntityType.PARROT
    }
}