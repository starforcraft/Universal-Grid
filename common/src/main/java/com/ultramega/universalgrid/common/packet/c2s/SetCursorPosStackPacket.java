package com.ultramega.universalgrid.common.packet.c2s;

import com.ultramega.universalgrid.common.PlatformProxy;

import com.refinedmods.refinedstorage.common.api.support.slotreference.PlayerSlotReference;
import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record SetCursorPosStackPacket(PlayerSlotReference slotReference, int cursorX, int cursorY, boolean applyCursorPos) implements CustomPacketPayload {
    public static final Type<SetCursorPosStackPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("set_cursor_pos_stack"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetCursorPosStackPacket> STREAM_CODEC = StreamCodec.composite(
        PlayerSlotReference.STREAM_CODEC, SetCursorPosStackPacket::slotReference,
        ByteBufCodecs.INT, SetCursorPosStackPacket::cursorX,
        ByteBufCodecs.INT, SetCursorPosStackPacket::cursorY,
        ByteBufCodecs.BOOL, SetCursorPosStackPacket::applyCursorPos,
        SetCursorPosStackPacket::new
    );

    public static void handle(final SetCursorPosStackPacket packet, final PacketContext ctx) {
        final ItemStack stack = packet.slotReference().get(ctx.getPlayer());
        PlatformProxy.setWirelessUniversalGridState(stack, packet.cursorX, packet.cursorY, packet.applyCursorPos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
