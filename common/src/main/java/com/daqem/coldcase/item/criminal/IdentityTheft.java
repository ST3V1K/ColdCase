package com.daqem.coldcase.item.criminal;

import com.daqem.coldcase.item.ColdCaseItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class IdentityTheft extends ColdCaseItem {

    public IdentityTheft(Properties props) {
        super(props);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    public String getLoreKeyFormat() {
        return "item.coldcase.identity_theft.lore.%d";
    }
}
