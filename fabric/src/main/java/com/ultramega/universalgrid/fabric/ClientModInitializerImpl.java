package com.ultramega.universalgrid.fabric;

import com.refinedmods.refinedstorage.common.support.network.item.NetworkItemPropertyFunction;
import com.refinedmods.refinedstorage.common.support.packet.PacketHandler;

import com.ultramega.universalgrid.common.AbstractClientModInitializer;
import com.ultramega.universalgrid.common.ContentNames;
import com.ultramega.universalgrid.common.packet.SetCursorPacketOntoWindowPacket;
import com.ultramega.universalgrid.common.registry.Items;
import com.ultramega.universalgrid.common.registry.KeyMappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ClientModInitializerImpl extends AbstractClientModInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerKeyMappings();
        registerItemProperties();
        registerPacketHandlers();
    }

    private void registerKeyMappings() {
        KeyMappings.INSTANCE.setOpenWirelessUniversalGrid(KeyBindingHelper.registerKeyBinding(new KeyMapping(
            ContentNames.OPEN_WIRELESS_UNIVERSAL_GRID_TRANSLATION_KEY,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            ContentNames.MOD_TRANSLATION_KEY
        )));
        ClientTickEvents.END_CLIENT_TICK.register(client -> handleInputEvents());
    }

    private void registerItemProperties() {
        ItemProperties.register(
            Items.INSTANCE.getWirelessUniversalGrid(),
            NetworkItemPropertyFunction.NAME,
            new NetworkItemPropertyFunction()
        );
        ItemProperties.register(
            Items.INSTANCE.getCreativeWirelessUniversalGrid(),
            NetworkItemPropertyFunction.NAME,
            new NetworkItemPropertyFunction()
        );
    }

    private void registerPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(
            SetCursorPacketOntoWindowPacket.PACKET_TYPE,
            wrapHandler(SetCursorPacketOntoWindowPacket::handle)
        );
    }

    private static <T extends CustomPacketPayload> ClientPlayNetworking.PlayPayloadHandler<T> wrapHandler(
        final PacketHandler<T> handler
    ) {
        return (packet, ctx) -> handler.handle(packet, ctx::player);
    }
}
