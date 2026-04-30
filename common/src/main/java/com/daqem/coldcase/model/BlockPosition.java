package com.daqem.coldcase.model;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

public record BlockPosition(int x, int y, int z) {

    public Component getComponent() {
        return com.daqem.coldcase.ColdCase.translate("lookup.position", x, y, z)
                .withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, com.daqem.coldcase.ColdCase.literal("Click to teleport to this position.")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + x + " " + y + " " + z)));
    }
}
