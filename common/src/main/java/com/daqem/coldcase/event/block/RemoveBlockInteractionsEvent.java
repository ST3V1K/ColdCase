package com.daqem.coldcase.event.block;

import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.event.AbstractEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class RemoveBlockInteractionsEvent extends AbstractEvent {

    public static void removeBlockInteractions(Level level, BlockPos pos) {
        Services.BLOCK.removeInteractionsForPosition(level, pos);
    }
}
