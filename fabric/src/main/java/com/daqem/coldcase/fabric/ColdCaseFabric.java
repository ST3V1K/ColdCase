package com.daqem.coldcase.fabric;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.fabric.item.ColdCaseFabricItems;
import net.fabricmc.api.ModInitializer;

public class ColdCaseFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ColdCase.init();
        ColdCaseFabricItems.initialize();
    }
}
