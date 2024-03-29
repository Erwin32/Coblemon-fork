/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common

import com.cobblemon.mod.common.api.mulch.MulchVariant
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.api.pokemon.Natures
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.pokemon.status.Statuses
import com.cobblemon.mod.common.block.BerryBlock
import com.cobblemon.mod.common.block.MintBlock.MintType
import com.cobblemon.mod.common.item.*
import com.cobblemon.mod.common.item.battle.DireHitItem
import com.cobblemon.mod.common.item.battle.GuardSpecItem
import com.cobblemon.mod.common.item.battle.XStatItem
import com.cobblemon.mod.common.item.berry.FriendshipRaisingBerryItem
import com.cobblemon.mod.common.item.berry.HealingBerryItem
import com.cobblemon.mod.common.item.berry.PPRestoringBerryItem
import com.cobblemon.mod.common.item.berry.PortionHealingBerryItem
import com.cobblemon.mod.common.item.berry.StatusCuringBerryItem
import com.cobblemon.mod.common.item.berry.VolatileCuringBerryItem
import com.cobblemon.mod.common.item.interactive.*
import com.cobblemon.mod.common.item.interactive.PotionItem
import com.cobblemon.mod.common.platform.PlatformRegistry
import com.cobblemon.mod.common.pokeball.PokeBall
import com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager
import com.cobblemon.mod.common.util.cobblemonResource
import net.minecraft.block.Block
import net.minecraft.block.ComposterBlock
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.World

@Suppress("unused")
object CobblemonItems : PlatformRegistry<Registry<Item>, RegistryKey<Registry<Item>>, Item>() {
    override val registry: Registry<Item> = Registries.ITEM
    override val registryKey: RegistryKey<Registry<Item>> = RegistryKeys.ITEM

    @JvmField
    val pokeBalls = mutableListOf<PokeBallItem>()
    @JvmField
    val POKE_BALL = pokeBallItem(PokeBalls.POKE_BALL)
    @JvmField
    val CITRINE_BALL = pokeBallItem(PokeBalls.CITRINE_BALL)
    @JvmField
    val VERDANT_BALL = pokeBallItem(PokeBalls.VERDANT_BALL)
    @JvmField
    val AZURE_BALL = pokeBallItem(PokeBalls.AZURE_BALL)
    @JvmField
    val ROSEATE_BALL = pokeBallItem(PokeBalls.ROSEATE_BALL)
    @JvmField
    val SLATE_BALL = pokeBallItem(PokeBalls.SLATE_BALL)
    @JvmField
    val PREMIER_BALL = pokeBallItem(PokeBalls.PREMIER_BALL)
    @JvmField
    val GREAT_BALL = pokeBallItem(PokeBalls.GREAT_BALL)
    @JvmField
    val ULTRA_BALL = pokeBallItem(PokeBalls.ULTRA_BALL)
    @JvmField
    val SAFARI_BALL = pokeBallItem(PokeBalls.SAFARI_BALL)
    @JvmField
    val FAST_BALL = pokeBallItem(PokeBalls.FAST_BALL)
    @JvmField
    val LEVEL_BALL = pokeBallItem(PokeBalls.LEVEL_BALL)
    @JvmField
    val LURE_BALL = pokeBallItem(PokeBalls.LURE_BALL)
    @JvmField
    val HEAVY_BALL = pokeBallItem(PokeBalls.HEAVY_BALL)
    @JvmField
    val LOVE_BALL = pokeBallItem(PokeBalls.LOVE_BALL)
    @JvmField
    val FRIEND_BALL = pokeBallItem(PokeBalls.FRIEND_BALL)
    @JvmField
    val MOON_BALL = pokeBallItem(PokeBalls.MOON_BALL)
    @JvmField
    val SPORT_BALL = pokeBallItem(PokeBalls.SPORT_BALL)
    @JvmField
    val PARK_BALL = pokeBallItem(PokeBalls.PARK_BALL)
    @JvmField
    val NET_BALL = pokeBallItem(PokeBalls.NET_BALL)
    @JvmField
    val DIVE_BALL = pokeBallItem(PokeBalls.DIVE_BALL)
    @JvmField
    val NEST_BALL = pokeBallItem(PokeBalls.NEST_BALL)
    @JvmField
    val REPEAT_BALL = pokeBallItem(PokeBalls.REPEAT_BALL)
    @JvmField
    val TIMER_BALL = pokeBallItem(PokeBalls.TIMER_BALL)
    @JvmField
    val LUXURY_BALL = pokeBallItem(PokeBalls.LUXURY_BALL)
    @JvmField
    val DUSK_BALL = pokeBallItem(PokeBalls.DUSK_BALL)
    @JvmField
    val HEAL_BALL = pokeBallItem(PokeBalls.HEAL_BALL)
    @JvmField
    val QUICK_BALL = pokeBallItem(PokeBalls.QUICK_BALL)
    @JvmField
    val DREAM_BALL = pokeBallItem(PokeBalls.DREAM_BALL)
    @JvmField
    val BEAST_BALL = pokeBallItem(PokeBalls.BEAST_BALL)
    @JvmField
    val MASTER_BALL = pokeBallItem(PokeBalls.MASTER_BALL)
    @JvmField
    val CHERISH_BALL = pokeBallItem(PokeBalls.CHERISH_BALL)

    @JvmField
    val VIVICHOKE = create("vivichoke", Item(Item.Settings()))

    @JvmField
    val VIVICHOKE_SEEDS = create("vivichoke_seeds", VivichokeItem(CobblemonBlocks.VIVICHOKE_SEEDS))

    @JvmField
    val RED_APRICORN = create("red_apricorn", ApricornItem(CobblemonBlocks.RED_APRICORN))
    @JvmField
    val YELLOW_APRICORN = create("yellow_apricorn", ApricornItem(CobblemonBlocks.YELLOW_APRICORN))
    @JvmField
    val GREEN_APRICORN = create("green_apricorn", ApricornItem(CobblemonBlocks.GREEN_APRICORN))
    @JvmField
    val BLUE_APRICORN = create("blue_apricorn", ApricornItem(CobblemonBlocks.BLUE_APRICORN))
    @JvmField
    val PINK_APRICORN = create("pink_apricorn", ApricornItem(CobblemonBlocks.PINK_APRICORN))
    @JvmField
    val BLACK_APRICORN = create("black_apricorn", ApricornItem(CobblemonBlocks.BLACK_APRICORN))
    @JvmField
    val WHITE_APRICORN = create("white_apricorn", ApricornItem(CobblemonBlocks.WHITE_APRICORN))

    @JvmField
    val RED_APRICORN_SEED = create("red_apricorn_seed", ApricornSeedItem(CobblemonBlocks.RED_APRICORN_SAPLING))
    @JvmField
    val YELLOW_APRICORN_SEED = create("yellow_apricorn_seed", ApricornSeedItem(CobblemonBlocks.YELLOW_APRICORN_SAPLING))
    @JvmField
    val GREEN_APRICORN_SEED = create("green_apricorn_seed", ApricornSeedItem(CobblemonBlocks.GREEN_APRICORN_SAPLING))
    @JvmField
    val BLUE_APRICORN_SEED = create("blue_apricorn_seed", ApricornSeedItem(CobblemonBlocks.BLUE_APRICORN_SAPLING))
    @JvmField
    val PINK_APRICORN_SEED = create("pink_apricorn_seed", ApricornSeedItem(CobblemonBlocks.PINK_APRICORN_SAPLING))
    @JvmField
    val BLACK_APRICORN_SEED = create("black_apricorn_seed", ApricornSeedItem(CobblemonBlocks.BLACK_APRICORN_SAPLING))
    @JvmField
    val WHITE_APRICORN_SEED = create("white_apricorn_seed", ApricornSeedItem(CobblemonBlocks.WHITE_APRICORN_SAPLING))

    @JvmField
    val APRICORN_LOG = blockItem("apricorn_log", CobblemonBlocks.APRICORN_LOG)
    @JvmField
    val STRIPPED_APRICORN_LOG = blockItem("stripped_apricorn_log", CobblemonBlocks.STRIPPED_APRICORN_LOG)
    @JvmField
    val APRICORN_WOOD = blockItem("apricorn_wood", CobblemonBlocks.APRICORN_WOOD)
    @JvmField
    val STRIPPED_APRICORN_WOOD = blockItem("stripped_apricorn_wood", CobblemonBlocks.STRIPPED_APRICORN_WOOD)
    @JvmField
    val APRICORN_PLANKS = blockItem("apricorn_planks", CobblemonBlocks.APRICORN_PLANKS)
    @JvmField
    val APRICORN_LEAVES = blockItem("apricorn_leaves", CobblemonBlocks.APRICORN_LEAVES).also {
        val compostChance = ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.getFloat(Items.OAK_LEAVES)
        // Should always pass unless Mojang reworks leaves to no longer work in the Composter, in that case we already updated w.o doing anything
        if (compostChance != ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.defaultReturnValue()) {
            ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE[it] = compostChance
        }
    }

    @JvmField
    val APRICORN_DOOR = blockItem("apricorn_door", CobblemonBlocks.APRICORN_DOOR)
    @JvmField
    val APRICORN_TRAPDOOR = blockItem("apricorn_trapdoor", CobblemonBlocks.APRICORN_TRAPDOOR)
    @JvmField
    val APRICORN_FENCE = blockItem("apricorn_fence", CobblemonBlocks.APRICORN_FENCE)
    @JvmField
    val APRICORN_FENCE_GATE = blockItem("apricorn_fence_gate", CobblemonBlocks.APRICORN_FENCE_GATE)
    @JvmField
    val APRICORN_BUTTON = blockItem("apricorn_button", CobblemonBlocks.APRICORN_BUTTON)
    @JvmField
    val APRICORN_PRESSURE_PLATE = blockItem("apricorn_pressure_plate", CobblemonBlocks.APRICORN_PRESSURE_PLATE)
    //@JvmField
//    val APRICORN_SIGN = create("apricorn_sign", SignItem(Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_DECORATIONS), CobblemonBlocks.APRICORN_SIGN, CobblemonBlocks.APRICORN_WALL_SIGN))
    @JvmField
    val APRICORN_SLAB = blockItem("apricorn_slab", CobblemonBlocks.APRICORN_SLAB)
    @JvmField
    val APRICORN_STAIRS = blockItem("apricorn_stairs", CobblemonBlocks.APRICORN_STAIRS)

    @JvmField
    val HEALING_MACHINE = blockItem("healing_machine", CobblemonBlocks.HEALING_MACHINE)
    @JvmField
    val PC = blockItem("pc", CobblemonBlocks.PC)
    @JvmField
    val PASTURE = blockItem("pasture", CobblemonBlocks.PASTURE)

    // Evolution items
    @JvmField val LINK_CABLE = create("link_cable", LinkCableItem())
    @JvmField val DRAGON_SCALE = noSettingsItem("dragon_scale")
    @JvmField val KINGS_ROCK = noSettingsItem("kings_rock")
    @JvmField val METAL_COAT = noSettingsItem("metal_coat")
    @JvmField val UPGRADE = noSettingsItem("upgrade")
    @JvmField val DUBIOUS_DISC = noSettingsItem("dubious_disc")
    @JvmField val DEEP_SEA_SCALE = noSettingsItem("deep_sea_scale")
    @JvmField val DEEP_SEA_TOOTH = noSettingsItem("deep_sea_tooth")
    @JvmField val ELECTIRIZER = noSettingsItem("electirizer")
    @JvmField val MAGMARIZER = noSettingsItem("magmarizer")
    @JvmField val OVAL_STONE = noSettingsItem("oval_stone")
    @JvmField val PROTECTOR = noSettingsItem("protector")
    @JvmField val REAPER_CLOTH = noSettingsItem("reaper_cloth")
    @JvmField val PRISM_SCALE = noSettingsItem("prism_scale")
    @JvmField val SACHET = noSettingsItem("sachet")
    @JvmField val WHIPPED_DREAM = noSettingsItem("whipped_dream")
    @JvmField val STRAWBERRY_SWEET = noSettingsItem("strawberry_sweet")
    @JvmField val LOVE_SWEET = noSettingsItem("love_sweet")
    @JvmField val BERRY_SWEET = noSettingsItem("berry_sweet")
    @JvmField val CLOVER_SWEET = noSettingsItem("clover_sweet")
    @JvmField val FLOWER_SWEET = noSettingsItem("flower_sweet")
    @JvmField val STAR_SWEET = noSettingsItem("star_sweet")
    @JvmField val RIBBON_SWEET = noSettingsItem("ribbon_sweet")
    @JvmField val CHIPPED_POT = noSettingsItem("chipped_pot")
    @JvmField val CRACKED_POT = noSettingsItem("cracked_pot")
    @JvmField val SWEET_APPLE = noSettingsItem("sweet_apple")
    @JvmField val TART_APPLE = noSettingsItem("tart_apple")
    @JvmField val GALARICA_CUFF = noSettingsItem("galarica_cuff")
    @JvmField val GALARICA_WREATH = noSettingsItem("galarica_wreath")
    @JvmField val BLACK_AUGURITE = noSettingsItem("black_augurite")
    @JvmField val PEAT_BLOCK = noSettingsItem("peat_block")
    @JvmField val RAZOR_CLAW = noSettingsItem("razor_claw")
    @JvmField val RAZOR_FANG = noSettingsItem("razor_fang")
    @JvmField val AUSPICIOUS_ARMOR = heldItem("auspicious_armor")
    @JvmField val MALICIOUS_ARMOR = heldItem("malicious_armor")

    private val berries = mutableMapOf<Identifier, BerryItem>()
    // Plants
    @JvmField val ORAN_BERRY = berryItem("oran", HealingBerryItem(CobblemonBlocks.ORAN_BERRY) { CobblemonMechanics.berries.oranRestoreAmount })
    @JvmField val CHERI_BERRY = berryItem("cheri", StatusCuringBerryItem(CobblemonBlocks.CHERI_BERRY, Statuses.PARALYSIS))
    @JvmField val CHESTO_BERRY = berryItem("chesto", StatusCuringBerryItem(CobblemonBlocks.CHESTO_BERRY, Statuses.SLEEP))
    @JvmField val PECHA_BERRY = berryItem("pecha", StatusCuringBerryItem(CobblemonBlocks.PECHA_BERRY, Statuses.POISON, Statuses.POISON_BADLY))
    @JvmField val RAWST_BERRY = berryItem("rawst", StatusCuringBerryItem(CobblemonBlocks.RAWST_BERRY, Statuses.BURN))
    @JvmField val ASPEAR_BERRY = berryItem("aspear", StatusCuringBerryItem(CobblemonBlocks.ASPEAR_BERRY, Statuses.FROZEN))
    @JvmField val PERSIM_BERRY = berryItem("persim", VolatileCuringBerryItem(CobblemonBlocks.PERSIM_BERRY, "confusion"))
    @JvmField val RAZZ_BERRY = berryItem("razz", CobblemonBlocks.RAZZ_BERRY)
    @JvmField val BLUK_BERRY = berryItem("bluk", CobblemonBlocks.BLUK_BERRY)
    @JvmField val NANAB_BERRY = berryItem("nanab", CobblemonBlocks.NANAB_BERRY)
    @JvmField val WEPEAR_BERRY = berryItem("wepear", CobblemonBlocks.WEPEAR_BERRY)
    @JvmField val PINAP_BERRY = berryItem("pinap", CobblemonBlocks.PINAP_BERRY)
    @JvmField val OCCA_BERRY = berryItem("occa", CobblemonBlocks.OCCA_BERRY)
    @JvmField val PASSHO_BERRY = berryItem("passho", CobblemonBlocks.PASSHO_BERRY)
    @JvmField val WACAN_BERRY = berryItem("wacan", CobblemonBlocks.WACAN_BERRY)
    @JvmField val RINDO_BERRY = berryItem("rindo", CobblemonBlocks.RINDO_BERRY)
    @JvmField val YACHE_BERRY = berryItem("yache", CobblemonBlocks.YACHE_BERRY)
    @JvmField val CHOPLE_BERRY = berryItem("chople", CobblemonBlocks.CHOPLE_BERRY)
    @JvmField val KEBIA_BERRY = berryItem("kebia", CobblemonBlocks.KEBIA_BERRY)
    @JvmField val SHUCA_BERRY = berryItem("shuca", CobblemonBlocks.SHUCA_BERRY)
    @JvmField val COBA_BERRY = berryItem("coba", CobblemonBlocks.COBA_BERRY)
    @JvmField val PAYAPA_BERRY = berryItem("payapa", CobblemonBlocks.PAYAPA_BERRY)
    @JvmField val TANGA_BERRY = berryItem("tanga", CobblemonBlocks.TANGA_BERRY)
    @JvmField val CHARTI_BERRY = berryItem("charti", CobblemonBlocks.CHARTI_BERRY)
    @JvmField val KASIB_BERRY = berryItem("kasib", CobblemonBlocks.KASIB_BERRY)
    @JvmField val HABAN_BERRY = berryItem("haban", CobblemonBlocks.HABAN_BERRY)
    @JvmField val COLBUR_BERRY = berryItem("colbur", CobblemonBlocks.COLBUR_BERRY)
    @JvmField val BABIRI_BERRY = berryItem("babiri", CobblemonBlocks.BABIRI_BERRY)
    @JvmField val CHILAN_BERRY = berryItem("chilan", CobblemonBlocks.CHILAN_BERRY)
    @JvmField val ROSELI_BERRY = berryItem("roseli", CobblemonBlocks.ROSELI_BERRY)
    @JvmField val LEPPA_BERRY = berryItem("leppa", PPRestoringBerryItem(CobblemonBlocks.LEPPA_BERRY) { CobblemonMechanics.berries.ppRestoreAmount })
    @JvmField val LUM_BERRY = berryItem("lum", StatusCuringBerryItem(CobblemonBlocks.LUM_BERRY))
    @JvmField val FIGY_BERRY = berryItem("figy", PortionHealingBerryItem(CobblemonBlocks.FIGY_BERRY, true) { CobblemonMechanics.berries.portionHealRatio })
    @JvmField val WIKI_BERRY = berryItem("wiki", PortionHealingBerryItem(CobblemonBlocks.WIKI_BERRY, true) { CobblemonMechanics.berries.portionHealRatio })
    @JvmField val MAGO_BERRY = berryItem("mago", PortionHealingBerryItem(CobblemonBlocks.MAGO_BERRY, true) { CobblemonMechanics.berries.portionHealRatio })
    @JvmField val AGUAV_BERRY = berryItem("aguav", PortionHealingBerryItem(CobblemonBlocks.AGUAV_BERRY, true) { CobblemonMechanics.berries.portionHealRatio })
    @JvmField val IAPAPA_BERRY = berryItem("iapapa", PortionHealingBerryItem(CobblemonBlocks.IAPAPA_BERRY, true) { CobblemonMechanics.berries.portionHealRatio })
    @JvmField val SITRUS_BERRY = berryItem("sitrus", HealingBerryItem(CobblemonBlocks.SITRUS_BERRY) { CobblemonMechanics.berries.sitrusHealAmount })
    @JvmField val TOUGA_BERRY = berryItem("touga", CobblemonBlocks.TOUGA_BERRY)
    @JvmField val CORNN_BERRY = berryItem("cornn", CobblemonBlocks.CORNN_BERRY)
    @JvmField val MAGOST_BERRY = berryItem("magost", CobblemonBlocks.MAGOST_BERRY)
    @JvmField val RABUTA_BERRY = berryItem("rabuta", CobblemonBlocks.RABUTA_BERRY)
    @JvmField val NOMEL_BERRY = berryItem("nomel", CobblemonBlocks.NOMEL_BERRY)
    @JvmField val ENIGMA_BERRY = berryItem("enigma", CobblemonBlocks.ENIGMA_BERRY)
    @JvmField val POMEG_BERRY = berryItem("pomeg", FriendshipRaisingBerryItem(CobblemonBlocks.POMEG_BERRY, Stats.HP))
    @JvmField val KELPSY_BERRY = berryItem("kelpsy", FriendshipRaisingBerryItem(CobblemonBlocks.KELPSY_BERRY, Stats.ATTACK))
    @JvmField val QUALOT_BERRY = berryItem("qualot", FriendshipRaisingBerryItem(CobblemonBlocks.QUALOT_BERRY, Stats.DEFENCE))
    @JvmField val HONDEW_BERRY = berryItem("hondew", FriendshipRaisingBerryItem(CobblemonBlocks.HONDEW_BERRY, Stats.SPECIAL_ATTACK))
    @JvmField val GREPA_BERRY = berryItem("grepa", FriendshipRaisingBerryItem(CobblemonBlocks.GREPA_BERRY, Stats.SPECIAL_DEFENCE))
    @JvmField val TAMATO_BERRY = berryItem("tamato", FriendshipRaisingBerryItem(CobblemonBlocks.TAMATO_BERRY, Stats.SPEED))
    @JvmField val SPELON_BERRY = berryItem("spelon", CobblemonBlocks.SPELON_BERRY)
    @JvmField val PAMTRE_BERRY = berryItem("pamtre", CobblemonBlocks.PAMTRE_BERRY)
    @JvmField val WATMEL_BERRY = berryItem("watmel", CobblemonBlocks.WATMEL_BERRY)
    @JvmField val DURIN_BERRY = berryItem("durin", CobblemonBlocks.DURIN_BERRY)
    @JvmField val BELUE_BERRY = berryItem("belue", CobblemonBlocks.BELUE_BERRY)
    @JvmField val KEE_BERRY = berryItem("kee", CobblemonBlocks.KEE_BERRY)
    @JvmField val MARANGA_BERRY = berryItem("maranga", CobblemonBlocks.MARANGA_BERRY)
    @JvmField val HOPO_BERRY = berryItem("hopo", PPRestoringBerryItem(CobblemonBlocks.HOPO_BERRY) { CobblemonMechanics.berries.ppRestoreAmount })
    @JvmField val LIECHI_BERRY = berryItem("liechi", CobblemonBlocks.LIECHI_BERRY)
    @JvmField val GANLON_BERRY = berryItem("ganlon", CobblemonBlocks.GANLON_BERRY)
    @JvmField val SALAC_BERRY = berryItem("salac", CobblemonBlocks.SALAC_BERRY)
    @JvmField val PETAYA_BERRY = berryItem("petaya", CobblemonBlocks.PETAYA_BERRY)
    @JvmField val APICOT_BERRY = berryItem("apicot", CobblemonBlocks.APICOT_BERRY)
    @JvmField val LANSAT_BERRY = berryItem("lansat", CobblemonBlocks.LANSAT_BERRY)
    @JvmField val STARF_BERRY = berryItem("starf", CobblemonBlocks.STARF_BERRY)
    @JvmField val MICLE_BERRY = berryItem("micle", CobblemonBlocks.MICLE_BERRY)
    @JvmField val CUSTAP_BERRY = berryItem("custap", CobblemonBlocks.CUSTAP_BERRY)
    @JvmField val JABOCA_BERRY = berryItem("jaboca", CobblemonBlocks.JABOCA_BERRY)
    @JvmField val ROWAP_BERRY = berryItem("rowap", CobblemonBlocks.ROWAP_BERRY)

    @JvmField val BERRY_JUICE = this.create("berry_juice", BerryJuiceItem())

    // Medicine
    @JvmField
    val RARE_CANDY = candyItem("rare_candy") { _, pokemon -> pokemon.getExperienceToNextLevel() }
    @JvmField
    val EXPERIENCE_CANDY_XS = candyItem("exp_candy_xs") { _, _ -> CandyItem.DEFAULT_XS_CANDY_YIELD }
    @JvmField
    val EXPERIENCE_CANDY_S = candyItem("exp_candy_s") { _, _ -> CandyItem.DEFAULT_S_CANDY_YIELD }
    @JvmField
    val EXPERIENCE_CANDY_M = candyItem("exp_candy_m") { _, _ -> CandyItem.DEFAULT_M_CANDY_YIELD }
    @JvmField
    val EXPERIENCE_CANDY_L = candyItem("exp_candy_l") { _, _ -> CandyItem.DEFAULT_L_CANDY_YIELD }
    @JvmField
    val EXPERIENCE_CANDY_XL = candyItem("exp_candy_xl") { _, _ -> CandyItem.DEFAULT_XL_CANDY_YIELD }
    @JvmField
    val CALCIUM = create("calcium", VitaminItem(Stats.SPECIAL_ATTACK))
    @JvmField
    val CARBOS = create("carbos", VitaminItem(Stats.SPEED))
    @JvmField
    val HP_UP = create("hp_up", VitaminItem(Stats.HP))
    @JvmField
    val IRON = create("iron", VitaminItem(Stats.DEFENCE))
    @JvmField
    val PROTEIN = create("protein", VitaminItem(Stats.ATTACK))
    @JvmField
    val ZINC = create("zinc", VitaminItem(Stats.SPECIAL_DEFENCE))
    @JvmField
    val MEDICINAL_LEEK = create("medicinal_leek", MedicinalLeekItem(CobblemonBlocks.MEDICINAL_LEEK, Item.Settings().food(FoodComponent.Builder().snack().hunger(1).saturationModifier(0.2f).build())))
    @JvmField
    val ROASTED_LEEK = create("roasted_leek", Item(Item.Settings().food(FoodComponent.Builder().snack().hunger(3).saturationModifier(0.3f).build())))
    @JvmField
    val BRAISED_VIVICHOKE = create("braised_vivichoke", Item(Item.Settings().food(FoodComponent.Builder().hunger(6).saturationModifier(0.6f).build())))
    @JvmField
    val VIVICHOKE_DIP = create("vivichoke_dip", object : StewItem(Settings().maxCount(1)
        .food(FoodComponent.Builder()
            .hunger(10)
            .saturationModifier(1.2F)
            .statusEffect(StatusEffectInstance(StatusEffects.ABSORPTION, 900, 0), 1F)
            .alwaysEdible()
            .build())) {
        override fun finishUsing(stack: ItemStack?, world: World?, user: LivingEntity?): ItemStack {
            user?.clearStatusEffects()
            return super.finishUsing(stack, world, user)
        }
    })
    @JvmField
    val ENERGY_ROOT = create("energy_root", EnergyRootItem(CobblemonBlocks.ENERGY_ROOT, Item.Settings().food(FoodComponent.Builder().hunger(1).snack().saturationModifier(0.2f).build())))
    @JvmField
    val REVIVAL_HERB = create("revival_herb", RevivalHerbItem(CobblemonBlocks.REVIVAL_HERB))
    @JvmField
    val PEP_UP_FLOWER = blockItem("pep_up_flower", CobblemonBlocks.PEP_UP_FLOWER)
    @JvmField
    val MEDICINAL_BREW = create("medicinal_brew", Item(Item.Settings()))
    @JvmField
    val REMEDY = create("remedy", RemedyItem(RemedyItem.NORMAL))
    @JvmField
    val FINE_REMEDY = create("fine_remedy", RemedyItem(RemedyItem.FINE))
    @JvmField
    val SUPERB_REMEDY = create("superb_remedy", RemedyItem(RemedyItem.SUPERB))

    @JvmField
    val POTION = create("potion", PotionItem(PotionType.POTION))
    @JvmField
    val SUPER_POTION = create("super_potion", PotionItem(PotionType.SUPER_POTION))
    @JvmField
    val HYPER_POTION = create("hyper_potion", PotionItem(PotionType.HYPER_POTION))
    @JvmField
    val MAX_POTION = create("max_potion", PotionItem(PotionType.MAX_POTION))
    @JvmField
    val FULL_RESTORE = create("full_restore", PotionItem(PotionType.FULL_RESTORE))

    @JvmField
    val HEAL_POWDER = create("heal_powder", HealPowderItem())
    @JvmField
    val LEEK_AND_POTATO_STEW = create("leek_and_potato_stew", StewItem(Item.Settings().food(FoodComponent.Builder().hunger(8).saturationModifier(0.6f).build()).maxCount(1)))
    @JvmField
    val REVIVE = create("revive", ReviveItem(max = false))
    @JvmField
    val MAX_REVIVE = create("max_revive", ReviveItem(max = true))
    @JvmField
    val PP_UP = create("pp_up", PPUpItem(1))
    @JvmField
    val PP_MAX = create("pp_max", PPUpItem(3))

    @JvmField
    val RED_MINT_SEEDS = blockItem("red_mint_seeds", MintType.RED.getCropBlock())
    @JvmField
    val RED_MINT_LEAF = create("red_mint_leaf", MintLeafItem(MintType.RED))
    @JvmField
    val BLUE_MINT_SEEDS = blockItem("blue_mint_seeds", MintType.BLUE.getCropBlock())
    @JvmField
    val BLUE_MINT_LEAF = create("blue_mint_leaf", MintLeafItem(MintType.BLUE))
    @JvmField
    val CYAN_MINT_SEEDS = blockItem("cyan_mint_seeds", MintType.CYAN.getCropBlock())
    @JvmField
    val CYAN_MINT_LEAF = create("cyan_mint_leaf", MintLeafItem(MintType.CYAN))
    @JvmField
    val PINK_MINT_SEEDS = blockItem("pink_mint_seeds", MintType.PINK.getCropBlock())
    @JvmField
    val PINK_MINT_LEAF = create("pink_mint_leaf", MintLeafItem(MintType.PINK))
    @JvmField
    val GREEN_MINT_SEEDS = blockItem("green_mint_seeds", MintType.GREEN.getCropBlock())
    @JvmField
    val GREEN_MINT_LEAF = create("green_mint_leaf", MintLeafItem(MintType.GREEN))
    @JvmField
    val WHITE_MINT_SEEDS = blockItem("white_mint_seeds", MintType.WHITE.getCropBlock())
    @JvmField
    val WHITE_MINT_LEAF = create("white_mint_leaf", MintLeafItem(MintType.WHITE))

    @JvmField
    val LONELY_MINT = create("lonely_mint", MintItem(Natures.LONELY))
    @JvmField
    val ADAMANT_MINT = create("adamant_mint", MintItem(Natures.ADAMANT))
    @JvmField
    val NAUGHTY_MINT = create("naughty_mint", MintItem(Natures.NAUGHTY))
    @JvmField
    val BRAVE_MINT = create("brave_mint", MintItem(Natures.BRAVE))
    @JvmField
    val BOLD_MINT = create("bold_mint", MintItem(Natures.BOLD))
    @JvmField
    val IMPISH_MINT = create("impish_mint", MintItem(Natures.IMPISH))
    @JvmField
    val LAX_MINT = create("lax_mint", MintItem(Natures.LAX))
    @JvmField
    val RELAXED_MINT = create("relaxed_mint", MintItem(Natures.RELAXED))
    @JvmField
    val MODEST_MINT = create("modest_mint", MintItem(Natures.MODEST))
    @JvmField
    val MILD_MINT = create("mild_mint", MintItem(Natures.MILD))
    @JvmField
    val RASH_MINT = create("rash_mint", MintItem(Natures.RASH))
    @JvmField
    val QUIET_MINT = create("quiet_mint", MintItem(Natures.QUIET))
    @JvmField
    val CALM_MINT = create("calm_mint", MintItem(Natures.CALM))
    @JvmField
    val GENTLE_MINT = create("gentle_mint", MintItem(Natures.GENTLE))
    @JvmField
    val CAREFUL_MINT = create("careful_mint", MintItem(Natures.CAREFUL))
    @JvmField
    val SASSY_MINT = create("sassy_mint", MintItem(Natures.SASSY))
    @JvmField
    val TIMID_MINT = create("timid_mint", MintItem(Natures.TIMID))
    @JvmField
    val HASTY_MINT = create("hasty_mint", MintItem(Natures.HASTY))
    @JvmField
    val JOLLY_MINT = create("jolly_mint", MintItem(Natures.JOLLY))
    @JvmField
    val NAIVE_MINT = create("naive_mint", MintItem(Natures.NAIVE))
    @JvmField
    val SERIOUS_MINT = create("serious_mint", MintItem(Natures.SERIOUS))

    @JvmField val X_ACCURACY = create("x_${Stats.ACCURACY.identifier.path}", XStatItem(Stats.ACCURACY))
    @JvmField val X_ATTACK = create("x_${Stats.ATTACK.identifier.path}", XStatItem(Stats.ATTACK))
    @JvmField val X_DEFENSE = create("x_${Stats.DEFENCE.identifier.path}", XStatItem(Stats.DEFENCE))
    @JvmField val X_SP_ATK = create("x_${Stats.SPECIAL_ATTACK.identifier.path}", XStatItem(Stats.SPECIAL_ATTACK))
    @JvmField val X_SP_DEF = create("x_${Stats.SPECIAL_DEFENCE.identifier.path}", XStatItem(Stats.SPECIAL_DEFENCE))
    @JvmField val X_SPEED = create("x_${Stats.SPEED.identifier.path}", XStatItem(Stats.SPEED))

    @JvmField val DIRE_HIT = create("dire_hit", DireHitItem())
    @JvmField val GUARD_SPEC = create("guard_spec", GuardSpecItem())

    @JvmField val BURN_HEAL = create("burn_heal", StatusCureItem("item.cobblemon.burn_heal", Statuses.BURN))
    @JvmField val PARALYZE_HEAL = create("paralyze_heal", StatusCureItem("item.cobblemon.paralyze_heal", Statuses.PARALYSIS))
    @JvmField val ICE_HEAL = create("ice_heal", StatusCureItem("item.cobblemon.ice_heal", Statuses.FROZEN))
    @JvmField val ANTIDOTE = create("antidote", StatusCureItem("item.cobblemon.antidote", Statuses.POISON, Statuses.POISON_BADLY))
    @JvmField val AWAKENING = create("awakening", StatusCureItem("item.cobblemon.awakening", Statuses.SLEEP))

    @JvmField val FULL_HEAL = create("full_heal", StatusCureItem("item.cobblemon.full_heal"))

    @JvmField val ETHER = create("ether", EtherItem(max = false))
    @JvmField val MAX_ETHER = create("max_ether", EtherItem(max = true))
    @JvmField val ELIXIR = create("elixir", ElixirItem(max = false))
    @JvmField val MAX_ELIXIR = create("max_elixir", ElixirItem(max = true))

    /**
     * Evolution Ores and Stones
     */
    @JvmField
    val DAWN_STONE_ORE = blockItem("dawn_stone_ore", CobblemonBlocks.DAWN_STONE_ORE)
    @JvmField
    val DUSK_STONE_ORE = blockItem("dusk_stone_ore", CobblemonBlocks.DUSK_STONE_ORE)
    @JvmField
    val FIRE_STONE_ORE = blockItem("fire_stone_ore", CobblemonBlocks.FIRE_STONE_ORE)
    @JvmField
    val ICE_STONE_ORE = blockItem("ice_stone_ore", CobblemonBlocks.ICE_STONE_ORE)
    @JvmField
    val LEAF_STONE_ORE = blockItem("leaf_stone_ore", CobblemonBlocks.LEAF_STONE_ORE)
    @JvmField
    val MOON_STONE_ORE = blockItem("moon_stone_ore", CobblemonBlocks.MOON_STONE_ORE)
    @JvmField
    val SHINY_STONE_ORE = blockItem("shiny_stone_ore", CobblemonBlocks.SHINY_STONE_ORE)
    @JvmField
    val SUN_STONE_ORE = blockItem("sun_stone_ore", CobblemonBlocks.SUN_STONE_ORE)
    @JvmField
    val THUNDER_STONE_ORE = blockItem("thunder_stone_ore", CobblemonBlocks.THUNDER_STONE_ORE)
    @JvmField
    val WATER_STONE_ORE = blockItem("water_stone_ore", CobblemonBlocks.WATER_STONE_ORE)
    @JvmField
    val DEEPSLATE_DAWN_STONE_ORE = blockItem("deepslate_dawn_stone_ore", CobblemonBlocks.DEEPSLATE_DAWN_STONE_ORE)
    @JvmField
    val DEEPSLATE_DUSK_STONE_ORE = blockItem("deepslate_dusk_stone_ore", CobblemonBlocks.DEEPSLATE_DUSK_STONE_ORE)
    @JvmField
    val DEEPSLATE_FIRE_STONE_ORE = blockItem("deepslate_fire_stone_ore", CobblemonBlocks.DEEPSLATE_FIRE_STONE_ORE)
    @JvmField
    val DEEPSLATE_ICE_STONE_ORE = blockItem("deepslate_ice_stone_ore", CobblemonBlocks.DEEPSLATE_ICE_STONE_ORE)
    @JvmField
    val DEEPSLATE_LEAF_STONE_ORE = blockItem("deepslate_leaf_stone_ore", CobblemonBlocks.DEEPSLATE_LEAF_STONE_ORE)
    @JvmField
    val DEEPSLATE_MOON_STONE_ORE = blockItem("deepslate_moon_stone_ore", CobblemonBlocks.DEEPSLATE_MOON_STONE_ORE)
    @JvmField
    val DEEPSLATE_SHINY_STONE_ORE = blockItem("deepslate_shiny_stone_ore", CobblemonBlocks.DEEPSLATE_SHINY_STONE_ORE)
    @JvmField
    val DEEPSLATE_SUN_STONE_ORE = blockItem("deepslate_sun_stone_ore", CobblemonBlocks.DEEPSLATE_SUN_STONE_ORE)
    @JvmField
    val DEEPSLATE_THUNDER_STONE_ORE = blockItem("deepslate_thunder_stone_ore", CobblemonBlocks.DEEPSLATE_THUNDER_STONE_ORE)
    @JvmField
    val DEEPSLATE_WATER_STONE_ORE = blockItem("deepslate_water_stone_ore", CobblemonBlocks.DEEPSLATE_WATER_STONE_ORE)
    @JvmField
    val DRIPSTONE_MOON_STONE_ORE = blockItem("dripstone_moon_stone_ore", CobblemonBlocks.DRIPSTONE_MOON_STONE_ORE)
    @JvmField
    val DAWN_STONE = noSettingsItem("dawn_stone")
    @JvmField
    val DUSK_STONE = noSettingsItem("dusk_stone")
    @JvmField
    val FIRE_STONE = noSettingsItem("fire_stone")
    @JvmField
    val ICE_STONE = noSettingsItem("ice_stone")
    @JvmField
    val LEAF_STONE = noSettingsItem("leaf_stone")
    @JvmField
    val MOON_STONE = noSettingsItem("moon_stone")
    @JvmField
    val SHINY_STONE = noSettingsItem("shiny_stone")
    @JvmField
    val SUN_STONE = noSettingsItem("sun_stone")
    @JvmField
    val THUNDER_STONE = noSettingsItem("thunder_stone")
    @JvmField
    val WATER_STONE = noSettingsItem("water_stone")

    // Held Items
    @JvmField
    val ASSAULT_VEST = heldItem("assault_vest")
    @JvmField
    val BIG_ROOT = blockItem("big_root", CobblemonBlocks.BIG_ROOT)
    @JvmField
    val BLACK_BELT = heldItem("black_belt")
    @JvmField
    val BLACK_GLASSES = heldItem("black_glasses")
    @JvmField
    val BLACK_SLUDGE = heldItem("black_sludge")
    @JvmField
    val CHARCOAL = heldItem("charcoal_stick", remappedName = "charcoal")
    @JvmField
    val CHOICE_BAND = heldItem("choice_band")
    @JvmField
    val CHOICE_SCARF = heldItem("choice_scarf")
    @JvmField
    val CHOICE_SPECS = heldItem("choice_specs")
    @JvmField
    val DRAGON_FANG = heldItem("dragon_fang")
    @JvmField
    val EXP_SHARE = heldItem("exp_share")
    @JvmField
    val FOCUS_BAND = heldItem("focus_band")
    @JvmField
    val HARD_STONE = heldItem("hard_stone")
    @JvmField
    val HEAVY_DUTY_BOOTS = heldItem("heavy_duty_boots")
    @JvmField
    val LEFTOVERS = heldItem("leftovers")
    @JvmField
    val LIGHT_CLAY = heldItem("light_clay")
    @JvmField
    val LUCKY_EGG = heldItem("lucky_egg")
    @JvmField
    val MAGNET = heldItem("magnet")
    @JvmField
    val MIRACLE_SEED = heldItem("miracle_seed")
    @JvmField
    val MUSCLE_BAND = heldItem("muscle_band")
    @JvmField
    val MYSTIC_WATER = heldItem("mystic_water")
    @JvmField
    val NEVER_MELT_ICE = heldItem("never_melt_ice")
    @JvmField
    val POISON_BARB = heldItem("poison_barb")
    @JvmField
    val QUICK_CLAW = heldItem("quick_claw")
    @JvmField
    val ROCKY_HELMET = heldItem("rocky_helmet")
    @JvmField
    val SAFETY_GOGGLES = heldItem("safety_goggles")
    @JvmField
    val SHARP_BEAK = heldItem("sharp_beak")
    @JvmField
    val SILK_SCARF = heldItem("silk_scarf")
    @JvmField
    val SILVER_POWDER = heldItem("silver_powder")
    @JvmField
    val SOFT_SAND = heldItem("soft_sand")
    @JvmField
    val SPELL_TAG = heldItem("spell_tag")
    @JvmField
    val TWISTED_SPOON = heldItem("twisted_spoon")
    @JvmField
    val WISE_GLASSES = heldItem("wise_glasses")
    @JvmField
    val MENTAL_HERB = heldItem("mental_herb")
    @JvmField
    val MIRROR_HERB = heldItem("mirror_herb")
    @JvmField
    val POWER_HERB = heldItem("power_herb")
    @JvmField
    val WHITE_HERB = heldItem("white_herb")
    @JvmField
    val BRIGHT_POWDER = heldItem("bright_powder")
    @JvmField
    val METAL_POWDER = heldItem("metal_powder")
    @JvmField
    val QUICK_POWDER = heldItem("quick_powder")
    @JvmField
    val DESTINY_KNOT = heldItem("destiny_knot")
    @JvmField
    val EVERSTONE = heldItem("everstone")
    @JvmField
    val POWER_ANKLET = heldItem("power_anklet")
    @JvmField
    val POWER_BAND = heldItem("power_band")
    @JvmField
    val POWER_BELT = heldItem("power_belt")
    @JvmField
    val POWER_BRACER = heldItem("power_bracer")
    @JvmField
    val POWER_LENS = heldItem("power_lens")
    @JvmField
    val POWER_WEIGHT = heldItem("power_weight")

    // Mulch
    @JvmField
    val MULCH_BASE = noSettingsItem("mulch_base")
    @JvmField
    val COARSE_MULCH = mulchItem("coarse_mulch", MulchVariant.COARSE)
    @JvmField
    val GROWTH_MULCH = mulchItem("growth_mulch", MulchVariant.GROWTH)
    @JvmField
    val HUMID_MULCH = mulchItem("humid_mulch", MulchVariant.HUMID)
    @JvmField
    val LOAMY_MULCH = mulchItem("loamy_mulch", MulchVariant.LOAMY)
    @JvmField
    val PEAT_MULCH = mulchItem("peat_mulch", MulchVariant.PEAT)
    @JvmField
    val RICH_MULCH = mulchItem("rich_mulch", MulchVariant.RICH)
    @JvmField
    val SANDY_MULCH = mulchItem("sandy_mulch", MulchVariant.SANDY)
    @JvmField
    val SURPRISE_MULCH = mulchItem("surprise_mulch", MulchVariant.SURPRISE)

    // Misc
    @JvmField
    val POKEMON_MODEL = this.create("pokemon_model", PokemonItem())

    private fun blockItem(name: String, block: Block): BlockItem = this.create(name, BlockItem(block, Item.Settings()))

    private fun noSettingsItem(name: String): CobblemonItem = this.create(name, CobblemonItem(Item.Settings()))

    fun berries() = this.berries.toMap()

    private fun mulchItem(name: String, mulchVariant: MulchVariant): MulchItem = this.create(name, MulchItem(mulchVariant))

    private fun pokeBallItem(pokeBall: PokeBall): PokeBallItem {
        val item = create(pokeBall.name.path, PokeBallItem(pokeBall))
        pokeBall.item = item
        pokeBalls.add(item)
        return item
    }

    private fun candyItem(name: String, calculator: CandyItem.Calculator): CandyItem  = this.create(name, CandyItem(calculator))

    private fun heldItem(name: String, remappedName: String? = null): CobblemonItem = create(
        name,
        CobblemonItem(Item.Settings()).also {
            if (remappedName != null) {
                CobblemonHeldItemManager.registerRemap(it, remappedName)
            }
        }
    )

    private fun berryItem(name: String, berryBlock: BerryBlock): BerryItem {
        val finalName = "${name}_berry"
        val item = this.create(finalName, BerryItem(berryBlock))
        this.berries[cobblemonResource(finalName)] = item
        return item
    }

    private fun berryItem(name: String, berryItem: BerryItem): BerryItem {
        val finalName = "${name}_berry"
        val item = this.create(finalName, berryItem)
        this.berries[cobblemonResource(finalName)] = item
        return item
    }
}
