package com.daqem.coldcase.entity;

import com.daqem.coldcase.ColdCase;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ColdCaseEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ColdCase.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<DeadBodyEntity>> DEAD_BODY = ENTITY_TYPES.register("dead_body", () ->
            EntityType.Builder.<DeadBodyEntity>of(DeadBodyEntity::new, MobCategory.MISC)
                    .sized(1.2f, 0.5f)
                    .clientTrackingRange(10)
                    .updateInterval(2)
                    .build("dead_body")
    );

    public static void init() {
        ENTITY_TYPES.register();
        EntityAttributeRegistry.register(DEAD_BODY, DeadBodyEntity::createAttributes);
    }
}
