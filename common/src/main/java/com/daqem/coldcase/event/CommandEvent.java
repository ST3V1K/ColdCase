package com.daqem.coldcase.event;

import com.daqem.coldcase.database.service.Services;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandPerformEvent;
import net.minecraft.server.level.ServerPlayer;

public class CommandEvent {

    public static void registerEvent() {
        CommandPerformEvent.EVENT.register(commandPerformEvent -> {
            ServerPlayer player = commandPerformEvent.getResults().getContext().getSource().getPlayer();
            if (player != null) {
                Services.COMMAND.insert(
                        player.getUUID(),
                        player.level(),
                        player.getOnPos(),
                        commandPerformEvent.getResults().getReader().getString()
                );
            }
            return EventResult.pass();
        });
    }
}
