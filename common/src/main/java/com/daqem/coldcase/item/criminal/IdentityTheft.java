package com.daqem.coldcase.item.criminal;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IdentityTheft extends Item {

    String LORE_KEY_FORMAT = "item.coldcase.identity_theft.lore.%d";

    public IdentityTheft(Properties props) {
        super(props);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    @Nullable
    public SoundEvent getEatingSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        Language language = Language.getInstance();
        for (int i = 1; language.has(LORE_KEY_FORMAT.formatted(i)); i++) {
            tooltip.add(Component.translatable(LORE_KEY_FORMAT.formatted(i)));
        }
    }
}
