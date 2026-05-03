package com.daqem.coldcase.item;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public abstract class ColdCaseItem extends Item {

    protected ColdCaseItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);

        String loreKey = getLoreKeyFormat();
        Language language = Language.getInstance();

        String key;
        for (int i = 1; language.has(key = loreKey.formatted(i)); i++) {
            tooltip.add(Component.translatable(key));
        }
    }

    protected abstract String getLoreKeyFormat();
}
