package com.daqem.coldcase.neoforge;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.neoforge.item.ColdCaseNeoForgeItems;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ColdCase.MOD_ID)
public class ColdCaseNeoForge {

    public ColdCaseNeoForge(IEventBus modEventBus) {
        ColdCase.init();
        ColdCaseNeoForgeItems.initialize(modEventBus);

        EnvExecutor.getEnvSpecific(
                () -> SideProxyNeoForge.Client::new,
                () -> SideProxyNeoForge.Server::new
        );
    }
}
