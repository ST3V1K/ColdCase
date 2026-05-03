package com.daqem.coldcase.model.history;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.IAction;
import net.minecraft.core.component.DataComponentPatch;

public class ContainerHistory extends ItemHistory {

    public ContainerHistory(long time, String name, String uuid, int x, int y, int z, String material, DataComponentPatch data, int amount, int action, byte cleanworkArmor) {
        super(time, name, uuid, x, y, z, material, data, amount, action, cleanworkArmor);
    }

    public ContainerHistory(Time time, User user, BlockPosition position, SimpleItemStack itemStack, IAction action, byte cleanworkArmor) {
        super(time, user, position, itemStack, action, cleanworkArmor);
    }
}
