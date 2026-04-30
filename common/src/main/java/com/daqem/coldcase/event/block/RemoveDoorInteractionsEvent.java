package com.daqem.coldcase.event.block;

import com.daqem.coldcase.block.BlockHandler;
import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.event.AbstractEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class RemoveDoorInteractionsEvent extends AbstractEvent {

    public static void removeDoorInteractions(Level level, BlockPos pos, BlockState state) {
        List<BlockPos> positions = new ArrayList<>(List.of(pos));
        BlockHandler.getSecondDoorPosition(pos, state).ifPresent(positions::add);
        for (BlockPos position : positions) {
            Services.BLOCK.removeInteractionsForPosition(level, position);
        }
    }
}
