package com.daqem.coldcase.neoforge;

import com.daqem.coldcase.ColdCaseExpectPlatform;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ColdCaseExpectPlatformImpl {
    /**
     * This is our actual method to {@link ColdCaseExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
