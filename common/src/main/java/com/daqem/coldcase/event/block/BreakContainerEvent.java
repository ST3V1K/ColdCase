package com.daqem.coldcase.event.block;

import com.daqem.coldcase.block.container.ContainerHandler;
import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.action.ItemAction;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

import java.util.List;

public class BreakContainerEvent {

    public static void breakContainer(ColdCaseServerPlayer player, Level level, BlockPos pos, BaseContainerBlockEntity containerBlockEntity) {
        List<SimpleItemStack> itemStacks = ContainerHandler.getContainerItems(containerBlockEntity);
        Services.CONTAINER.insertList(
                player.coldcase$asServerPlayer().getUUID(),
                level,
                pos,
                itemStacks,
                ItemAction.REMOVE_ITEM);
    }
}
