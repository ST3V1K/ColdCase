package com.daqem.coldcase.event.block;

import com.daqem.coldcase.database.service.Services;
import com.daqem.coldcase.event.AbstractEvent;
import com.daqem.coldcase.model.action.BlockAction;
import com.daqem.coldcase.player.ColdCaseServerPlayer;
import com.daqem.coldcase.util.SkinColorManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LogBlockEvent extends AbstractEvent {

    public static void logBlock(ColdCaseServerPlayer player, Level level, BlockState state, BlockPos pos, BlockAction blockAction) {
        ResourceLocation materialLocation = state.getBlock().arch$registryName();
        if (materialLocation != null) {
            ServerPlayer serverPlayer = player.coldcase$asServerPlayer();
            String tool = BuiltInRegistries.ITEM.getKey(serverPlayer.getMainHandItem().getItem()).toString();
            String skinColor = SkinColorManager.getSkinColor(serverPlayer);
            
            Services.BLOCK.insertMaterial(
                    serverPlayer.getUUID(),
                    level.dimension().location().toString(),
                    pos,
                    materialLocation.toString(),
                    blockAction,
                    tool,
                    skinColor);
        }
    }
}
