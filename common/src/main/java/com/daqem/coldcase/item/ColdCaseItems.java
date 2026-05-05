package com.daqem.coldcase.item;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.item.criminal.IdentityTheft;
import com.daqem.coldcase.item.criminal.armor.CleanworkBoots;
import com.daqem.coldcase.item.criminal.armor.CleanworkChestplate;
import com.daqem.coldcase.item.criminal.armor.CleanworkHelmet;
import com.daqem.coldcase.item.criminal.armor.CleanworkPants;
import com.daqem.coldcase.item.detective.AutopsyKit;
import com.daqem.coldcase.item.detective.MagnifyingGlass;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ColdCaseItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ColdCase.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(ColdCase.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> COLD_CASE_TAB = TABS.register("coldcase", () ->
            CreativeTabRegistry.create(
                    Component.translatable("coldcase.name"),
                    () -> new ItemStack(ColdCaseItems.MAGNIFYING_GLASS.get())));

    public static final RegistrySupplier<Item> MAGNIFYING_GLASS = ITEMS.register("magnifying_glass",
            () -> new MagnifyingGlass(new Item.Properties().stacksTo(1).arch$tab(COLD_CASE_TAB)));
    public static final RegistrySupplier<Item> AUTOPSY_KIT = ITEMS.register("autopsy_kit",
            () -> new AutopsyKit(new Item.Properties().stacksTo(1).arch$tab(COLD_CASE_TAB)));
    public static final RegistrySupplier<Item> IDENTITY_THEFT = ITEMS.register("identity_theft",
            () -> new IdentityTheft(new Item.Properties().stacksTo(1)
                    .food(new FoodProperties.Builder().alwaysEdible().build())
                    .arch$tab(COLD_CASE_TAB)));

    public static final RegistrySupplier<Item> CLEANWORK_HELMET = ITEMS.register("cleanwork_helmet",
            () -> new CleanworkHelmet(new Item.Properties().durability(100)
                    .arch$tab(COLD_CASE_TAB)));
    public static final RegistrySupplier<Item> CLEANWORK_CHESTPLATE = ITEMS.register("cleanwork_chestplate",
            () -> new CleanworkChestplate(new Item.Properties().durability(100)
                    .arch$tab(COLD_CASE_TAB)));
    public static final RegistrySupplier<Item> CLEANWORK_PANTS = ITEMS.register("cleanwork_pants",
            () -> new CleanworkPants(new Item.Properties().durability(100)
                    .arch$tab(COLD_CASE_TAB)));
    public static final RegistrySupplier<Item> CLEANWORK_BOOTS = ITEMS.register("cleanwork_boots",
            () -> new CleanworkBoots(new Item.Properties().durability(100)
                    .arch$tab(COLD_CASE_TAB)));

    public static void init() {
        ITEMS.register();
        TABS.register();
    }
}
