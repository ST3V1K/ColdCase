package com.daqem.coldcase.database.service;

import com.daqem.coldcase.command.filter.FilterList;
import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.ItemRepository;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.action.ItemAction;
import com.daqem.coldcase.model.history.ItemHistory;
import com.daqem.coldcase.util.CleanworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(Database database) {
        this.itemRepository = new ItemRepository(database);
    }

    public void createTable() {
        itemRepository.createTable();
    }

    public void createIndexes() {
        itemRepository.createIndexes();
    }

    public void insert(Player player, BlockPos pos, SimpleItemStack item, ItemAction itemAction) {
        ResourceLocation itemLocation = item.getItem().arch$registryName();
        if (itemLocation != null) {
            itemRepository.insert(System.currentTimeMillis(),
                    player.getUUID().toString(),
                    player.level(),
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    item,
                    itemAction.getId(),
                    CleanworkManager.getCleanworkArmorAsByte(player));
        }
    }

    public void insertMap(Player player, BlockPos pos, Map<ItemAction, List<SimpleItemStack>> itemsMap) {
        itemRepository.insertMap(System.currentTimeMillis(),
                player.getUUID().toString(),
                player.level(),
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                itemsMap,
                CleanworkManager.getCleanworkArmorAsByte(player));
    }

    public List<ItemHistory> getFilteredItemHistory(Level level, FilterList filterList) {
        return itemRepository.getFilteredItemHistory(
                level,
                filterList
        );
    }
}
