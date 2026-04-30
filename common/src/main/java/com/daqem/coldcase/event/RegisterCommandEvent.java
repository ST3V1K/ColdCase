package com.daqem.coldcase.event;

import dev.architectury.event.events.common.CommandRegistrationEvent;

public class RegisterCommandEvent {

    public static void registerEvent() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) ->
                com.daqem.coldcase.command.ColdCaseCommand.registerCommand(dispatcher));
    }
}
