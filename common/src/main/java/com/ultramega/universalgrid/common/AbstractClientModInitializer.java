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
import net.minecraft.world.item.ItemStack;

public abstract class AbstractClientModInitializer {
    private static boolean activePress = false;
    private static boolean holdTriggered = false;
    private static long pressTime = 0;
    private static final long HOLD_TIME = 200;

    protected static void tickInputEvents() {
        final KeyMapping key = KeyMappings.INSTANCE.getOpenWirelessUniversalGrid();
        if (key == null) {
            resetInputState();
            return;
        }

        final Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) {
            resetInputState();
            drainClicks(key);
            return;
        }

        final Player player = mc.player;
        if (player == null) {
            resetInputState();
            drainClicks(key);
            return;
        }

        final long now = System.currentTimeMillis();

        // Key pressed
        while (key.consumeClick()) {
            if (!activePress) {
                activePress = true;
                holdTriggered = false;
                pressTime = now;
            }
        }

        if (!activePress) {
            return;
        }

        // Key released
        if (!key.isDown()) {
            if (holdTriggered) {
                // Was held -> stop and select grid
                GridSelectionOverlay.INSTANCE.stopAndSelect();
            } else {
                // Was a short press -> use grid
                RefinedStorageApi.INSTANCE.usePlayerSlotReferencedItem(
                    player,
                    Items.INSTANCE.getWirelessUniversalGrid(),
                    Items.INSTANCE.getCreativeWirelessUniversalGrid()
                );
            }

            resetInputState();
            return;
        }

        if (!holdTriggered && now - pressTime >= HOLD_TIME) {
            holdTriggered = true;
            GridSelectionOverlay.INSTANCE.open((gridType) -> switchGridType(player, gridType));
        }
    }

    private static void drainClicks(final KeyMapping key) {
        while (key.consumeClick()) {
            // Discard stale activations from GUI/context transitions
            continue;
        }
    }

    private static void resetInputState() {
        activePress = false;
        holdTriggered = false;
        pressTime = 0;
    }

    private static void switchGridType(final Player player, final GridTypes gridType) {
        if (RefinedStorageApi.INSTANCE instanceof InvokerRefinedStorageApiProxy proxy
            && proxy.universalgrid$ensureLoaded() instanceof AccessorRefinedStorageApiImpl accessor) {
            final Set<Item> validItems = Set.of(Items.INSTANCE.getWirelessUniversalGrid(), Items.INSTANCE.getCreativeWirelessUniversalGrid());

            accessor.getPlayerSlotReferenceProvider().findForUse(player, (Item) validItems.toArray()[0], validItems)
                .ifPresent((slotReference) -> {
                    final ItemStack grid = slotReference.get(player);
                    PlatformProxy.getConfig().getWirelessUniversalGrid().setGridType(gridType);
                    Platform.INSTANCE.sendPacketToServer(new UseUniversalGridOnServerPacket(grid, slotReference, gridType));
                });
        }
    }
}
