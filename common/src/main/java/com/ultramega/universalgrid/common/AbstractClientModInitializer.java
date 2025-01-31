package com.ultramega.universalgrid.common;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;

import com.ultramega.universalgrid.common.registry.Items;
import com.ultramega.universalgrid.common.registry.KeyMappings;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractClientModInitializer {
    protected static void handleInputEvents() {
        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final KeyMapping openWirelessUniversalGrid = KeyMappings.INSTANCE.getOpenWirelessUniversalGrid();
        while (openWirelessUniversalGrid != null && openWirelessUniversalGrid.consumeClick()) {
            RefinedStorageApi.INSTANCE.useSlotReferencedItem(
                player,
                Items.INSTANCE.getWirelessUniversalGrid(),
                Items.INSTANCE.getCreativeWirelessUniversalGrid()
            );
        }
    }
}
