package com.daqem.coldcase.item.criminal.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

public class CleanworkChestplate extends ArmorItem implements CleansuitArmorItem {

    public CleanworkChestplate(Properties props) {
        super(ArmorMaterials.LEATHER, Type.CHESTPLATE, props);
    }
}
