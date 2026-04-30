package com.daqem.coldcase.event;

import com.daqem.coldcase.database.service.Services;
import dev.architectury.event.EventResult;

public class ChatEvent {

    public static void registerEvent() {
        dev.architectury.event.events.common.ChatEvent.RECEIVED.register((player, component) -> {
            if (player != null) {
                Services.CHAT.insert(
                        player.getUUID(),
                        player.level(),
                        player.getOnPos(),
                        component.getString()
                );
            }
            return EventResult.pass();
        });
    }
}
