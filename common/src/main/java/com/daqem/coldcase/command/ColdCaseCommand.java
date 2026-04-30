package com.daqem.coldcase.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ColdCaseCommand {

    private static final com.daqem.coldcase.command.ICommand INSPECT = new com.daqem.coldcase.command.InspectCommand();
    private static final com.daqem.coldcase.command.ICommand LOOKUP = new com.daqem.coldcase.command.LookupCommand();
    private static final com.daqem.coldcase.command.ICommand PAGE = new com.daqem.coldcase.command.PageCommand();

    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(commandWithPrefix("coldcase"));
        dispatcher.register(commandWithPrefix("cs"));
        dispatcher.register(commandWithPrefix("grieflogger"));
        dispatcher.register(commandWithPrefix("gl"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> commandWithPrefix(String prefix) {
        return Commands.literal(prefix)
                .then(INSPECT.getCommand())
                .then(LOOKUP.getCommand())
                .then(PAGE.getCommand());
    }
}
