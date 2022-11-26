/*
 * Copyright (C) 2022 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.render.models.blockbench.pokemon.gen7

import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPose
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPoseableModel
import com.cobblemon.mod.common.entity.PoseType
import net.minecraft.client.model.ModelPart
import net.minecraft.util.math.Vec3d

class PyukumukuModel(root: ModelPart) : PokemonPoseableModel() {
    override val rootPart = root.registerChildWithAllChildren("pyukumuku")

    override val portraitScale = 1.65F
    override val portraitTranslation = Vec3d(-0.1, -0.8, 0.0)
    override val profileScale = 1.0F
    override val profileTranslation = Vec3d(0.0, 0.0, 0.0)

//    lateinit var sleep: PokemonPose
    lateinit var standing: PokemonPose

    override fun registerPoses() {
//        sleep = registerPose(
//            poseType = PoseType.SLEEP,
//            idleAnimations = arrayOf(bedrock("0771_pyukumuku/pyukumuku", "sleep"))
//        )

        standing = registerPose(
            poseName = "standing",
            poseTypes = PoseType.ALL_POSES,// - PoseType.SLEEP,
            idleAnimations = emptyArray()// arrayOf(bedrock("0771_pyukumuku/pyukumuku", "ground_idle"))
        )
    }

//    override fun getFaintAnimation(
//        pokemonEntity: PokemonEntity,
//        state: PoseableEntityState<PokemonEntity>
//    ) = if (state.isPosedIn(standing)) bedrockStateful("0771_pyukumuku/pyukumuku", "faint") else null
}