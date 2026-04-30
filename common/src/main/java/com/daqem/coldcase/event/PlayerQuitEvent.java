package com.daqem.coldcase.event;

import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.model.action.SessionAction;
import dev.architectury.event.events.common.PlayerEvent;

public class PlayerQuitEvent {

    public static void registerEvent() {
        PlayerEvent.PLAYER_QUIT.register(player ->
                Services.SESSION.insert(
                        player.getUUID(),
                        player.level(),
                        player.getOnPos(),
                        SessionAction.QUIT));
    }
}
