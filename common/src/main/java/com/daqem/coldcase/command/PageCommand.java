package com.daqem.coldcase.command;

import com.daqem.coldcase.command.page.Page;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class PageCommand implements ICommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("page")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("page", IntegerArgumentType.integer())
                        .executes(context -> page(context.getSource(), IntegerArgumentType.getInteger(context, "page"))));
    }

    private static int page(CommandSourceStack source, int page) {
        if (source.getPlayer() instanceof ColdCaseServerPlayer player) {
            List<Page> lookupResults = player.coldcase$getPages();
            if (page > 0 && page <= lookupResults.size()) {
                Page pageToDisplay = lookupResults.get(page - 1);
                pageToDisplay.sendToPlayer((ServerPlayer) player);
            } else {
                source.sendFailure(com.daqem.coldcase.ColdCase.translate("lookup.invalid_page", com.daqem.coldcase.ColdCase.getName()));
            }
        }
        return 1;
    }
}
