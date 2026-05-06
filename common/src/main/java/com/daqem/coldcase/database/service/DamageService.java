package com.daqem.coldcase.database.service;

import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.database.repository.DamageRepository;
import com.daqem.coldcase.model.DamageLog;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class DamageService {

    private final DamageRepository damageRepository;

    public DamageService(Database database) {
        this.damageRepository = new DamageRepository(database);
    }

    public void createTable() {
        damageRepository.createTable();
    }

    public void createIndexes() {
        damageRepository.createIndexes();
    }

    public void insert(String type, UUID attackerUuid, String attackerType, UUID targetUuid, Vec3 attackerPos, Float attackerPitch, Float attackerYaw, Vec3 targetPos, Float targetPitch, Float targetYaw, Level level,
                       String attackerHead, String attackerChest, String attackerLegs, String attackerFeet, String attackerMainHand, String attackerOffHand,
                       String targetHead, String targetChest, String targetLegs, String targetFeet, String targetMainHand, String targetOffHand,
                       float damageAmount, float healthLeft) {
        damageRepository.insert(UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                type,
                attackerUuid != null ? attackerUuid.toString() : null,
                attackerType,
                targetUuid.toString(),
                attackerPos != null ? (float) attackerPos.x : null,
                attackerPos != null ? (float) attackerPos.y : null,
                attackerPos != null ? (float) attackerPos.z : null,
                attackerPitch,
                attackerYaw,
                (float) targetPos.x,
                (float) targetPos.y,
                (float) targetPos.z,
                targetPitch,
                targetYaw,
                level.dimension().location().toString(),
                attackerHead, attackerChest, attackerLegs, attackerFeet, attackerMainHand, attackerOffHand,
                targetHead, targetChest, targetLegs, targetFeet, targetMainHand, targetOffHand,
                damageAmount, healthLeft);
    }

    @Nullable
    public List<DamageLog> getDamageHistory(HolderLookup.Provider provider, @Nullable UUID playerUuid) {
        return damageRepository.getDamageHistory(provider, playerUuid, null, null);
    }

    @Nullable
    public List<DamageLog> getDamageHistory(HolderLookup.Provider provider, @Nullable UUID playerUuid, @Nullable Long startTime, @Nullable Long endTime) {
        return damageRepository.getDamageHistory(provider, playerUuid, startTime, endTime);
    }
}
