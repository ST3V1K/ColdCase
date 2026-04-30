package com.daqem.coldcase.event;

import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.model.action.BlockAction;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class EntityEvents {

    public static void registerEvents() {
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (source.getEntity() instanceof ServerPlayer serverPlayer) {
                ResourceLocation entityLocation = entity.getType().arch$registryName();
                if (entityLocation != null) {
                    Services.BLOCK.insertEntity(
                            serverPlayer.getUUID(),
                            entity.level().dimension().location().toString(),
                            entity.blockPosition(),
                            entityLocation.toString(),
                            BlockAction.KILL_ENTITY
                    );
                }
            }
            return EventResult.pass();
        });
    }
}
