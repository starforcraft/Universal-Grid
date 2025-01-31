package com.ultramega.universalgrid.common.packet;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReferenceFactory;
import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import com.ultramega.universalgrid.common.Platform;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record SetCursorPacketOntoStackPacket(SlotReference slotReference, int cursorX, int cursorY, boolean applyCursorPos) implements CustomPacketPayload {
    public static final Type<SetCursorPacketOntoStackPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("set_cursor_onto_stack"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetCursorPacketOntoStackPacket> STREAM_CODEC = StreamCodec.composite(
        SlotReferenceFactory.STREAM_CODEC, SetCursorPacketOntoStackPacket::slotReference,
        ByteBufCodecs.INT, SetCursorPacketOntoStackPacket::cursorX,
        ByteBufCodecs.INT, SetCursorPacketOntoStackPacket::cursorY,
        ByteBufCodecs.BOOL, SetCursorPacketOntoStackPacket::applyCursorPos,
        SetCursorPacketOntoStackPacket::new
    );

    public static void handle(final SetCursorPacketOntoStackPacket packet, final PacketContext ctx) {
        packet.slotReference().resolve(ctx.getPlayer()).ifPresent(stack ->
            Platform.setWirelessUniversalGridState(stack, packet.cursorX, packet.cursorY, packet.applyCursorPos));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
