package com.daqem.coldcase.command;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class InspectCommand implements com.daqem.coldcase.command.ICommand {


    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("inspect")
                .requires(source -> source.hasPermission(2))
                .executes(context -> inspect(context.getSource()));
    }

    private static int inspect(CommandSourceStack source) {
        if (source.getPlayer() instanceof ColdCaseServerPlayer player) {
            player.coldcase$setInspecting(!player.coldcase$isInspecting());
            source.sendSuccess(() -> ColdCase.translate("commands.inspect." + (player.coldcase$isInspecting() ? "enabled" : "disabled"), ColdCase.getName()), false);
        }
        return 1;
    }
}
