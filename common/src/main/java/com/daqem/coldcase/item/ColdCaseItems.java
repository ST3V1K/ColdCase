package com.daqem.coldcase.item;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.item.criminal.IdentityTheft;
import com.daqem.coldcase.item.criminal.armor.CleanworkArmorItem;
import com.daqem.coldcase.item.detective.MagnifyingGlass;
import com.daqem.coldcase.item.evidence.EvidenceItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

public class ColdCaseItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ColdCase.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> MAGNIFYING_GLASS = ITEMS.register("magnifying_glass", () -> new MagnifyingGlass(new Item.Properties().stacksTo(1)));
    public static final RegistrySupplier<Item> IDENTITY_THEFT = ITEMS.register("identity_theft", () -> new IdentityTheft(new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<Item> CLEANWORK_HELMET = ITEMS.register("cleanwork_helmet", () -> new CleanworkArmorItem(ArmorItem.Type.LEATHER, new Item.Properties()));
    public static final RegistrySupplier<Item> CLEANWORK_CHESTPLATE = ITEMS.register("cleanwork_chestplate", () -> new CleanworkArmorItem(ArmorItem.Type.LEATHER, new Item.Properties()));
    public static final RegistrySupplier<Item> CLEANWORK_LEGGINGS = ITEMS.register("cleanwork_leggings", () -> new CleanworkArmorItem(ArmorItem.Type.LEATHER, new Item.Properties()));
    public static final RegistrySupplier<Item> CLEANWORK_BOOTS = ITEMS.register("cleanwork_boots", () -> new CleanworkArmorItem(ArmorItem.Type.LEATHER, new Item.Properties()));

    public static final RegistrySupplier<Item> EVIDENCE_ITEM = ITEMS.register("evidence_item", () -> new EvidenceItem(new Item.Properties()));

    public static void init() {
        ITEMS.register();
    }
}
