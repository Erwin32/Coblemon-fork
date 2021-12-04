package com.cablemc.pokemoncobbled.common.entity.pokemon

import com.cablemc.pokemoncobbled.common.api.pokemon.PokemonSpecies
import com.cablemc.pokemoncobbled.common.entity.EntityProperty
import com.cablemc.pokemoncobbled.common.entity.EntityRegistry
import com.cablemc.pokemoncobbled.common.pokemon.Pokemon
import com.cablemc.pokemoncobbled.common.util.DataKeys
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.Packet
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.animal.ShoulderRidingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData
import net.minecraftforge.fmllegacy.network.NetworkHooks

class PokemonEntity(
    level: Level,
    pokemon: Pokemon = Pokemon(),
    type: EntityType<out PokemonEntity> = EntityRegistry.POKEMON.get()
) : ShoulderRidingEntity(type, level), IEntityAdditionalSpawnData {
    var pokemon: Pokemon
    val delegate = if (level.isClientSide) {
        // Don't import because scanning for imports is a CI job we'll do later to detect errant access to client from server
        com.cablemc.pokemoncobbled.client.entity.PokemonClientDelegate()
    } else {
        PokemonServerDelegate()
    }

    val entityProperties = mutableListOf<EntityProperty<*>>()

    val dexNumber = addEntityProperty(SPECIES_DEX, pokemon.species.nationalPokedexNumber)
    val isMoving = addEntityProperty(MOVING, false)
    val scaleModifier = addEntityProperty(SCALE_MODIFIER, pokemon.scaleModifier)
    // properties like the above are synced and can be subscribed to changes for on either side

    init {
        this.pokemon = pokemon.also { it.entity = this }
        delegate.initialize(this)
    }

    companion object {
        private val SPECIES_DEX = SynchedEntityData.defineId(PokemonEntity::class.java, EntityDataSerializers.INT)
        private val MOVING = SynchedEntityData.defineId(PokemonEntity::class.java, EntityDataSerializers.BOOLEAN)
        private val SCALE_MODIFIER = SynchedEntityData.defineId(PokemonEntity::class.java, EntityDataSerializers.FLOAT)
    }

    override fun tick() {
        super.tick()
        entityProperties.forEach { it.checkForUpdate() }
        delegate.tick(this)
    }

    override fun save(nbt: CompoundTag): Boolean {
        nbt.put(DataKeys.POKEMON, pokemon.saveToNBT(CompoundTag()))
        return super.save(nbt)
    }

    override fun saveWithoutId(nbt: CompoundTag): CompoundTag {
        nbt.put(DataKeys.POKEMON, pokemon.saveToNBT(CompoundTag()))
        return super.saveWithoutId(nbt)
    }

    override fun load(nbt: CompoundTag) {
        super.load(nbt)
        pokemon = Pokemon().loadFromNBT(nbt.getCompound(DataKeys.POKEMON))
        dexNumber.set(pokemon.species.nationalPokedexNumber)
        scaleModifier.set(pokemon.scaleModifier)
        speed = 0.35F
    }

    public override fun registerGoals() {
        goalSelector.addGoal(1, WaterAvoidingRandomStrollGoal(this, speed.toDouble()))
        goalSelector.addGoal(2, LookAtPlayerGoal(this, Player::class.java, 5F))
    }

    fun <T> addEntityProperty(accessor: EntityDataAccessor<T>, initialValue: T): EntityProperty<T> {
        val property = EntityProperty(
            entityData = entityData,
            accessor = accessor,
            initialValue = initialValue
        )
        entityProperties.add(property)
        return property
    }

    override fun getBreedOffspring(level: ServerLevel, partner: AgeableMob) = null

    override fun canSitOnShoulder(): Boolean {
        // TODO: Determine what can or can't be shouldered
        return true
    }

    override fun mobInteract(player : Player, hand : InteractionHand) : InteractionResult {
        // TODO: Move to proper pokemon interaction menu
        if (player.isCrouching && hand == InteractionHand.MAIN_HAND) {
            if (canSitOnShoulder() && player is ServerPlayer) {
                // TODO: Check ownership as well
                this.setEntityOnShoulder(player)
            }
        }
        return super.mobInteract(player, hand)
    }

    override fun getDimensions(pPose: Pose): EntityDimensions {
        val scale = pokemon.form.baseScale * pokemon.scaleModifier
        return pokemon.form.hitbox.scale(scale)
    }

    override fun getAddEntityPacket(): Packet<*> {
        return NetworkHooks.getEntitySpawningPacket(this)
    }

    override fun writeSpawnData(buffer: FriendlyByteBuf) {
        buffer.writeFloat(scaleModifier.get())
        buffer.writeShort(pokemon.species.nationalPokedexNumber)
        buffer.writeUtf(pokemon.form.name)
    }

    override fun readSpawnData(buffer: FriendlyByteBuf) {
        if (this.level.isClientSide) {
            pokemon.scaleModifier = buffer.readFloat()
            pokemon.species = PokemonSpecies.getByPokedexNumber(buffer.readUnsignedShort())!! // TODO exception handling
            pokemon.form = pokemon.species.forms.find { form -> form.name == buffer.readUtf() }!! // TODO exception handling
        }
    }

    override fun shouldBeSaved(): Boolean {
        return pokemon.isWild()
    }
}