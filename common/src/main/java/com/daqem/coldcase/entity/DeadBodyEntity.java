package com.daqem.coldcase.entity;

import com.daqem.coldcase.sound.ColdCaseSoundEvents;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DeadBodyEntity extends Mob {

    private static final EntityDataAccessor<Optional<UUID>> DATA_DECEASED_UUID = SynchedEntityData.defineId(DeadBodyEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Long> DATA_DEATH_TIME = SynchedEntityData.defineId(DeadBodyEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<String> DATA_ATTACKER_SKIN_COLOR = SynchedEntityData.defineId(DeadBodyEntity.class, EntityDataSerializers.STRING);

    private static final long DESPAWN_TIME_MILLIS = 7200000;

    @Nullable
    private GameProfile deceasedProfile;

    public DeadBodyEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DECEASED_UUID, Optional.empty());
        builder.define(DATA_DEATH_TIME, 0L);
        builder.define(DATA_ATTACKER_SKIN_COLOR, "");
    }

    @Override
    public void baseTick() {
        super.baseTick();

        Level level = this.level();
        if (!level.isClientSide()) {
            long now = System.currentTimeMillis();
            long deathTime = this.entityData.get(DATA_DEATH_TIME);
            if (deathTime > 0 && now - deathTime > DESPAWN_TIME_MILLIS) {
                this.discard();
            }

            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        int flyCount = getFlyCount();
        if (flyCount <= 0 || this.random.nextBoolean()) {
            return;
        }

        if (flyCount > this.random.nextInt(100)) {
            float dist = player.distanceTo(this);
            float maxRange = 8;
            if (dist < maxRange) {
                float distVolume = 1 - dist / maxRange;
                float flyVolume = Mth.map(flyCount, 2, 100, 0.15f, 0.5f);
                float volume = distVolume * flyVolume;
                level.playLocalSound(this,
                        ColdCaseSoundEvents.FLIES_AMBIENT.get(),
                        SoundSource.AMBIENT,
                        volume,
                        1.0F);
            }
        }
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return true;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return !source.is(DamageTypes.GENERIC_KILL) && !source.is(DamageTypes.FELL_OUT_OF_WORLD);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        Level level = this.level();
        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.is(Items.LEAD) && this.canBeLeashed()) {
            this.setLeashedTo(player, true);
            itemStack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (this.isLeashed() && this.getLeashHolder() == player) {
            this.dropLeash(true, !player.getAbilities().instabuild);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("DeceasedUUID")) {
            this.entityData.set(DATA_DECEASED_UUID, Optional.of(compoundTag.getUUID("DeceasedUUID")));
        }
        if (compoundTag.contains("DeathTime")) {
            this.entityData.set(DATA_DEATH_TIME, compoundTag.getLong("DeathTime"));
        }
        if (compoundTag.contains("DeceasedProfile", 10)) {
            CompoundTag profileTag = compoundTag.getCompound("DeceasedProfile");
            UUID uuid = profileTag.getUUID("Id");
            String name = profileTag.getString("Name");
            this.deceasedProfile = new GameProfile(uuid, name);
        }
        if (compoundTag.contains("AttackerSkinColor")) {
            this.entityData.set(DATA_ATTACKER_SKIN_COLOR, compoundTag.getString("AttackerSkinColor"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.entityData.get(DATA_DECEASED_UUID)
                .ifPresent(uuid -> compoundTag.putUUID("DeceasedUUID", uuid));
        compoundTag.putLong("DeathTime", this.entityData.get(DATA_DEATH_TIME));
        if (this.deceasedProfile != null) {
            CompoundTag profileTag = new CompoundTag();
            if (this.deceasedProfile.getId() != null) {
                profileTag.putUUID("Id", this.deceasedProfile.getId());
            }
            if (this.deceasedProfile.getName() != null) {
                profileTag.putString("Name", this.deceasedProfile.getName());
            }
            compoundTag.put("DeceasedProfile", profileTag);
        }
        compoundTag.putString("AttackerSkinColor", this.entityData.get(DATA_ATTACKER_SKIN_COLOR));
    }

    public void setDeceasedUuid(UUID uuid) {
        this.entityData.set(DATA_DECEASED_UUID, Optional.ofNullable(uuid));
    }

    public Optional<UUID> getDeceasedUuid() {
        return this.entityData.get(DATA_DECEASED_UUID);
    }

    public void setDeathTime(long time) {
        this.entityData.set(DATA_DEATH_TIME, time);
    }

    public long getDeathTime() {
        return this.entityData.get(DATA_DEATH_TIME);
    }

    public void setDeceasedProfile(GameProfile profile) {
        this.deceasedProfile = profile;
    }

    @Nullable
    public GameProfile getDeceasedProfile() {
        if (this.deceasedProfile == null) {
            this.getDeceasedUuid().ifPresent(uuid -> {
                SkullBlockEntity.fetchGameProfile(uuid).thenAccept(profile -> {
                    profile.ifPresent(gameProfile -> this.deceasedProfile = gameProfile);
                });
            });
        }
        return this.deceasedProfile;
    }

    public void setAttackerSkinColor(String skinColor) {
        this.entityData.set(DATA_ATTACKER_SKIN_COLOR, skinColor);
    }

    public String getAttackerSkinColor() {
        return this.entityData.get(DATA_ATTACKER_SKIN_COLOR);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
    }

    public int getFlyCount() {
        long timeSinceDeath = System.currentTimeMillis() - getDeathTime();
        if (timeSinceDeath < 60000)
            return 0;
        return Math.clamp(timeSinceDeath / 60000, 2, 100);
    }
}
