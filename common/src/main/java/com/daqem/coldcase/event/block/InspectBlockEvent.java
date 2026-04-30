package com.daqem.coldcase.event.block;

import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.event.AbstractEvent;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;

public class InspectBlockEvent extends AbstractEvent {

    public static EventResult inspectBlock(ColdCaseServerPlayer player, BlockPos pos) {
        Services.BLOCK.getBlockHistoryAsync(
                player.coldcase$asServerPlayer().level(),
                pos,
                player::coldcase$sendInspectMessage);
        return interrupt();
    }
}
