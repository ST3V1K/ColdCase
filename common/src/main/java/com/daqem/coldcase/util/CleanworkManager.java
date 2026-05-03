package com.daqem.coldcase.util;

import com.daqem.coldcase.item.criminal.armor.CleanworkArmorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CleanworkManager {

    public static byte getCleanworkArmorAsByte(Player player) {
        byte armorByte = 0;
        int i = 0;
        for (ItemStack armorItem : player.getArmorSlots()) {
            if (armorItem.getItem() instanceof CleanworkArmorItem) {
                armorByte |= (byte) (1 << i);
            }
            i++;
        }
        return armorByte;
    }

    public static float getHideClueChance(byte cleanworkArmor) {
        // TODO: Implement actual hide clue chance calculation
        int piecesWorn = Integer.bitCount(cleanworkArmor & 0b1111);
        return 0.2f + piecesWorn * 0.15f;
    }
}
