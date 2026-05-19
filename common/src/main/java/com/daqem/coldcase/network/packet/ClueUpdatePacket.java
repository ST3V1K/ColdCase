
package com.daqem.coldcase.network.packet;

import com.daqem.coldcase.ColdCase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record ClueUpdatePacket(UUID messageId, Component component) implements CustomPacketPayload {

    public static final Type<ClueUpdatePacket> TYPE = new Type<>(ColdCase.getId("clue_update"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClueUpdatePacket> STREAM_CODEC = StreamCodec.of(
            ClueUpdatePacket::encode, ClueUpdatePacket::new);

    public ClueUpdatePacket(RegistryFriendlyByteBuf buf) {
        this(buf.readUUID(), ComponentSerialization.STREAM_CODEC.decode(buf));
    }

    public static void encode(RegistryFriendlyByteBuf buf, ClueUpdatePacket val) {
        buf.writeUUID(val.messageId());
        ComponentSerialization.STREAM_CODEC.encode(buf, val.component());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}