package com.daqem.coldcase.model.history;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.IAction;
import net.minecraft.network.chat.Component;

public interface IHistory {

    Time getTime();

    User getUser();

    long getOriginalTime();

    default BlockPosition getPosition() {
        return null;
    }

    default IAction getAction() {
        return null;
    }

    default Component getComponent() {
        return Component.empty();
    }

    default Component getMaterialComponent() {
        return Component.empty();
    }

    default Component getComponentWithPos() {
        return Component.empty();
    }

    default Component getClueComponent() {
        return getComponent();
    }
}