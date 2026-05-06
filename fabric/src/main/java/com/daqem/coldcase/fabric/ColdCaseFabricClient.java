package com.daqem.coldcase.fabric;

import com.daqem.coldcase.client.ColdCaseClient;
import net.fabricmc.api.ClientModInitializer;

public class ColdCaseFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ColdCaseClient.init();
    }
}
