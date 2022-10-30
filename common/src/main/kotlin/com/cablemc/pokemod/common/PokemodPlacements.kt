/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common

import com.cablemc.pokemod.common.util.pokemodResource
import dev.architectury.registry.level.biome.BiomeModifications
import net.minecraft.util.registry.RegistryEntry
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier

object PokemodPlacements {

    lateinit var BLACK_APRICORN_TREE: RegistryEntry<PlacedFeature>
    lateinit var BLUE_APRICORN_TREE: RegistryEntry<PlacedFeature>
    lateinit var GREEN_APRICORN_TREE: RegistryEntry<PlacedFeature>
    lateinit var PINK_APRICORN_TREE: RegistryEntry<PlacedFeature>
    lateinit var RED_APRICORN_TREE: RegistryEntry<PlacedFeature>
    lateinit var WHITE_APRICORN_TREE: RegistryEntry<PlacedFeature>
    lateinit var YELLOW_APRICORN_TREE: RegistryEntry<PlacedFeature>

    lateinit var APRICORN_TREES: RegistryEntry<PlacedFeature>

    fun register() {
        BLACK_APRICORN_TREE = PlacedFeatures.register(pokemodResource("black_apricorn_tree").toString(), PokemodConfiguredFeatures.BLACK_APRICORN_TREE, PlacedFeatures.wouldSurvive(
            PokemodBlocks.BLACK_APRICORN_SAPLING.get()))
        BLUE_APRICORN_TREE = PlacedFeatures.register(pokemodResource("blue_apricorn_tree").toString(), PokemodConfiguredFeatures.BLUE_APRICORN_TREE, PlacedFeatures.wouldSurvive(
            PokemodBlocks.BLUE_APRICORN_SAPLING.get()))
        GREEN_APRICORN_TREE = PlacedFeatures.register(pokemodResource("green_apricorn_tree").toString(), PokemodConfiguredFeatures.GREEN_APRICORN_TREE, PlacedFeatures.wouldSurvive(
            PokemodBlocks.GREEN_APRICORN_SAPLING.get()))
        PINK_APRICORN_TREE = PlacedFeatures.register(pokemodResource("pink_apricorn_tree").toString(), PokemodConfiguredFeatures.PINK_APRICORN_TREE, PlacedFeatures.wouldSurvive(
            PokemodBlocks.PINK_APRICORN_SAPLING.get()))
        RED_APRICORN_TREE = PlacedFeatures.register(pokemodResource("red_apricorn_tree").toString(), PokemodConfiguredFeatures.RED_APRICORN_TREE, PlacedFeatures.wouldSurvive(
            PokemodBlocks.RED_APRICORN_SAPLING.get()))
        WHITE_APRICORN_TREE = PlacedFeatures.register(pokemodResource("white_apricorn_tree").toString(), PokemodConfiguredFeatures.WHITE_APRICORN_TREE, PlacedFeatures.wouldSurvive(
            PokemodBlocks.WHITE_APRICORN_SAPLING.get()))
        YELLOW_APRICORN_TREE = PlacedFeatures.register(pokemodResource("yellow_apricorn_tree").toString(), PokemodConfiguredFeatures.YELLOW_APRICORN_TREE, PlacedFeatures.wouldSurvive(
            PokemodBlocks.YELLOW_APRICORN_SAPLING.get()))

        val apricornTreeVariety = ConfiguredFeatures.register(
            pokemodResource("apricorn_trees").toString(), Feature.RANDOM_SELECTOR, RandomFeatureConfig(
                listOf(
                    RandomFeatureEntry(BLACK_APRICORN_TREE, 0.04f),
                    RandomFeatureEntry(BLUE_APRICORN_TREE, 0.10f),
                    RandomFeatureEntry(GREEN_APRICORN_TREE, 0.15f),
                    RandomFeatureEntry(PINK_APRICORN_TREE, 0.12f),
                    RandomFeatureEntry(RED_APRICORN_TREE, 0.33f),
                    RandomFeatureEntry(WHITE_APRICORN_TREE, 0.20f),
                    RandomFeatureEntry(YELLOW_APRICORN_TREE, 0.06f)
                ), RED_APRICORN_TREE
            )
        )

        APRICORN_TREES = PlacedFeatures.register(pokemodResource("apricorn_trees").toString(), apricornTreeVariety, VegetationPlacedFeatures.modifiers(RarityFilterPlacementModifier.of(8)))

        BiomeModifications.addProperties({ context -> true}, { context, properties ->
            properties.generationProperties.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, APRICORN_TREES)
        })
    }

}