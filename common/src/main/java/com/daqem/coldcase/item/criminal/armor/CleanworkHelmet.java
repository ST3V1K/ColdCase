package com.daqem.coldcase.item.criminal.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

public class CleanworkHelmet extends ArmorItem implements CleansuitArmorItem {

    public CleanworkHelmet(Properties props) {
        super(ArmorMaterials.LEATHER, Type.HELMET, props);
    }
}
