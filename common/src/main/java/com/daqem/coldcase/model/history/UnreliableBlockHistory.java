package com.daqem.coldcase.model.history;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.config.ColdCaseCustomConfig;
import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.BlockAction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class UnreliableBlockHistory extends BlockHistory {

    private final Random random;

    public UnreliableBlockHistory(BlockHistory history) {
        super(history.getTime(), history.getUser(), history.getPosition(), history.getMaterial(), (BlockAction) history.getAction(), history.getTool(), history.getSkinColor(), history.getCleanworkArmor());
        this.random = this.getRandom();
    }

    public boolean shouldReveal(double currentRevealChance) {
        return random.nextDouble() < currentRevealChance;
    }

    public Component getClueComponent() {
        User user = getUser();

        boolean isPartialUser = random.nextFloat() > ColdCaseCustomConfig.userNameLetterRevealChance.get();

        MutableComponent timeStr = getTimeComponent();
        MutableComponent userStr = getUserComponent(user, isPartialUser);

        boolean isUnknownUser = userStr.getString()
                .equals(ColdCaseCustomConfig.clueUserUnknown.get());

        MutableComponent colorStr = getSkinColorComponent(isPartialUser, isUnknownUser);
        MutableComponent toolStr = getToolComponent();

        MutableComponent result = Component.empty();

        if (!timeStr.getString().isBlank()) {
            result.append(timeStr);
        }
        if (!userStr.getString().isBlank()) {
            result.append(Component.literal("\n")).append(userStr);
        }
        if (!colorStr.getString().isBlank()) {
            result.append(Component.literal("\n")).append(colorStr);
        }
        if (!toolStr.getString().isBlank()) {
            result.append(Component.literal("\n")).append(toolStr);
        }

        return result;
    }

    private double getRevealChance(double baseRevealChance) {
        double hideChance = 0;
        byte cleanworkArmor = getCleanworkArmor();
        if ((cleanworkArmor & 1) != 0) {
            hideChance += ColdCaseCustomConfig.cleanworkBootsHideChance.get();
        }
        if ((cleanworkArmor & 2) != 0) {
            hideChance += ColdCaseCustomConfig.cleanworkPantsHideChance.get();
        }
        if ((cleanworkArmor & 4) != 0) {
            hideChance += ColdCaseCustomConfig.cleanworkChestplateHideChance.get();
        }
        if ((cleanworkArmor & 8) != 0) {
            hideChance += ColdCaseCustomConfig.cleanworkHelmetHideChance.get();
        }
        return baseRevealChance * hideChance;
    }

    private MutableComponent formatComponent(String format, String placeholder, Component value) {
        MutableComponent result = Component.empty();
        String[] parts = format.split(Pattern.quote(placeholder), -1);
        for (int i = 0; i < parts.length; i++) {
            result.append(parts[i]);

            if (i < parts.length - 1) {
                result.append(value);
            }
        }

        if (parts.length > 0 && parts[0].startsWith("§")) {
            result.withStyle(ChatFormatting.getByCode(parts[0].charAt(1)));
        }
        return result;
    }

    private MutableComponent getTimeComponent() {
        if (random.nextDouble() < getRevealChance(ColdCaseCustomConfig.timeRevealChance.get())) {
            return ColdCase.literal(ColdCaseCustomConfig.clueTimeUnknown.get());
        }
        return formatComponent(ColdCaseCustomConfig.clueTime.get(), "{time}", getFormattedTimeAgo());
    }

    private MutableComponent getFormattedTimeAgo() {
        long timeAgo = System.currentTimeMillis() - getTime().time();

        long years = timeAgo / 31536000000L;
        long days = (timeAgo % 31536000000L) / 86400000L;
        long hours = (timeAgo % 86400000L) / 3600000L;
        long minutes = (timeAgo % 3600000L) / 60000L;
        long seconds = (timeAgo % 60000L) / 1000L;

        String format;
        if (years > 0) {
            format = ColdCaseCustomConfig.timeFormatYears.get();
        } else if (days > 0) {
            format = ColdCaseCustomConfig.timeFormatDays.get();
        } else if (hours > 0) {
            format = ColdCaseCustomConfig.timeFormatHours.get();
        } else if (minutes > 0) {
            format = ColdCaseCustomConfig.timeFormatMinutes.get();
        } else {
            format = ColdCaseCustomConfig.timeFormatSeconds.get();
        }

        format = format
                .replace("{years}", String.valueOf(years))
                .replace("{days}", String.valueOf(days))
                .replace("{hours}", String.valueOf(hours))
                .replace("{minutes}", String.valueOf(minutes))
                .replace("{seconds}", String.valueOf(seconds));

        return Component.literal(format)
                .withStyle(Style.EMPTY
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                ColdCase.literal(new Date(getTime().time()).toString()))));
    }

    private MutableComponent getUserComponent(User user, boolean isPartialUser) {
        if (random.nextDouble() < getRevealChance(ColdCaseCustomConfig.userRevealChance.get())) {
            return ColdCase.literal(ColdCaseCustomConfig.clueUserUnknown.get());
        }

        String name = user.getName();
        if (isPartialUser) {
            String letter = String.valueOf(name.charAt(random.nextInt(name.length())));
            return Component.literal(ColdCaseCustomConfig.clueUsernameContainsLetter.get()
                    .replace("{letter}", letter));
        } else {
            return Component.literal(ColdCaseCustomConfig.clueUser.get().replace("{user}", name));
        }
    }

    private MutableComponent getSkinColorComponent(boolean isPartialUser, boolean isUnknownUser) {
        if (isUnknownUser || isPartialUser) {
            if (random.nextDouble() < getRevealChance(ColdCaseCustomConfig.skinColorRevealChance.get())) {
                String hardToTell = ColdCaseCustomConfig.clueHardToTell.get();
                return Component.literal(ColdCaseCustomConfig.clueSkinColor.get()
                        .replace("{skin_color}", hardToTell));
            }
            if (getSkinColor() == null || getSkinColor().equals("unknown")) {
                String hardToTell = ColdCaseCustomConfig.clueHardToTell.get();
                return Component.literal(ColdCaseCustomConfig.clueSkinColor.get()
                        .replace("{skin_color}", hardToTell));
            }
            String skinColor = getSkinColor();
            return Component.literal(ColdCaseCustomConfig.clueSkinColor.get()
                    .replace("{skin_color}", skinColor));
        }
        return Component.empty();
    }

    private MutableComponent getToolComponent() {
        if (getAction() == BlockAction.BREAK_BLOCK) {
            if (random.nextDouble() < getRevealChance(ColdCaseCustomConfig.toolRevealChance.get())) {
                String unknown = ColdCaseCustomConfig.clueUnknown.get();
                return Component.literal(ColdCaseCustomConfig.clueTool.get()
                        .replace("{tool}", unknown));
            }
            if (getTool() == null || getTool().equals("minecraft:air")) {
                return Component.literal(ColdCaseCustomConfig.clueTool.get()
                        .replace("{tool}", "their hands"));
            }
            String tool = Component.translatable(BuiltInRegistries.ITEM.get(
                    ResourceLocation.parse(getTool())).getDescriptionId()).getString();
            return Component.literal(ColdCaseCustomConfig.clueTool.get().replace("{tool}", tool));
        }
        return Component.empty();
    }

    @Override
    public Time getTime() {
        long now = System.currentTimeMillis();
        long startTime = super.getTime().time();
        long realElapsed = now - startTime;
        double driftFactor = 1 + random.nextDouble(-ColdCaseCustomConfig.timeInaccuracy.get(), ColdCaseCustomConfig.timeInaccuracy.get());
        long skewedElapsed = Math.round(realElapsed * driftFactor);
        return new Time(now - skewedElapsed);
    }

    private Random getRandom() {
        BlockPosition pos = super.getPosition();
        long hash = 0xcbf29ce484222325L
                + (long) pos.x() * 374761393L
                + (long) pos.y() * 668265263L
                + (long) pos.z() * 873612221L;
        hash ^= super.getTime().time() * 0x9e3779b97f4a7c15L;
        return new Random(hash);
    }
}
