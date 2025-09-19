package com.ultramega.universalgrid.common.packet.c2s;

import com.ultramega.universalgrid.common.interfaces.MixinUpdateSlot;

import com.refinedmods.refinedstorage.common.autocrafting.monitor.WirelessAutocraftingMonitorContainerMenu;
import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record UpdateDisabledSlotPacket() implements CustomPacketPayload {
    public static final Type<UpdateDisabledSlotPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("update_disabled_slot"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateDisabledSlotPacket> STREAM_CODEC = StreamCodec.unit(new UpdateDisabledSlotPacket());

    public static void handle(final UpdateDisabledSlotPacket packet, final PacketContext ctx) {
        final Player player = ctx.getPlayer();
        if (player.containerMenu instanceof WirelessAutocraftingMonitorContainerMenu containerMenu) {
            ((MixinUpdateSlot) containerMenu).universalgrid$sendUpdate();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
