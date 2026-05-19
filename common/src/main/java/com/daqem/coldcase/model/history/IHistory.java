package com.daqem.coldcase.model.history;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.IAction;
import net.minecraft.network.chat.Component;

public interface IHistory {

    Time getTime();

    User getUser();

    BlockPosition getPosition();

    IAction getAction();

    Component getComponent();

    Component getMaterialComponent();

    Component getComponentWithPos();

    default double getChance() {
        return 100;
    }

    default boolean shouldReveal(double currentRevealChance) {
        return true;
    }

    default Component getClueComponent() {
        return getComponent();
    }

    long getOriginalTime();
}
