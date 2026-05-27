package com.daqem.coldcase.model.history;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.IAction;
import net.minecraft.core.component.DataComponentPatch;

import java.util.UUID;

public class ContainerHistory extends History {

    private final BlockPosition position;
    private final SimpleItemStack itemStack;
    private final IAction action;
    private final byte cleanworkArmor;

    public ContainerHistory(long time, String name, String uuid, int x, int y, int z, String material, DataComponentPatch data, int amount, int action, byte cleanworkArmor) {
        this(new Time(time), new User(name, UUID.fromString(uuid)), new BlockPosition(x, y, z), new SimpleItemStack(material, amount, data), com.daqem.coldcase.model.action.ItemAction.fromId(action), cleanworkArmor);
    }

    public ContainerHistory(Time time, User user, BlockPosition position, SimpleItemStack itemStack, IAction action, byte cleanworkArmor) {
        super(time, user, position, action);
        this.position = position;
        this.itemStack = itemStack;
        this.action = action;
        this.cleanworkArmor = cleanworkArmor;
    }

    public BlockPosition getPosition() {
        return position;
    }

    public SimpleItemStack getItemStack() {
        return itemStack;
    }

    public IAction getAction() {
        return action;
    }

    public byte getCleanworkArmor() {
        return cleanworkArmor;
    }
}