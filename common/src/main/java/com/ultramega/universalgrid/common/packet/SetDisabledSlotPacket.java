package com.ultramega.universalgrid.common.packet;

import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReferenceFactory;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record SetDisabledSlotPacket(SlotReference disabledSlot) implements CustomPacketPayload {
    public static final Type<SetDisabledSlotPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("set_disabled_slot"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetDisabledSlotPacket> STREAM_CODEC = StreamCodec.composite(
        SlotReferenceFactory.STREAM_CODEC, SetDisabledSlotPacket::disabledSlot,
        SetDisabledSlotPacket::new
    );

    public static void handle(final SetDisabledSlotPacket packet, final PacketContext ctx) {
        final AbstractContainerMenu container = ctx.getPlayer().containerMenu;
        if (container instanceof AbstractBaseContainerMenu containerMenu) {
            ((MixinDisabledSlot) containerMenu).universalgrid$setDisabledSlot(packet.disabledSlot());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
