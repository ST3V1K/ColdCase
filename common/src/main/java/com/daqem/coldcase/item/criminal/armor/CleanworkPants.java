package com.daqem.coldcase.item.criminal.armor;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CleanworkPants extends ArmorItem implements CleansuitArmorItem {

    String LORE_KEY_FORMAT = "item.coldcase.cleanwork_armor.lore.%d";

    public CleanworkPants(Properties props) {
        super(ArmorMaterials.LEATHER, Type.LEGGINGS, props);
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
