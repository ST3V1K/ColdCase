package com.daqem.coldcase.model.history;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.BlockAction;
import net.minecraft.network.chat.Component;

import java.util.Random;
import java.util.UUID;

public class UnreliableBlockHistory extends BlockHistory {

    private static final double INACCURACY = 0.01f;
    private static final double USER_REVEAL_CHANCE = 0.2f;

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
        if (random.nextFloat() < USER_REVEAL_CHANCE)
            return user;
        String name = "%c...".formatted(user.getName().charAt(0));
        return new User(name, null);
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
