package com.daqem.coldcase.model;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;

public record DamageLog(UUID id, long time, String type, UUID attackerUuid, String attackerName, String attackerType,
                        UUID targetUuid, String targetName, Vec3 attackerPos, float attackerPitch,
                        float attackerYaw, Vec3 targetPos, float targetPitch, float targetYaw,
                        String levelName, Map<EquipmentSlot, ItemStack> attackerEquipment,
                        Map<EquipmentSlot, ItemStack> targetEquipment, float damageAmount, float healthLeft) {
}
