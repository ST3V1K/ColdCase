package com.daqem.coldcase.event;

import com.daqem.coldcase.database.service.Services;
import dev.architectury.event.events.common.LifecycleEvent;

import java.util.ArrayList;
import java.util.List;

public class LevelLoadEvent {

    private static final List<String> registeredLevels = new ArrayList<>();

    public static void registerEvent() {
        LifecycleEvent.SERVER_LEVEL_LOAD.register(level -> {
            String levelName = level.dimension().location().toString();
            if (!registeredLevels.contains(levelName)) {
                registeredLevels.add(levelName);
                Services.LEVEL.insert(level.dimension().location().toString());
            }
        });
    }
}
