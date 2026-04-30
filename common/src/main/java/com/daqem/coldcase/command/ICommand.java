package com.daqem.coldcase.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface ICommand {

    LiteralArgumentBuilder<CommandSourceStack> getCommand();
}
