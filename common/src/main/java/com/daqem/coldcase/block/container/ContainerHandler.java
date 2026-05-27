package com.daqem.coldcase.block.container;

import com.daqem.coldcase.model.SimpleItemStack;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContainerHandler {

    public static boolean hasContainer(BlockEntity blockEntity) {
        return blockEntity instanceof BaseContainerBlockEntity;
    }

    public static Optional<BaseContainerBlockEntity> getContainer(BlockEntity blockEntity) {
        if (hasContainer(blockEntity)) {
            return Optional.of((BaseContainerBlockEntity) blockEntity);
        }
        return getContainers(blockEntity).map(list -> list.get(0));
    }

    public static boolean hasContainer(MenuProvider menuProvider) {
        return menuProvider instanceof BaseContainerBlockEntity || getContainers(menuProvider).isPresent();
    }

    public static Optional<BaseContainerBlockEntity> getContainer(MenuProvider menuProvider) {
        if (menuProvider instanceof BaseContainerBlockEntity) {
            return Optional.of((BaseContainerBlockEntity) menuProvider);
        }
        return getContainers(menuProvider).map(list -> list.get(0));
    }

    public static Optional<List<BaseContainerBlockEntity>> getContainers(Object object) {
        List<BaseContainerBlockEntity> containers = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            if (BaseContainerBlockEntity.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    containers.add((BaseContainerBlockEntity) field.get(object));
                } catch (IllegalAccessException e) {
                    com.daqem.coldcase.ColdCase.LOGGER.error("Failed to access field: {}", field.getName(), e);
                }
            }
        }

        return containers.isEmpty() ? Optional.empty() : Optional.of(containers);
    }

    public static List<SimpleItemStack> getContainerItems(BaseContainerBlockEntity containerBlockEntity) {
        List<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i < containerBlockEntity.getContainerSize(); i++) {
            itemStacks.add(containerBlockEntity.getItem(i));
        }
        return itemStacks.stream()
                .filter(itemStack -> !itemStack.isEmpty() && itemStack.getItem() != Items.AIR)
                .map(SimpleItemStack::new).toList();
    }
}