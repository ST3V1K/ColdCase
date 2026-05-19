package com.daqem.coldcase.model.history;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.IAction;
import net.minecraft.network.chat.Component;

public abstract class History implements IHistory {

    private final Time time;
    private final User user;
    private final BlockPosition position;
    private final IAction action;

    public History(Time time, User user, BlockPosition position, IAction action) {
        this.time = time;
        this.user = user;
        this.position = position;
        this.action = action;
    }

    public Time getTime() {
        return time;
    }

    public User getUser() {
        return user;
    }

    public BlockPosition getPosition() {
        return position;
    }

    public IAction getAction() {
        return action;
    }

    @Override
    public Component getComponentWithPos() {
        return getComponent().copy().append(" ").append(getPosition().getComponent());
    }

    @Override
    public long getOriginalTime() {
        return time.time();
    }
}
