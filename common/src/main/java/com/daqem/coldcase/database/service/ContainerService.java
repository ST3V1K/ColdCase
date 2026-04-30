package com.daqem.coldcase.database.service;

import com.daqem.coldcase.command.filter.FilterList;
import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.ContainerRepository;
import com.daqem.coldcase.model.SimpleItemStack;
import com.daqem.coldcase.model.action.ItemAction;
import com.daqem.coldcase.model.history.IHistory;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ContainerService {

    private final ContainerRepository containerRepository;

    public ContainerService(Database database) {
        this.containerRepository = new ContainerRepository(database);
    }

    public void createTable() {
        containerRepository.createTable();
    }

    public void createIndexes() {
        containerRepository.createIndexes();
    }

    public void insert(UUID userUuid, Level level, BlockPos pos, SimpleItemStack item, ItemAction itemAction) {
        ResourceLocation itemLocation = item.getItem().arch$registryName();
        if (itemLocation != null) {
            containerRepository.insert(System.currentTimeMillis(),
                    userUuid.toString(),
                    level,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    item,
                    itemAction.getId());
        }
    }

    public void insertList(UUID userUuid, Level level, BlockPos pos, List<SimpleItemStack> items, ItemAction itemAction) {
        containerRepository.insertList(System.currentTimeMillis(),
                userUuid.toString(),
                level,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                items,
                itemAction.getId());
    }

    public void insertMap(UUID userUuid, Level level, BlockPos pos, Map<ItemAction, List<SimpleItemStack>> itemsMap) {
        containerRepository.insertMap(System.currentTimeMillis(),
                userUuid.toString(),
                level,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                itemsMap);
    }

    public List<IHistory> getHistory(Level level, BlockPos pos) {
        return containerRepository.getHistory(
                level,
                pos.getX(),
                pos.getY(),
                pos.getZ()
        );
    }

    public List<IHistory> getHistory(Level level, BlockPos pos, BlockPos connectionPos) {
        return containerRepository.getHistory(
                level,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                connectionPos.getX(),
                connectionPos.getY(),
                connectionPos.getZ()
        );
    }

    public List<IHistory> getFilteredContainerHistory(Level level, FilterList filterList) {
        return containerRepository.getFilteredContainerHistory(
                level,
                filterList
        );
    }
}
