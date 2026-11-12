package com.ultramega.universalgrid.common;

import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.mixin.AccessorRefinedStorageApiImpl;
import com.ultramega.universalgrid.common.mixin.InvokerRefinedStorageApiProxy;
import com.ultramega.universalgrid.common.packet.c2s.UseUniversalGridOnServerPacket;
import com.ultramega.universalgrid.common.radialmenu.GridSelectionOverlay;
import com.ultramega.universalgrid.common.registry.Items;
import com.ultramega.universalgrid.common.registry.KeyMappings;
import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridItem;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;

import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public abstract class AbstractClientModInitializer {
    private static final long HOLD_TIME = 200;

    @Nullable
    private static GridPress activePress;

    protected static void registerHeldGridInput() {
        WirelessUniversalGridItem.setClientUseListener(AbstractClientModInitializer::startHeldGridPress);
    }

    private static void startHeldGridPress(final Player player, final InteractionHand hand) {
        final Minecraft mc = Minecraft.getInstance();
        if (activePress != null || mc.player != player || !player.isUsingItem() || player.getUsedItemHand() != hand
            || !(player.getItemInHand(hand).getItem() instanceof WirelessUniversalGridItem gridItem)) {
            return;
        }
        final SlotReference slot = RefinedStorageApi.INSTANCE.createInventorySlotReference(player, hand);
        final KeyMapping useKey = mc.options.keyUse;
        activePress = new GridPress(
            useKey,
            player,
            Util.getMillis(),
            () -> player.getItemInHand(hand).is(gridItem)
                && (hand == InteractionHand.OFF_HAND || slot.isDisabledSlot(player.getInventory().selected))
                && (!useKey.isDown() || (player.isUsingItem() && player.getUsedItemHand() == hand)),
            () -> slot.resolve(player).ifPresent(stack -> gridItem.useOnClient(player.level(), stack, slot)),
            gridType -> openGridType(player, slot, gridType)
        );
    }

    protected static void tickInputEvents() {
        final Minecraft mc = Minecraft.getInstance();
        final KeyMapping key = KeyMappings.INSTANCE.getOpenWirelessUniversalGrid();
        final Player player = mc.player;

        final boolean inGame = mc.screen == null && mc.getOverlay() == null && mc.isWindowActive();
        if (!inGame || player == null) {
            cancelPress(false);
            if (key != null) {
                drainClicks(key);
            }
            return;
        }

        final long now = Util.getMillis();
        while (key != null && key.consumeClick()) {
            if (activePress == null) {
                activePress = new GridPress(
                    key,
                    player,
                    now,
                    () -> true,
                    () -> RefinedStorageApi.INSTANCE.useSlotReferencedItem(
                        player,
                        Items.INSTANCE.getWirelessUniversalGrid(),
                        Items.INSTANCE.getCreativeWirelessUniversalGrid()
                    ),
                    gridType -> switchGridType(player, gridType)
                );
            }
        }

        final GridPress press = activePress;
        if (press == null) {
            return;
        }
        if (press.player() != player || !press.canContinue().getAsBoolean()) {
            cancelPress(true);
            return;
        }

        final GridSelectionOverlay overlay = GridSelectionOverlay.INSTANCE;
        if (!press.key().isDown()) {
            activePress = null;
            if (overlay.isOpened()) {
                overlay.stopAndSelect();
            } else {
                press.onTap().run();
            }
        } else if (!overlay.isOpened() && now - press.startedAt() >= HOLD_TIME) {
            overlay.open(press.onSelect());
        }
    }

    private static void drainClicks(final KeyMapping key) {
        while (key.consumeClick()) {
            // Discard stale activations from GUI/context transitions
            continue;
        }
    }

    private static void cancelPress(final boolean grabMouse) {
        activePress = null;
        if (GridSelectionOverlay.INSTANCE.isOpened()) {
            GridSelectionOverlay.INSTANCE.close(grabMouse);
        }
    }

    private static void switchGridType(final Player player, final GridTypes gridType) {
        if (RefinedStorageApi.INSTANCE instanceof InvokerRefinedStorageApiProxy proxy
            && proxy.universalgrid$ensureLoaded() instanceof AccessorRefinedStorageApiImpl accessor) {
            final Set<Item> validItems = Set.of(Items.INSTANCE.getWirelessUniversalGrid(), Items.INSTANCE.getCreativeWirelessUniversalGrid());

            accessor.getSlotReferenceProvider().findForUse(player, (Item) validItems.toArray()[0], validItems)
                .ifPresent(slot -> openGridType(player, slot, gridType));
        }
    }

    private static void openGridType(final Player player, final SlotReference slot, final GridTypes gridType) {
        slot.resolve(player).ifPresent(grid -> {
            com.ultramega.universalgrid.common.Platform.getConfig().getWirelessUniversalGrid().setGridType(gridType);
            Platform.INSTANCE.sendPacketToServer(new UseUniversalGridOnServerPacket(grid, slot, gridType));
        });
    }

    private record GridPress(KeyMapping key,
                             Player player,
                             long startedAt,
                             BooleanSupplier canContinue,
                             Runnable onTap,
                             Consumer<GridTypes> onSelect) {
    }
}
