package com.cablemc.pokemoncobbled.common.api.pokeball.catching.modifiers

import com.cablemc.pokemoncobbled.common.api.pokeball.catching.CatchRateModifier
import com.cablemc.pokemoncobbled.common.pokemon.Pokemon
import net.minecraft.entity.LivingEntity

class GuaranteedModifier : CatchRateModifier {
    override fun modifyCatchRate(currentCatchRate: Float, thrower: LivingEntity, pokemon: Pokemon, host: Pokemon?): Float {
        return 255.0f // Catch rates should check the CatchRateModifier#isGuaranteed call
    }
}