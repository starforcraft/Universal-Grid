package com.ultramega.universalgrid.common;

import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.mixin.AccessorRefinedStorageApiImpl;
import com.ultramega.universalgrid.common.mixin.InvokerRefinedStorageApiProxy;
import com.ultramega.universalgrid.common.packet.c2s.UseUniversalGridOnServerPacket;
import com.ultramega.universalgrid.common.radialmenu.GridSelectionOverlay;
import com.ultramega.universalgrid.common.registry.Items;
import com.ultramega.universalgrid.common.registry.KeyMappings;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;

import java.util.Set;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public abstract class AbstractClientModInitializer {
    private static boolean wasDown = false;
    private static long pressTime = 0;
    private static final long HOLD_TIME = 200;

    protected static void tickInputEvents() {
        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        final KeyMapping key = KeyMappings.INSTANCE.getOpenWirelessUniversalGrid();
        if (key == null) {
            return;
        }

        final boolean isDown = key.isDown();

        // Key pressed
        if (isDown && !wasDown) {
            pressTime = System.currentTimeMillis();
        }

        // Key released
        if (!isDown && wasDown) {
            final long heldDuration = System.currentTimeMillis() - pressTime;

            if (heldDuration >= HOLD_TIME) {
                // Was held -> stop and select grid
                GridSelectionOverlay.INSTANCE.stopAndSelect();
            } else {
                // Was a short press -> use grid
                RefinedStorageApi.INSTANCE.useSlotReferencedItem(
                    player,
                    Items.INSTANCE.getWirelessUniversalGrid(),
                    Items.INSTANCE.getCreativeWirelessUniversalGrid()
                );
            }
        }

        // While holding
        if (isDown && (System.currentTimeMillis() - pressTime >= HOLD_TIME)) {
            GridSelectionOverlay.INSTANCE.open((gridType) -> AbstractClientModInitializer.switchGridType(player, gridType));
        }

        wasDown = isDown;
    }

    private static void switchGridType(final Player player, final GridTypes gridType) {
        if (RefinedStorageApi.INSTANCE instanceof InvokerRefinedStorageApiProxy proxy
            && proxy.universalgrid$ensureLoaded() instanceof AccessorRefinedStorageApiImpl accessor) {
            final Set<Item> validItems = Set.of(Items.INSTANCE.getWirelessUniversalGrid(), Items.INSTANCE.getCreativeWirelessUniversalGrid());

            accessor.getSlotReferenceProvider().findForUse(player, (Item) validItems.toArray()[0], validItems)
                .ifPresent((slotReference) ->
                    slotReference.resolve(player).ifPresent((grid) -> {
                        PlatformProxy.getConfig().getWirelessUniversalGrid().setGridType(gridType);
                        Platform.INSTANCE.sendPacketToServer(new UseUniversalGridOnServerPacket(grid, slotReference, gridType));
                    }));
        }
    }
}
