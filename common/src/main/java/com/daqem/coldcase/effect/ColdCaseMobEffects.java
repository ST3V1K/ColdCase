package com.daqem.coldcase.effect;

import com.daqem.coldcase.ColdCase;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

public class ColdCaseMobEffects {

    private static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ColdCase.MOD_ID, Registries.MOB_EFFECT);

    public static final RegistrySupplier<MobEffect> IDENTITY_THEFT =
            MOB_EFFECTS.register("identity_theft", IdentityTheftMobEffect::new);

    public static void init() {
        MOB_EFFECTS.register();
    }

    public static Holder<MobEffect> identityTheftHolder() {
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(IDENTITY_THEFT.get());
    }
}
