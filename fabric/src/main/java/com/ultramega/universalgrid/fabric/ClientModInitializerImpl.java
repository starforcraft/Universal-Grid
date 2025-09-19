package com.ultramega.universalgrid.fabric;

import com.ultramega.universalgrid.common.AbstractClientModInitializer;
import com.ultramega.universalgrid.common.ContentNames;
import com.ultramega.universalgrid.common.packet.s2c.SetCursorPosWindowPacket;
import com.ultramega.universalgrid.common.packet.s2c.SetDisabledSlotPacket;
import com.ultramega.universalgrid.common.packet.s2c.UseUniversalGridOnClientPacket;
import com.ultramega.universalgrid.common.registry.Items;
import com.ultramega.universalgrid.common.registry.KeyMappings;

import com.refinedmods.refinedstorage.common.support.network.item.NetworkItemPropertyFunction;
import com.refinedmods.refinedstorage.common.support.packet.PacketHandler;

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
        this.registerKeyMappings();
        this.registerItemProperties();
        this.registerPacketHandlers();
    }

    private void registerKeyMappings() {
        KeyMappings.INSTANCE.setOpenWirelessUniversalGrid(KeyBindingHelper.registerKeyBinding(new KeyMapping(
            ContentNames.OPEN_WIRELESS_UNIVERSAL_GRID_TRANSLATION_KEY,
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            ContentNames.MOD_TRANSLATION_KEY
        )));
        KeyMappings.INSTANCE.setSwitchWirelessUniversalGridType(KeyBindingHelper.registerKeyBinding(new KeyMapping(
            ContentNames.SWITCH_WIRELESS_UNIVERSAL_GRID_TYPE_TRANSLATION_KEY,
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
