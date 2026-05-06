package com.daqem.coldcase.client;

import com.daqem.coldcase.client.entity.renderer.DeadBodyRenderer;
import com.daqem.coldcase.entity.ColdCaseEntities;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;

public class ColdCaseClient {

    public static void init() {
        EntityRendererRegistry.register(ColdCaseEntities.DEAD_BODY, DeadBodyRenderer::new);
    }
}
