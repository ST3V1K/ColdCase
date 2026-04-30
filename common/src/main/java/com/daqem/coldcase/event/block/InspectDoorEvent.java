package com.daqem.coldcase.event.block;

import com.daqem.coldcase.block.BlockHandler;
import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.event.AbstractEvent;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class InspectDoorEvent extends AbstractEvent {

    public static EventResult inspectDoor(ColdCaseServerPlayer player, Level level, BlockPos pos, BlockState state, boolean isInteraction) {
        List<BlockPos> positions = new ArrayList<>(List.of(pos));
        BlockHandler.getSecondDoorPosition(pos, state).ifPresent(positions::add);
        if (isInteraction) {
            Services.BLOCK.getInteractionHistoryAsync(
                    level,
                    positions,
                    player::coldcase$sendInspectMessage);
            return interrupt();
        } else {
            Services.BLOCK.getBlockHistoryAsync(
                    level,
                    positions,
                    player::coldcase$sendInspectMessage);
        }
        return interrupt();
    }
}
