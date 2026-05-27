package com.daqem.coldcase.model.history;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;

public abstract class UnreliableHistory implements IHistory {

    private final Time time;
    private final User user;
    private final BlockPosition position;

    public UnreliableHistory(Time time, User user, BlockPosition position) {
        this.time = time;
        this.user = user;
        this.position = position;
    }

    @Override
    public Time getTime() {
        return time;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public BlockPosition getPosition() {
        return position;
    }

    @Override
    public long getOriginalTime() {
        return time.time();
    }
}