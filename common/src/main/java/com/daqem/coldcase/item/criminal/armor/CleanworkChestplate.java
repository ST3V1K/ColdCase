package com.daqem.coldcase.item.criminal.armor;

import net.minecraft.world.item.ArmorMaterials;

public class CleanworkChestplate extends ColdCaseArmorItem implements CleanworkArmorItem {

    public CleanworkChestplate(Properties props) {
        super(ArmorMaterials.LEATHER, Type.CHESTPLATE, props);
    }
}
