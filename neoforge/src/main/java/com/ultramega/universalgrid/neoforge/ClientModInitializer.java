package com.ultramega.universalgrid.neoforge;

import com.refinedmods.refinedstorage.common.support.network.item.NetworkItemPropertyFunction;

import com.ultramega.universalgrid.common.AbstractClientModInitializer;
import com.ultramega.universalgrid.common.ContentNames;
import com.ultramega.universalgrid.common.registry.Items;
import com.ultramega.universalgrid.common.registry.KeyMappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;

public class ClientModInitializer extends AbstractClientModInitializer {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent e) {
        NeoForge.EVENT_BUS.addListener(ClientModInitializer::onKeyInput);
        NeoForge.EVENT_BUS.addListener(ClientModInitializer::onMouseInput);
        e.enqueueWork(ClientModInitializer::registerItemProperties);
    }

    @SubscribeEvent
    public static void onKeyInput(final InputEvent.Key e) {
        handleInputEvents();
    }

    @SubscribeEvent
    public static void onMouseInput(final InputEvent.MouseButton.Pre e) {
        handleInputEvents();
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(final RegisterKeyMappingsEvent e) {
        final KeyMapping openWirelessUniversalGrid = new KeyMapping(
            ContentNames.OPEN_WIRELESS_UNIVERSAL_GRID_TRANSLATION_KEY,
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            ContentNames.MOD_TRANSLATION_KEY
        );
        e.register(openWirelessUniversalGrid);
        KeyMappings.INSTANCE.setOpenWirelessUniversalGrid(openWirelessUniversalGrid);
    }

    private static void registerItemProperties() {
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
}
