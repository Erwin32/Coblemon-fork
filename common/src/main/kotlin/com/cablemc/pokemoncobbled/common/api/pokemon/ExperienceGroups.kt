package com.cablemc.pokemoncobbled.common.api.pokemon

import com.cablemc.pokemoncobbled.common.api.CachedLevelThresholds
import com.cablemc.pokemoncobbled.common.api.LevelCurve
import com.cablemc.pokemoncobbled.common.util.lang
import com.cablemc.pokemoncobbled.common.util.math.pow
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import net.minecraft.network.chat.TranslatableComponent
import java.lang.Math.cbrt
import java.lang.reflect.Type
import kotlin.math.max

/**
 * A collection of all the [ExperienceGroup]s a Pokémon species can have.
 *
 * This can be edited but you should really only edit this early in the lifecycle.
 *
 * @author Hiroku
 * @since March 21st, 2022
 */
object ExperienceGroups : Iterable<ExperienceGroup> {
    private val groups = mutableListOf<ExperienceGroup>()

    fun findByName(name: String) = find { it.name.equals(name, ignoreCase = true) }
    fun register(experienceGroup: ExperienceGroup): ExperienceGroup = experienceGroup.also { groups.add(it) }
    fun unregister(experienceGroup: ExperienceGroup) = groups.remove(experienceGroup)

    fun registerDefaults() {
        register(Erratic)
        register(Fast)
        register(MediumFast)
        register(MediumSlow)
        register(Slow)
        register(Fluctuating)
    }

    override fun iterator() = groups.iterator()
}

object ExperienceGroupAdapter : JsonSerializer<ExperienceGroup>, JsonDeserializer<ExperienceGroup> {
    override fun serialize(experienceGroup: ExperienceGroup, type: Type, ctx: JsonSerializationContext) = JsonPrimitive(experienceGroup.name)
    override fun deserialize(json: JsonElement, type: Type, ctx: JsonDeserializationContext): ExperienceGroup {
        return ExperienceGroups.findByName(json.asString) ?: ExperienceGroup.dummy(json.asString)
    }
}

/**
 * A Pokémon's experience group, as an implementation of [LevelCurve]. Complicated experience groups
 * should extend [CachedExperienceGroup] to save you from having to invert the level-to-experience
 * equation.
 *
 * @author Hiroku
 * @since March 21st, 2022
 */
interface ExperienceGroup : LevelCurve {
    val name: String
    val translatedName: TranslatableComponent
        get() = lang("experience_group.${name.lowercase()}")

    companion object {
        fun dummy(name: String) = object : ExperienceGroup {
            override val name = name
            override fun getExperience(level: Int) = 1
            override fun getLevel(experience: Int) = 1
        }
    }
}

/**
 * An experience group which uses [CachedLevelThresholds] to answer how
 * to get a level from an experience value, given only the opposite equation.
 *
 * @author Hiroku
 * @since March 21st, 2022
 */
abstract class CachedExperienceGroup : ExperienceGroup {
    private val thresholds = CachedLevelThresholds(experienceToLevel = this::getExperience)
    override fun getLevel(experience: Int) = thresholds.getLevel(experience)
}

object Erratic : CachedExperienceGroup() {
    override val name = "erratic"
    override fun getExperience(level: Int): Int {
        return when {
            level < 50 -> level.pow(3) * (100 - level) / 50
            level < 68 -> level.pow(3) * (150 - level) / 100
            level < 98 -> level.pow(3) * (1911 - 10 * level) / 3 / 500
            else -> level.pow(3) * (160 - level) / 100
        }
    }
}

object Fast : ExperienceGroup {
    override val name = "fast"
    override fun getExperience(level: Int) = 4 * level.pow(3) / 5
    override fun getLevel(experience: Int) = cbrt(experience * 5.0 / 4).toInt()
}

object MediumFast : ExperienceGroup {
    override val name = "mediumfast"
    override fun getExperience(level: Int) = level.pow(3)
    override fun getLevel(experience: Int) = cbrt(experience.toDouble()).toInt()
}

object MediumSlow : CachedExperienceGroup() {
    override val name = "mediumslow"
    override fun getExperience(level: Int) = max(0, 6 / 5 * level.pow(3) - 15 * level.pow(2) + 100 * level - 140)
}

object Slow : ExperienceGroup {
    override val name = "slow"
    override fun getExperience(level: Int) = 5 * level.pow(3) / 4
    override fun getLevel(experience: Int) = cbrt(experience * 4.0 / 3).toInt()
}

object Fluctuating : CachedExperienceGroup() {
    override val name = "fluctuating"
    override fun getExperience(level: Int): Int {
        return (when {
            level < 15 -> level.pow(3) * ((level + 1.0) / 3 + 24) / 50
            level < 36 -> level.pow(3) * (level + 14) / 50.0
            else -> level.pow(3) * (level / 2.0 + 32) / 50
        }).toInt()
    }
}