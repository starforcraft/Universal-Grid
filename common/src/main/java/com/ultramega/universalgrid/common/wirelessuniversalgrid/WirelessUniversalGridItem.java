package com.ultramega.universalgrid.common.wirelessuniversalgrid;

import com.ultramega.universalgrid.common.ContentIds;
import com.ultramega.universalgrid.common.PlatformProxy;
import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.packet.c2s.UseUniversalGridOnServerPacket;
import com.ultramega.universalgrid.common.packet.s2c.SetCursorPosWindowPacket;
import com.ultramega.universalgrid.common.packet.s2c.UseUniversalGridOnClientPacket;
import com.ultramega.universalgrid.common.registry.DataComponents;

import com.refinedmods.refinedstorage.api.network.energy.EnergyStorage;
import com.refinedmods.refinedstorage.api.network.impl.energy.EnergyStorageImpl;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.energy.AbstractNetworkEnergyItem;
import com.refinedmods.refinedstorage.common.api.support.energy.EnergyItemContext;
import com.refinedmods.refinedstorage.common.api.support.network.item.NetworkItemContext;
import com.refinedmods.refinedstorage.common.api.support.slotreference.PlayerSlotReference;
import com.refinedmods.refinedstorage.common.content.Items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class WirelessUniversalGridItem extends AbstractNetworkEnergyItem {
    public WirelessUniversalGridItem(final boolean creative) {
        super(
            new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM,
                creative ? ContentIds.CREATIVE_WIRELESS_UNIVERSAL_GRID : ContentIds.WIRELESS_UNIVERSAL_GRID)),
            RefinedStorageApi.INSTANCE.getEnergyItemHelper(),
            RefinedStorageApi.INSTANCE.getNetworkItemHelper()
        );
    }

    public static EnergyStorage createEnergyStorage(final ItemStack stack, final EnergyItemContext context) {
        final EnergyStorage energyStorage = new EnergyStorageImpl(
            PlatformProxy.getConfig().getWirelessUniversalGrid().getEnergyCapacity()
        );
        return RefinedStorageApi.INSTANCE.createItemEnergyStorage(energyStorage, stack, context);
    }

    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);

        final PlayerSlotReference slotReference = RefinedStorageApi.INSTANCE.createPlayerInventorySlotReference(player, hand);
        this.useOnClient(level, stack, slotReference);

        return InteractionResult.CONSUME;
    }

    @Override
    public void use(final ServerPlayer player, final ItemStack stack, final PlayerSlotReference playerSlotReference) {
        com.refinedmods.refinedstorage.common.Platform.INSTANCE.sendPacketToClient(player, new UseUniversalGridOnClientPacket(stack, playerSlotReference));
    }

    @Override
    protected void use(@Nullable final Component name, final ServerPlayer player, final PlayerSlotReference slotReference, final NetworkItemContext context) {
    }

    public void useOnClient(final Level level, final ItemStack stack, final PlayerSlotReference playerSlotReference) {
        if (level.isClientSide()) {
            final GridTypes gridType = PlatformProxy.getConfig().getWirelessUniversalGrid().getGridType();
            com.refinedmods.refinedstorage.common.Platform.INSTANCE.sendPacketToServer(new UseUniversalGridOnServerPacket(stack, playerSlotReference, gridType));
        }
    }

    public void useGridCorrectly(final ServerPlayer serverPlayer, final Level level, final PlayerSlotReference playerSlotReference, final GridTypes gridType) {
        if (level.getServer() != null) {
            final ItemStack stack = playerSlotReference.get(serverPlayer);
            this.openGridType(serverPlayer, stack, playerSlotReference, gridType);
        }
    }

    private void openGridType(final ServerPlayer serverPlayer, final ItemStack stack, final PlayerSlotReference playerSlotReference, final GridTypes gridType) {
        switch (gridType) {
            case WIRELESS_GRID ->
                Items.INSTANCE.getWirelessGrid().use(serverPlayer, stack, playerSlotReference);
            case WIRELESS_CRAFTING_GRID ->
                com.refinedmods.refinedstorage.quartzarsenal.common.Items.INSTANCE.getWirelessCraftingGrid().use(serverPlayer, stack, playerSlotReference);
            case WIRELESS_AUTOCRAFTING_MONITOR ->
                Items.INSTANCE.getWirelessAutocraftingMonitor().use(serverPlayer, stack, playerSlotReference);
        }

        // Read and apply cursor position
        final WirelessUniversalGridState state = stack.get(DataComponents.INSTANCE.getWirelessUniversalGridState());
        if (state == null) {
            return;
        }
        if (!state.applyCursorPos()) {
            return;
        }

        com.refinedmods.refinedstorage.common.Platform.INSTANCE.sendPacketToClient(serverPlayer,
            new SetCursorPosWindowPacket(state.cursorX(), state.cursorY()));

        PlatformProxy.setWirelessUniversalGridState(stack, state.cursorX(), state.cursorY(), false);
    }
}
