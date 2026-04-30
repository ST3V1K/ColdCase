package com.daqem.coldcase.player;

import com.daqem.coldcase.command.page.Page;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.action.ItemAction;
import com.daqem.coldcase.model.history.IHistory;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface ColdCaseServerPlayer extends ColdCasePlayer {

    boolean coldcase$isInspecting();

    void coldcase$setInspecting(boolean inspecting);

    void coldcase$sendInspectMessage(List<IHistory> history);

    void coldcase$addItemToQueue(ItemAction action, SimpleItemStack itemStack);

    ServerPlayer coldcase$asServerPlayer();

    List<Page> coldcase$getPages();

    void coldcase$setPages(List<Page> pages);
}
