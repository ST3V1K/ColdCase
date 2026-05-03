package com.daqem.coldcase.item.criminal;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class IdentityTheft extends Item {

    String LORE_KEY_FORMAT = "item.coldcase.identity_theft.lore.%d";

    public IdentityTheft(Properties props) {
        super(props);
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
