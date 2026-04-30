package com.daqem.coldcase.event.item;

import com.daqem.coldcase.event.AbstractEvent;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.action.ItemAction;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class DropItemEvent extends AbstractEvent {

    public static void onDropItem(Player player, ItemEntity itemEntity) {
        if (player instanceof ColdCaseServerPlayer serverPlayer) {
            serverPlayer.coldcase$addItemToQueue(ItemAction.DROP_ITEM, new SimpleItemStack(itemEntity.getItem()));
        }
    }
}