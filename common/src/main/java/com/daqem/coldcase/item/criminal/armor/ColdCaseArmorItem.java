package com.daqem.coldcase.item.criminal.armor;

import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public abstract class ColdCaseArmorItem extends ArmorItem {

    protected ColdCaseArmorItem(Holder<ArmorMaterial> holder, Type type, Properties properties) {
        super(holder, type, properties);
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

    protected String getLoreKeyFormat() {
        return "item.coldcase.cleanwork_armor.lore.%d";
    }
}
