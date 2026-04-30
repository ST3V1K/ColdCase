package com.daqem.coldcase.event.item;

import com.daqem.coldcase.event.AbstractEvent;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.action.ItemAction;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PickupItemEvent extends AbstractEvent {

    public static void onPickupItem(Player player, ItemEntity itemEntity, ItemStack itemStack) {
        if (player instanceof ColdCaseServerPlayer serverPlayer) {
            serverPlayer.coldcase$addItemToQueue(ItemAction.PICKUP_ITEM, new SimpleItemStack(itemStack));
        }
    }
}
