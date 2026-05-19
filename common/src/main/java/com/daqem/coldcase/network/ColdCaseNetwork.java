package com.daqem.coldcase.network;

import com.daqem.coldcase.network.packet.ClueUpdatePacket;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ColdCaseNetwork {

    public static void register() {
        NetworkManager.registerS2CPayloadType(ClueUpdatePacket.TYPE, ClueUpdatePacket.STREAM_CODEC);
    }

    public static void sendClueUpdate(ServerPlayer player, UUID messageId, Component component) {
        NetworkManager.sendToPlayer(player, new ClueUpdatePacket(messageId, component));
    }
}