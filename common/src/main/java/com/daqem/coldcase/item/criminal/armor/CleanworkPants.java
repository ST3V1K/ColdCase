package com.daqem.coldcase.item.criminal.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

public class CleanworkPants extends ArmorItem implements CleansuitArmorItem {

    public CleanworkPants(Properties props) {
        super(ArmorMaterials.LEATHER, Type.LEGGINGS, props);
    }
}
