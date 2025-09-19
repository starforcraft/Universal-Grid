package com.ultramega.universalgrid.common.packet.c2s;

import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridItem;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReferenceFactory;
import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static com.refinedmods.refinedstorage.common.util.PlatformUtil.enumStreamCodec;
import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record UseUniversalGridOnServerPacket(ItemStack grid, SlotReference slotReference, GridTypes gridType) implements CustomPacketPayload {
    public static final Type<UseUniversalGridOnServerPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("use_universal_grid_on_server"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UseUniversalGridOnServerPacket> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC, UseUniversalGridOnServerPacket::grid,
        SlotReferenceFactory.STREAM_CODEC, UseUniversalGridOnServerPacket::slotReference,
        enumStreamCodec(GridTypes.values()), UseUniversalGridOnServerPacket::gridType,
        UseUniversalGridOnServerPacket::new
    );

    public static void handle(final UseUniversalGridOnServerPacket packet, final PacketContext ctx) {
        if (packet.grid().getItem() instanceof WirelessUniversalGridItem gridItem && ctx.getPlayer() instanceof ServerPlayer serverPlayer) {
            gridItem.useGridCorrectly(serverPlayer, serverPlayer.level(), packet.slotReference(), packet.gridType());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
