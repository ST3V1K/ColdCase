package com.daqem.coldcase.model.history;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.config.ColdCaseCustomConfig;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class UnreliableContainerHistory extends ContainerHistory {

    private final double chance;

    public UnreliableContainerHistory(ContainerHistory history, double chance) {
        super(history.getTime(), history.getUser(), history.getPosition(), history.getItemStack(), history.getAction(), history.getCleanworkArmor());
        this.chance = chance;
    }

    public void appendBaseClues(MutableComponent component, double transactionFactor, Random random) {
        User user = getUser();
        boolean isPartialUser = random.nextFloat() > ColdCaseCustomConfig.userNameLetterRevealChance.get() * transactionFactor;

        MutableComponent timeStr = getTimeComponent(transactionFactor, random);
        MutableComponent userStr = getUserComponent(user, isPartialUser, transactionFactor, random);

        boolean isUnknownUser = userStr.getString()
                .equals(ColdCaseCustomConfig.clueUserUnknown.get());

        MutableComponent colorStr = getSkinColorComponent(isPartialUser, isUnknownUser, transactionFactor, random);

        if (!timeStr.getString().isBlank()) {
            component.append(timeStr);
        }
        if (!userStr.getString().isBlank()) {
            if (!component.getString().isEmpty()) component.append("\n");
            component.append(userStr);
        }
        if (!colorStr.getString().isBlank()) {
            if (!component.getString().isEmpty()) component.append("\n");
            component.append(colorStr);
        }
    }

    private double getRevealChance(double baseRevealChance, double transactionFactor, Random random) {
        double revealChanceModifier = 1.0;
        byte cleanworkArmor = getCleanworkArmor();
        if ((cleanworkArmor & 1) != 0) {
            revealChanceModifier -= ColdCaseCustomConfig.cleanworkBootsHideChance.get();
        }
        if ((cleanworkArmor & 2) != 0) {
            revealChanceModifier -= ColdCaseCustomConfig.cleanworkPantsHideChance.get();
        }
        if ((cleanworkArmor & 4) != 0) {
            revealChanceModifier -= ColdCaseCustomConfig.cleanworkChestplateHideChance.get();
        }
        if ((cleanworkArmor & 8) != 0) {
            revealChanceModifier -= ColdCaseCustomConfig.cleanworkHelmetHideChance.get();
        }

        if (baseRevealChance == ColdCaseCustomConfig.clueBaseRevealChance.get()) {
            long timeElapsed = System.currentTimeMillis() - getTime(random).time();
            long fullChanceTime = ColdCaseCustomConfig.itemRevealFullChanceTime.get();
            long zeroChanceTime = ColdCaseCustomConfig.itemRevealZeroChanceTime.get();

            if (timeElapsed > fullChanceTime) {
                if (timeElapsed >= zeroChanceTime) {
                    baseRevealChance = 0;
                } else {
                    double decayFactor = (double) (timeElapsed - fullChanceTime) / (zeroChanceTime - fullChanceTime);
                    baseRevealChance *= (1.0 - decayFactor);
                }
            }
        }

        return baseRevealChance * revealChanceModifier * transactionFactor * chance;
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

    private MutableComponent getTimeComponent(double transactionFactor, Random random) {
        if (random.nextDouble() > getRevealChance(ColdCaseCustomConfig.timeRevealChance.get(), transactionFactor, random)) {
            return ColdCase.literal(ColdCaseCustomConfig.clueTimeUnknown.get());
        }
        return formatComponent(ColdCaseCustomConfig.clueTime.get(), "{time}", getFormattedTimeAgo(random));
    }

    private MutableComponent getFormattedTimeAgo(Random random) {
        long timeAgo = System.currentTimeMillis() - getTime(random).time();

        long years = timeAgo / 31536000000L;
        long days = (timeAgo % 31536000000L) / 86400000L;
        long hours = (timeAgo % 3600000L) / 3600000L;
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
                                ColdCase.literal(new Date(getTime(random).time()).toString()))));
    }

    private MutableComponent getUserComponent(User user, boolean isPartialUser, double transactionFactor, Random random) {
        if (random.nextDouble() > getRevealChance(ColdCaseCustomConfig.userRevealChance.get(), transactionFactor, random)) {
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

    private MutableComponent getSkinColorComponent(boolean isPartialUser, boolean isUnknownUser, double transactionFactor, Random random) {
        if (isUnknownUser || isPartialUser) {
            if (random.nextDouble() > getRevealChance(ColdCaseCustomConfig.skinColorRevealChance.get(), transactionFactor, random)) {
                String hardToTell = ColdCaseCustomConfig.clueHardToTell.get();
                return Component.literal(ColdCaseCustomConfig.clueSkinColor.get()
                        .replace("{skin_color}", hardToTell));
            }
            if (getCleanworkArmor() == 0) {
                String hardToTell = ColdCaseCustomConfig.clueHardToTell.get();
                return Component.literal(ColdCaseCustomConfig.clueSkinColor.get()
                        .replace("{skin_color}", hardToTell));
            }
            String skinColor = String.valueOf(getCleanworkArmor());
            return Component.literal(ColdCaseCustomConfig.clueSkinColor.get()
                    .replace("{skin_color}", skinColor));
        }
        return Component.empty();
    }

    @Override
    public MutableComponent getMaterialComponent() {
        ItemStack itemStack = getItemStack().toItemStack();
        itemStack.setCount(1);
        return itemStack.getDisplayName().copy();
    }

    public Time getTime(Random random) {
        long now = System.currentTimeMillis();
        long startTime = super.getTime().time();
        long realElapsed = now - startTime;
        double driftFactor = 1 + random.nextDouble(-ColdCaseCustomConfig.timeInaccuracy.get(), ColdCaseCustomConfig.timeInaccuracy.get());
        long skewedElapsed = Math.round(realElapsed * driftFactor);
        return new Time(now - skewedElapsed);
    }
}