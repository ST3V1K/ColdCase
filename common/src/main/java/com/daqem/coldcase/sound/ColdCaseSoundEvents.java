package com.daqem.coldcase.sound;

import com.daqem.coldcase.ColdCase;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class ColdCaseSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ColdCase.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> FLIES_AMBIENT = SOUND_EVENTS.register("flies_ambient", () -> SoundEvent.createVariableRangeEvent(ColdCase.getId("flies_ambient")));

    public static void init() {
        SOUND_EVENTS.register();
    }
}
