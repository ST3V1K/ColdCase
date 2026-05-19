package com.daqem.coldcase.client;

import com.daqem.coldcase.client.chat.IColdCaseChatComponent;
import com.daqem.coldcase.client.entity.renderer.DeadBodyRenderer;
import com.daqem.coldcase.entity.ColdCaseEntities;
import com.daqem.coldcase.network.packet.ClueUpdatePacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.minecraft.client.Minecraft;

public class ColdCaseClient {

    public static void init() {
        EntityRendererRegistry.register(ColdCaseEntities.DEAD_BODY, DeadBodyRenderer::new);
        registerPacketReceivers();
    }

    // -------------------------------------------------------------------------
    // Packet receivers (client-side)
    // -------------------------------------------------------------------------

    private static void registerPacketReceivers() {
        // S2C: ClueUpdate – add or in-place replace a clue chat message.
        //
        // Payload (written by ColdCaseNetwork.sendClueUpdate):
        //   UUID      messageId  – stable identifier for this clue slot in chat
        //   Component component  – new content (clue text + navigation footer)
        NetworkManager.registerReceiver(
                NetworkManager.Side.S2C,
                ClueUpdatePacket.TYPE,
                ClueUpdatePacket.STREAM_CODEC,
                (packet, context) -> {
                    // Defer to the main thread – ChatComponent is not thread-safe.
                    context.queue(() -> {
                        Minecraft mc = Minecraft.getInstance();
                        // MixinChatComponent implements IColdCaseChatComponent,
                        // so this cast is always valid at runtime.
                        IColdCaseChatComponent chat =
                                (IColdCaseChatComponent) mc.gui.getChat();
                        chat.coldcase$addOrReplaceMessage(packet.messageId(), packet.component());
                    });
                }
        );
    }
}