package com.daqem.coldcase.block.container;

import net.minecraft.server.level.ServerPlayer;

public interface IContainerTransactionManager {
    void finalize(ServerPlayer serverPlayer);
}
