package com.daqem.coldcase.util;

import com.daqem.coldcase.model.BlockPosition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class ClueComponentUtils {

    public static MutableComponent createNavigationFooter(int currentClueIndex, int totalClues, BlockPosition locationPos) {
        MutableComponent footer = Component.literal("\n");

        // Previous button
        if (currentClueIndex > 0) {
            footer.append(Component.literal("<- Previous")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.AQUA)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/coldcase cluenav " + locationPos.x() + " " + locationPos.y() + " " + locationPos.z() + " " + (currentClueIndex - 1)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to see previous clue")))));
        }

        // Clue count
        footer.append(Component.literal(" [" + (currentClueIndex + 1) + "/" + totalClues + "] ")
                .withStyle(ChatFormatting.GRAY));

        // Next button (only if not the last clue)
        if (currentClueIndex < totalClues - 1) {
            footer.append(Component.literal("Next ->")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.AQUA)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/coldcase cluenav " + locationPos.x() + " " + locationPos.y() + " " + locationPos.z() + " " + (currentClueIndex + 1)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to see next clue")))));
        }
        return footer;
    }
}
