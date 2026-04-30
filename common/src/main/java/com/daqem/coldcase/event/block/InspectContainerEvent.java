package com.daqem.coldcase.event.block;

import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.event.AbstractEvent;
import com.daqem.coldcase.model.history.IHistory;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import com.daqem.coldcase.thread.ThreadManager;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class InspectContainerEvent extends AbstractEvent {

    public static EventResult inspectContainer(ColdCaseServerPlayer serverPlayer, Level level, BlockPos pos) {
        ThreadManager.submit(() -> {
            List<IHistory> history = new ArrayList<>();
            List<IHistory> containerHistory = Services.CONTAINER.getHistory(
                    level,
                    pos);
            List<IHistory> interactionHistory = Services.BLOCK.getInteractionHistory(
                    level,
                    pos
            );
            history.addAll(containerHistory);
            history.addAll(interactionHistory);
            history.sort((a, b) -> Long.compare(b.getTime().time(), a.getTime().time()));
            return history;
        }, serverPlayer::coldcase$sendInspectMessage);
        return interrupt();
    }

    public static EventResult inspectContainers(ColdCaseServerPlayer serverPlayer, Level level, BlockPos pos, BlockPos connectionPos) {
        ThreadManager.submit(() -> {
            List<IHistory> history = new ArrayList<>();
            List<IHistory> containerHistory = Services.CONTAINER.getHistory(
                    level,
                    pos,
                    connectionPos);
            List<IHistory> interactionHistory = Services.BLOCK.getInteractionHistory(
                    level,
                    List.of(pos, connectionPos)
            );
            history.addAll(containerHistory);
            history.addAll(interactionHistory);
            history.sort((a, b) -> Long.compare(b.getTime().time(), a.getTime().time()));
            return history;
        }, serverPlayer::coldcase$sendInspectMessage);
        return interrupt();
    }
}
