package com.daqem.coldcase.neoforge;

import com.daqem.coldcase.ColdCase;
import dev.architectury.utils.EnvExecutor;
import net.neoforged.fml.common.Mod;

@Mod(ColdCase.MOD_ID)
public class ColdCaseNeoForge {

    public ColdCaseNeoForge() {
        EnvExecutor.getEnvSpecific(
                () -> SideProxyNeoForge.Client::new,
                () -> SideProxyNeoForge.Server::new
        );
    }
}
