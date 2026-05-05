package com.daqem.coldcase.fabric;

import com.daqem.coldcase.ColdCase;
import net.fabricmc.api.ModInitializer;

public class ColdCaseFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ColdCase.init();
    }
}
