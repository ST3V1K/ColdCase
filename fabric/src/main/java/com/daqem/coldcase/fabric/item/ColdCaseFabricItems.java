package com.daqem.coldcase.fabric.item;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.item.criminal.IdentityTheft;
import com.daqem.coldcase.item.criminal.armor.CleanworkBoots;
import com.daqem.coldcase.item.criminal.armor.CleanworkChestplate;
import com.daqem.coldcase.item.criminal.armor.CleanworkHelmet;
import com.daqem.coldcase.item.criminal.armor.CleanworkPants;
import com.daqem.coldcase.item.detective.AutopsyKit;
import com.daqem.coldcase.item.detective.MagnifyingGlass;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ColdCaseFabricItems {

    private static final ResourceKey<CreativeModeTab> CUSTOM_ITEM_GROUP_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ResourceLocation.fromNamespaceAndPath(ColdCase.MOD_ID, "item_group"));

    private static <T extends Item> T register(T item, String id) {
        ResourceLocation itemId = ResourceLocation.fromNamespaceAndPath(ColdCase.MOD_ID, id);
        return Registry.register(BuiltInRegistries.ITEM, itemId, item);
    }

    public static final MagnifyingGlass MAGNIFYING_GLASS = register(
            new MagnifyingGlass(new Item.Properties().durability(10)),
            "magnifying_glass"
    );

    public static final AutopsyKit AUTOPSY_KIT = register(
            new AutopsyKit(new Item.Properties().durability(1)),
            "autopsy_kit"
    );

    public static final IdentityTheft IDENTITY_THEFT = register(
            new IdentityTheft(new Item.Properties().durability(1)),
            "identity_theft"
    );

    public static final CleanworkHelmet CLEANWORK_HELMET = register(
            new CleanworkHelmet(new Item.Properties().durability(100)),
            "cleanwork_helmet"
    );

    public static final CleanworkChestplate CLEANWORK_CHESTPLATE = register(
            new CleanworkChestplate(new Item.Properties().durability(100)),
            "cleanwork_chestplate"
    );

    public static final CleanworkPants CLEANWORK_PANTS = register(
            new CleanworkPants(new Item.Properties().durability(100)),
            "cleanwork_pants"
    );

    public static final CleanworkBoots CLEANWORK_BOOTS = register(
            new CleanworkBoots(new Item.Properties().durability(100)),
            "cleanwork_boots"
    );

    public static final CreativeModeTab ITEM_GROUP = FabricItemGroup.builder()
            .title(Component.translatable("coldcase.name"))
            .icon(() -> new ItemStack(MAGNIFYING_GLASS))
            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_ITEM_GROUP_KEY, ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.accept(MAGNIFYING_GLASS);
            itemGroup.accept(AUTOPSY_KIT);
            itemGroup.accept(IDENTITY_THEFT);
            itemGroup.accept(CLEANWORK_HELMET);
            itemGroup.accept(CLEANWORK_CHESTPLATE);
            itemGroup.accept(CLEANWORK_PANTS);
            itemGroup.accept(CLEANWORK_BOOTS);
        });
    }
}
