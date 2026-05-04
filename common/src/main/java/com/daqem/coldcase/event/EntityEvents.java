package com.daqem.coldcase.event;

import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.model.action.BlockAction;
import com.daqem.coldcase.util.CleanworkManager;
import com.daqem.coldcase.util.ItemStackSerializer;
import com.daqem.coldcase.util.SkinColorManager;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class EntityEvents {

    public static void registerEvents() {
        EntityEvent.LIVING_DEATH.register((entity, source) -> {
            if (source.getEntity() instanceof ServerPlayer serverPlayer) {
                ResourceLocation entityLocation = entity.getType().arch$registryName();
                if (entityLocation != null) {
                    String tool = BuiltInRegistries.ITEM.getKey(serverPlayer.getMainHandItem()
                            .getItem()).toString();
                    String skinColor = SkinColorManager.getSkinColor(serverPlayer);
                    byte cleanworkArmor = CleanworkManager.getCleanworkArmorAsByte(serverPlayer);

                    Services.BLOCK.insertEntity(
                            serverPlayer.getUUID(),
                            entity.level().dimension().location().toString(),
                            entity.blockPosition(),
                            entityLocation.toString(),
                            BlockAction.KILL_ENTITY,
                            tool,
                            skinColor,
                            cleanworkArmor
                    );
                }
            }
            return EventResult.pass();
        });

        EntityEvent.LIVING_HURT.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayer targetPlayer) {
                UUID attackerUuid = null;
                String attackerType = null;
                Vec3 attackerPos = null;
                Float attackerPitch = null;
                Float attackerYaw = null;

                String attackerHead = null;
                String attackerChest = null;
                String attackerLegs = null;
                String attackerFeet = null;
                String attackerMainHand = null;
                String attackerOffHand = null;

                if (source.getEntity() != null) {
                    attackerUuid = source.getEntity().getUUID();
                    attackerType = source.getEntity().getType().arch$registryName().toString();
                    attackerPos = source.getEntity().position();
                    attackerPitch = source.getEntity().getXRot();
                    attackerYaw = source.getEntity().getYRot();

                    if (source.getEntity() instanceof LivingEntity attacker) {
                        RegistryAccess registryAccess = attacker.level().registryAccess();
                        attackerHead = ItemStackSerializer.serialize(attacker.getItemBySlot(EquipmentSlot.HEAD), registryAccess);
                        attackerChest = ItemStackSerializer.serialize(attacker.getItemBySlot(EquipmentSlot.CHEST), registryAccess);
                        attackerLegs = ItemStackSerializer.serialize(attacker.getItemBySlot(EquipmentSlot.LEGS), registryAccess);
                        attackerFeet = ItemStackSerializer.serialize(attacker.getItemBySlot(EquipmentSlot.FEET), registryAccess);
                        attackerMainHand = ItemStackSerializer.serialize(attacker.getItemBySlot(EquipmentSlot.MAINHAND), registryAccess);
                        attackerOffHand = ItemStackSerializer.serialize(attacker.getItemBySlot(EquipmentSlot.OFFHAND), registryAccess);
                    }
                }

                RegistryAccess registryAccess = targetPlayer.level().registryAccess();
                String targetHead = ItemStackSerializer.serialize(targetPlayer.getItemBySlot(EquipmentSlot.HEAD), registryAccess);
                String targetChest = ItemStackSerializer.serialize(targetPlayer.getItemBySlot(EquipmentSlot.CHEST), registryAccess);
                String targetLegs = ItemStackSerializer.serialize(targetPlayer.getItemBySlot(EquipmentSlot.LEGS), registryAccess);
                String targetFeet = ItemStackSerializer.serialize(targetPlayer.getItemBySlot(EquipmentSlot.FEET), registryAccess);
                String targetMainHand = ItemStackSerializer.serialize(targetPlayer.getItemBySlot(EquipmentSlot.MAINHAND), registryAccess);
                String targetOffHand = ItemStackSerializer.serialize(targetPlayer.getItemBySlot(EquipmentSlot.OFFHAND), registryAccess);

                float healthLeft = Math.max(0, targetPlayer.getHealth() - amount);

                Services.DAMAGE.insert(
                        source.getMsgId(),
                        attackerUuid,
                        attackerType,
                        targetPlayer.getUUID(),
                        attackerPos,
                        attackerPitch,
                        attackerYaw,
                        targetPlayer.position(),
                        targetPlayer.getXRot(),
                        targetPlayer.getYRot(),
                        targetPlayer.level(),
                        attackerHead,
                        attackerChest,
                        attackerLegs,
                        attackerFeet,
                        attackerMainHand,
                        attackerOffHand,
                        targetHead,
                        targetChest,
                        targetLegs,
                        targetFeet,
                        targetMainHand,
                        targetOffHand,
                        amount,
                        healthLeft
                );
            }
            return EventResult.pass();
        });
    }
}