package com.daqem.coldcase.model.history;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.BlockAction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Random;
import java.util.UUID;

public class UnreliableBlockHistory extends BlockHistory {

    private static final double INACCURACY = 0.01f;
    private static final double CLUE_REVEAL_CHANCE = 0.8f;

    private static final double TIME_REVEAL_CHANCE = 0.8f;
    private static final double USER_COMPONENT_REVEAL_CHANCE = 0.8f;
    private static final double PARTIAL_USER_REVEAL_CHANCE = 0.2f;
    private static final double SKIN_COLOR_REVEAL_CHANCE = 0.8f;
    private static final double TOOL_REVEAL_CHANCE = 0.8f;

    private final Random random;

    public UnreliableBlockHistory(long time, String name, String uuid, int x, int y, int z, String material, int blockAction) {
        this(new Time(time), new User(name, UUID.fromString(uuid)), new BlockPosition(x, y, z), material, BlockAction.fromId(blockAction));
    }

    public UnreliableBlockHistory(Time time, User user, BlockPosition position, String material, BlockAction action) {
        super(time, user, position, material, action);
        this.random = this.getRandom();
    }

    @Override
    public Component getComponent() {
        return getTime().getFormattedTimeAgo().append(" ")
                .append(getAction().getPrefix()).append(" ")
                .append(getUser().getNameComponent()).append(" ")
                .append(getAction().getPastTense()).append(" ")
                .append(getMaterialComponent());
    }

    public Component getClueComponent() {
        if (random.nextDouble() > CLUE_REVEAL_CHANCE) {
            return ColdCase.translate("clue.not_found").withStyle(ChatFormatting.GRAY);
        }

        User user = getUser();
        boolean isPartialUser = user.getName().endsWith("...");
        
        MutableComponent timeStr = getTimeComponent();
        MutableComponent userStr = getUserComponent(user, isPartialUser);
        
        boolean isUnknownUser = userStr.getString().equals(ColdCase.translate("clue.user.unknown").getString());
        
        MutableComponent colorStr = getSkinColorComponent(isPartialUser, isUnknownUser);
        MutableComponent toolStr = getToolComponent();

        MutableComponent header = ColdCase.translate("clue.found.header")
                .withStyle(ChatFormatting.GOLD);
        MutableComponent body = ColdCase.translate("clue.found.body", timeStr, userStr, colorStr, toolStr)
                .withStyle(ChatFormatting.GREEN);

        return header.append("\n").append(body);
    }

    private MutableComponent getTimeComponent() {
        if (random.nextDouble() > TIME_REVEAL_CHANCE) {
            return ColdCase.translate("clue.time.unknown");
        }
        return ColdCase.translate("clue.time", getTime().getFormattedTimeAgo());
    }

    private MutableComponent getUserComponent(User user, boolean isPartialUser) {
        if (random.nextDouble() > USER_COMPONENT_REVEAL_CHANCE) {
            return ColdCase.translate("clue.user.unknown");
        }
        
        String name = user.getName();
        if (isPartialUser) {
            return ColdCase.translate("clue.user.start_with", String.valueOf(name.charAt(0)));
        } else {
            return ColdCase.translate("clue.user", name);
        }
    }

    private MutableComponent getSkinColorComponent(boolean isPartialUser, boolean isUnknownUser) {
        if (isUnknownUser || isPartialUser) {
            if (random.nextDouble() > SKIN_COLOR_REVEAL_CHANCE) {
                return ColdCase.translate("clue.skin_color", ColdCase.translate("clue.hard_to_tell"));
            }
            // TODO: Implement real logic for skin color
            return ColdCase.translate("clue.skin_color", ColdCase.translate("clue.hard_to_tell"));
        }
        return Component.empty();
    }

    private MutableComponent getToolComponent() {
        if (random.nextDouble() > TOOL_REVEAL_CHANCE) {
            return ColdCase.translate("clue.tool", ColdCase.translate("clue.unknown"));
        }
        // TODO: Implement real logic for tool used
        return ColdCase.translate("clue.tool", ColdCase.translate("clue.unknown"));
    }

    @Override
    public Time getTime() {
        long now = System.currentTimeMillis();
        long startTime = super.getTime().time();
        long realElapsed = now - startTime;
        double driftFactor = 1 + random.nextDouble(-INACCURACY, INACCURACY);
        long skewedElapsed = Math.round(realElapsed * driftFactor);
        return new Time(now - skewedElapsed);
    }

    @Override
    public User getUser() {
        User user = super.getUser();
        if (random.nextDouble() < PARTIAL_USER_REVEAL_CHANCE) {
            String name = "%c...".formatted(user.getName().charAt(0));
            return new User(name, null);
        }
        return user;
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
