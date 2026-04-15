package com.ultramega.universalgrid.fabric;

import com.ultramega.universalgrid.common.AbstractClientModInitializer;
import com.ultramega.universalgrid.common.ContentNames;
import com.ultramega.universalgrid.common.packet.s2c.SetCursorPosWindowPacket;
import com.ultramega.universalgrid.common.packet.s2c.SetDisabledSlotPacket;
import com.ultramega.universalgrid.common.packet.s2c.UseUniversalGridOnClientPacket;
import com.ultramega.universalgrid.common.radialmenu.GridSelectionOverlay;
import com.ultramega.universalgrid.common.registry.KeyMappings;

import com.refinedmods.refinedstorage.common.support.packet.PacketHandler;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public class ClientModInitializerImpl extends AbstractClientModInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        this.registerKeyMappings();
        this.registerPacketHandlers();
    }

    private void registerKeyMappings() {
        final KeyMapping.Category category = KeyMapping.Category.register(createUniversalGridIdentifier("keymappings"));

        KeyMappings.INSTANCE.setOpenWirelessUniversalGrid(KeyMappingHelper.registerKeyMapping(new KeyMapping(
            ContentNames.OPEN_WIRELESS_UNIVERSAL_GRID_TRANSLATION_KEY,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            category
        )));
        KeyMappings.INSTANCE.setSwitchWirelessUniversalGridType(KeyMappingHelper.registerKeyMapping(new KeyMapping(
            ContentNames.SWITCH_WIRELESS_UNIVERSAL_GRID_TYPE_TRANSLATION_KEY,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            category
        )));
        ClientTickEvents.END_CLIENT_TICK.register(client -> tickInputEvents());
        HudElementRegistry.addLast(createUniversalGridIdentifier("grid_types_selection"), GridSelectionOverlay.INSTANCE::render);
    }

    private void registerPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(
            SetCursorPosWindowPacket.PACKET_TYPE,
            wrapHandler(SetCursorPosWindowPacket::handle)
        );
        ClientPlayNetworking.registerGlobalReceiver(
            SetDisabledSlotPacket.PACKET_TYPE,
            wrapHandler(SetDisabledSlotPacket::handle)
        );
        ClientPlayNetworking.registerGlobalReceiver(
            UseUniversalGridOnClientPacket.PACKET_TYPE,
            wrapHandler(UseUniversalGridOnClientPacket::handle)
        );
    }

    private static <T extends CustomPacketPayload> ClientPlayNetworking.PlayPayloadHandler<T> wrapHandler(
        final PacketHandler<T> handler
    ) {
        return (packet, ctx) -> handler.handle(packet, ctx::player);
    }
}
