package com.daqem.coldcase.fabric;

import com.daqem.coldcase.ColdCaseExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ColdCaseExpectPlatformImpl {
    /**
     * This is our actual method to {@link ColdCaseExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
