package com.daqem.coldcase.neoforge.item;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.item.criminal.IdentityTheft;
import com.daqem.coldcase.item.criminal.armor.CleanworkBoots;
import com.daqem.coldcase.item.criminal.armor.CleanworkChestplate;
import com.daqem.coldcase.item.criminal.armor.CleanworkHelmet;
import com.daqem.coldcase.item.criminal.armor.CleanworkPants;
import com.daqem.coldcase.item.detective.AutopsyKit;
import com.daqem.coldcase.item.detective.MagnifyingGlass;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ColdCaseNeoForgeItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ColdCase.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ColdCase.MOD_ID);

    public static final DeferredItem<MagnifyingGlass> MAGNIFYING_GLASS = ITEMS.registerItem(
            "magnifying_glass",
            MagnifyingGlass::new,
            new Item.Properties().durability(10)
    );

    public static final DeferredItem<AutopsyKit> AUTOPSY_KIT = ITEMS.registerItem(
            "autopsy_kit",
            AutopsyKit::new,
            new Item.Properties().durability(1)
    );

    public static final DeferredItem<IdentityTheft> IDENTITY_THEFT = ITEMS.registerItem(
            "identity_theft",
            IdentityTheft::new,
            new Item.Properties().durability(1)
    );

    public static final DeferredItem<CleanworkHelmet> CLEANWORK_HELMET = ITEMS.registerItem(
            "cleanwork_helmet",
            CleanworkHelmet::new,
            new Item.Properties().durability(100)
    );

    public static final DeferredItem<CleanworkChestplate> CLEANWORK_CHESTPLATE = ITEMS.registerItem(
            "cleanwork_chestplate",
            CleanworkChestplate::new,
            new Item.Properties().durability(100)
    );

    public static final DeferredItem<CleanworkPants> CLEANWORK_PANTS = ITEMS.registerItem(
            "cleanwork_pants",
            CleanworkPants::new,
            new Item.Properties().durability(100)
    );

    public static final DeferredItem<CleanworkBoots> CLEANWORK_BOOTS = ITEMS.registerItem(
            "cleanwork_boots",
            CleanworkBoots::new,
            new Item.Properties().durability(100)
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEM_GROUP = CREATIVE_MODE_TABS.register(ColdCase.MOD_ID, () -> CreativeModeTab.builder()
            .title(Component.translatable("coldcase.name"))
            .icon(() -> new ItemStack(MAGNIFYING_GLASS.get()))
            .displayItems((params, output) -> {
                output.accept(MAGNIFYING_GLASS.get());
                output.accept(AUTOPSY_KIT.get());
                output.accept(IDENTITY_THEFT.get());
                output.accept(CLEANWORK_HELMET.get());
                output.accept(CLEANWORK_CHESTPLATE.get());
                output.accept(CLEANWORK_PANTS.get());
                output.accept(CLEANWORK_BOOTS.get());
            })
            .build()
    );

    public static void initialize(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
