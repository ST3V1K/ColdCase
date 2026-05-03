package com.daqem.coldcase.mixin;

import com.daqem.coldcase.effect.ColdCaseMobEffects;
import com.daqem.coldcase.effect.IdentityTheftAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IdentityTheftAccessor {

    @Unique
    private static final EntityDataAccessor<Optional<Component>> STOLEN_IDENTITY =
            SynchedEntityData.defineId(PlayerMixin.class, EntityDataSerializers.OPTIONAL_COMPONENT);

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onEffectRemoved(MobEffectInstance instance) {
        super.onEffectRemoved(instance);
        if (instance.getEffect().is(ColdCaseMobEffects.IDENTITY_THEFT.getKey())) {
            this.coldcase$clearStolenIdentity();
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void coldcase$onDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(STOLEN_IDENTITY, Optional.empty());
    }

    @Override
    public void coldcase$setStolenIdentity(Component name) {
        this.getEntityData().set(STOLEN_IDENTITY, Optional.ofNullable(name));
    }

    @Override
    public Optional<Component> coldcase$getStolenIdentity() {
        return this.getEntityData().get(STOLEN_IDENTITY);
    }
}
