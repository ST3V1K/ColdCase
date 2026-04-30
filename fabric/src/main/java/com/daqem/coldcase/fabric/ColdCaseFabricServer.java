package com.daqem.coldcase.fabric;

import com.daqem.coldcase.ColdCase;
import net.fabricmc.api.DedicatedServerModInitializer;

public class ColdCaseFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ColdCase.init();
    }
}
