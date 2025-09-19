package com.ultramega.universalgrid.common.packet.s2c;

import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridItem;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReferenceFactory;
import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record UseUniversalGridOnClientPacket(ItemStack grid, SlotReference slotReference) implements CustomPacketPayload {
    public static final Type<UseUniversalGridOnClientPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("use_universal_grid_on_client"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UseUniversalGridOnClientPacket> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC, UseUniversalGridOnClientPacket::grid,
        SlotReferenceFactory.STREAM_CODEC, UseUniversalGridOnClientPacket::slotReference,
        UseUniversalGridOnClientPacket::new
    );

    public static void handle(final UseUniversalGridOnClientPacket packet, final PacketContext ctx) {
        if (packet.grid().getItem() instanceof WirelessUniversalGridItem gridItem) {
            gridItem.useOnClient(ctx.getPlayer().level(), packet.grid(), packet.slotReference());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
