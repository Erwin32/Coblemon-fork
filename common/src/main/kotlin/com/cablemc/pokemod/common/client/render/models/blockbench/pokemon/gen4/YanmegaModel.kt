package com.cablemc.pokemod.common.client.render.models.blockbench.pokemon.gen4

import com.cablemc.pokemod.common.client.render.models.blockbench.asTransformed
import com.cablemc.pokemod.common.client.render.models.blockbench.frame.BiWingedFrame
import com.cablemc.pokemod.common.client.render.models.blockbench.frame.HeadedFrame
import com.cablemc.pokemod.common.client.render.models.blockbench.pokemon.PokemonPose
import com.cablemc.pokemod.common.client.render.models.blockbench.pokemon.PokemonPoseableModel
import com.cablemc.pokemod.common.client.render.models.blockbench.pose.TransformedModelPart
import com.cablemc.pokemod.common.client.render.models.blockbench.wavefunction.triangleFunction
import com.cablemc.pokemod.common.entity.PoseType.Companion.MOVING_POSES
import com.cablemc.pokemod.common.entity.PoseType.Companion.STATIONARY_POSES
import com.cablemc.pokemod.common.entity.PoseType.Companion.UI_POSES
import net.minecraft.client.model.ModelPart
import net.minecraft.util.math.Vec3d

class YanmegaModel(root: ModelPart) : PokemonPoseableModel(), HeadedFrame {
    override val rootPart = root.registerChildWithAllChildren("yanmega")
    override val head = getPart("head")

    override val portraitScale = 1.0F
    override val portraitTranslation = Vec3d(0.0, 0.0, 0.0)

    override val profileScale = 1.0F
    override val profileTranslation = Vec3d(0.0, 0.0, 0.0)

    lateinit var standing: PokemonPose
    lateinit var walk: PokemonPose

    override fun registerPoses() {
        val wingFrame1 = object : BiWingedFrame {
            override val rootPart = this@YanmegaModel.rootPart
            override val leftWing = getPart("wing_left1")
            override val rightWing = getPart("wing_right1")
        }

        val wingFrame2 = object : BiWingedFrame {
            override val rootPart = this@YanmegaModel.rootPart
            override val leftWing = getPart("wing_left2")
            override val rightWing = getPart("wing_right2")
        }

        standing = registerPose(
            poseName = "standing",
            poseTypes = STATIONARY_POSES + UI_POSES,
            idleAnimations = arrayOf(
                singleBoneLook(),
                bedrock("0469_yanmega/yanmega", "ground_idle"),
                wingFrame1.wingFlap(
                    flapFunction = triangleFunction(period = 0.3F, amplitude = 0.4F),
                    timeVariable = { state, _, ageInTicks -> state?.animationSeconds ?: ageInTicks },
                    axis = TransformedModelPart.Z_AXIS
                ),
                wingFrame2.wingFlap(
                    flapFunction = triangleFunction(period = 0.3F, amplitude = 0.4F),
                    timeVariable = { state, _, ageInTicks -> 0.01F + (state?.animationSeconds ?: (ageInTicks / 20)) },
                    axis = TransformedModelPart.Z_AXIS
                )
            ),
            transformedParts = arrayOf(
                rootPart.asTransformed().addPosition(TransformedModelPart.Y_AXIS, -4)
            )
        )

        walk = registerPose(
            poseName = "walk",
            poseTypes = MOVING_POSES,
            idleAnimations = arrayOf(
                singleBoneLook(),
                bedrock("0469_yanmega/yanmega", "ground_idle"),
                wingFrame1.wingFlap(
                    flapFunction = triangleFunction(period = 0.3F, amplitude = 0.4F),
                    timeVariable = { state, _, ageInTicks -> state?.animationSeconds ?: ageInTicks },
                    axis = TransformedModelPart.Z_AXIS
                ),
                wingFrame2.wingFlap(
                    flapFunction = triangleFunction(period = 0.3F, amplitude = 0.4F),
                    timeVariable = { state, _, ageInTicks -> 0.01F + (state?.animationSeconds ?: (ageInTicks / 20)) },
                    axis = TransformedModelPart.Z_AXIS
                )
                //bedrock("0469_yanmega/yanmega", "ground_walk")
            ),
            transformedParts = arrayOf(
                rootPart.asTransformed().addPosition(TransformedModelPart.Y_AXIS, -4)
            )
        )
    }

//    override fun getFaintAnimation(
//        pokemonEntity: PokemonEntity,
//        state: PoseableEntityState<PokemonEntity>
//    ) = if (state.isPosedIn(standing, walk)) bedrockStateful("0469_yanmega/yanmega", "faint") else null
}