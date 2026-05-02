package com.daqem.coldcase.util;

import com.daqem.coldcase.item.criminal.armor.CleansuitArmorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CleansuitManager {

    public static byte getCleansuitArmorAsByte(Player player) {
        byte armorByte = 0;
        int i = 0;
        for (ItemStack armorItem : player.getArmorSlots()) {
            if (armorItem.getItem() instanceof CleansuitArmorItem) {
                armorByte |= (byte) (1 << i);
            }
            i++;
        }
        return armorByte;
    }

    public static float getHideClueChance(byte cleansuitArmor) {
        // TODO: Implement actual hide clue chance calculation
        int piecesWorn = Integer.bitCount(cleansuitArmor & 0b1111);
        return 0.2f + piecesWorn * 0.15f;
    }
}
