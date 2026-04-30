package com.daqem.coldcase.event.item;

import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.action.ItemAction;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ShootItemEvent {
    public static void shootItem(Player player, ItemStack itemStack) {
        if (player instanceof ColdCaseServerPlayer serverPlayer) {
            serverPlayer.coldcase$addItemToQueue(ItemAction.SHOOT_ITEM, new SimpleItemStack(itemStack));
        }
    }
}
