package com.daqem.coldcase.item.criminal.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

public class CleanworkBoots extends ArmorItem implements CleansuitArmorItem {

    public CleanworkBoots(Properties props) {
        super(ArmorMaterials.LEATHER, Type.BOOTS, props);
    }
}
