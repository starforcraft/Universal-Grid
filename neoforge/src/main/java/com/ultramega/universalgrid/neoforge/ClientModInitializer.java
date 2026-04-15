package com.ultramega.universalgrid.neoforge;

import com.ultramega.universalgrid.common.AbstractClientModInitializer;
import com.ultramega.universalgrid.common.ContentNames;
import com.ultramega.universalgrid.common.radialmenu.GridSelectionOverlay;
import com.ultramega.universalgrid.common.registry.KeyMappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public class ClientModInitializer extends AbstractClientModInitializer {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent e) {
        NeoForge.EVENT_BUS.addListener(ClientModInitializer::onClientTick);
    }

    @SubscribeEvent
    public static void onClientTick(final ClientTickEvent.Post e) {
        tickInputEvents();
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(final RegisterKeyMappingsEvent e) {
        final KeyMapping.Category category = new KeyMapping.Category(createUniversalGridIdentifier("keymappings"));
        e.registerCategory(category);

        final KeyMapping openWirelessUniversalGrid = new KeyMapping(
            ContentNames.OPEN_WIRELESS_UNIVERSAL_GRID_TRANSLATION_KEY,
            KeyConflictContext.IN_GAME,
            InputConstants.UNKNOWN,
            category
        );
        e.register(openWirelessUniversalGrid);
        KeyMappings.INSTANCE.setOpenWirelessUniversalGrid(openWirelessUniversalGrid);

        final KeyMapping switchWirelessUniversalGridType = new KeyMapping(
            ContentNames.SWITCH_WIRELESS_UNIVERSAL_GRID_TYPE_TRANSLATION_KEY,
            KeyConflictContext.GUI,
            InputConstants.UNKNOWN,
            category
        );
        e.register(switchWirelessUniversalGridType);
        KeyMappings.INSTANCE.setSwitchWirelessUniversalGridType(switchWirelessUniversalGridType);
    }

    @SubscribeEvent
    public static void registerGuiLayers(final RegisterGuiLayersEvent event) {
        event.registerAboveAll(createUniversalGridIdentifier("grid_types_selection"), GridSelectionOverlay.INSTANCE::render);
    }
}
