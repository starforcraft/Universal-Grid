package com.ultramega.universalgrid.common.packet;

import com.ultramega.universalgrid.common.Platform;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReferenceFactory;
import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record SetCursorPosStackPacket(SlotReference slotReference, int cursorX, int cursorY, boolean applyCursorPos) implements CustomPacketPayload {
    public static final Type<SetCursorPosStackPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("set_cursor_pos_stack"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetCursorPosStackPacket> STREAM_CODEC = StreamCodec.composite(
        SlotReferenceFactory.STREAM_CODEC, SetCursorPosStackPacket::slotReference,
        ByteBufCodecs.INT, SetCursorPosStackPacket::cursorX,
        ByteBufCodecs.INT, SetCursorPosStackPacket::cursorY,
        ByteBufCodecs.BOOL, SetCursorPosStackPacket::applyCursorPos,
        SetCursorPosStackPacket::new
    );

    public static void handle(final SetCursorPosStackPacket packet, final PacketContext ctx) {
        packet.slotReference().resolve(ctx.getPlayer()).ifPresent(stack ->
            Platform.setWirelessUniversalGridState(stack, packet.cursorX, packet.cursorY, packet.applyCursorPos));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
