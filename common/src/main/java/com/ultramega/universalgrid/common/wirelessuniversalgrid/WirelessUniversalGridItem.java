package com.ultramega.universalgrid.common.wirelessuniversalgrid;

import com.refinedmods.refinedstorage.api.network.energy.EnergyStorage;
import com.refinedmods.refinedstorage.api.network.impl.energy.EnergyStorageImpl;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.energy.AbstractNetworkEnergyItem;
import com.refinedmods.refinedstorage.common.api.support.energy.EnergyItemHelper;
import com.refinedmods.refinedstorage.common.api.support.network.item.NetworkItemContext;
import com.refinedmods.refinedstorage.common.api.support.network.item.NetworkItemHelper;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.content.Items;

import com.ultramega.universalgrid.common.Platform;
import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.packet.SetCursorPacketOntoWindowPacket;
import com.ultramega.universalgrid.common.registry.DataComponents;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WirelessUniversalGridItem extends AbstractNetworkEnergyItem {
    private final boolean creative;

    public WirelessUniversalGridItem(final boolean creative,
                                     final EnergyItemHelper energyItemHelper,
                                     final NetworkItemHelper networkItemHelper) {
        super(
            new Item.Properties().stacksTo(1),
            energyItemHelper,
            networkItemHelper
        );
        this.creative = creative;
    }

    public EnergyStorage createEnergyStorage(final ItemStack stack) {
        final EnergyStorage energyStorage = new EnergyStorageImpl(
            Platform.getConfig().getWirelessUniversalGrid().getEnergyCapacity()
        );
        return RefinedStorageApi.INSTANCE.asItemEnergyStorage(energyStorage, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer && level.getServer() != null) {
            final SlotReference slotReference = RefinedStorageApi.INSTANCE.createInventorySlotReference(serverPlayer, hand);
            slotReference.resolve(serverPlayer).ifPresent(s ->
                openCorrectGrid(serverPlayer, s, slotReference));
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void use(final ServerPlayer player, final ItemStack stack, final SlotReference slotReference) {
        openCorrectGrid(player, stack, slotReference);
    }

    @Override
    protected void use(@Nullable final Component name,
                       final ServerPlayer player,
                       final SlotReference slotReference,
                       final NetworkItemContext context) {
    }

    private void openCorrectGrid(ServerPlayer serverPlayer, ItemStack stack, SlotReference slotReference) {
        GridTypes gridType = Platform.getConfig().getWirelessUniversalGrid().getGridType();
        switch (gridType) {
            case WIRELESS_GRID ->
                Items.INSTANCE.getWirelessGrid().use(serverPlayer, stack, slotReference);
            case WIRELESS_CRAFTING_GRID ->
                com.refinedmods.refinedstorage.quartzarsenal.common.Items.INSTANCE.getWirelessCraftingGrid().use(serverPlayer, stack, slotReference);
            case WIRELESS_AUTOCRAFTING_MONITOR ->
                Items.INSTANCE.getWirelessAutocraftingMonitor().use(serverPlayer, stack, slotReference);
        }

        // Read and apply cursor position
        final WirelessUniversalGridState state = stack.get(DataComponents.INSTANCE.getWirelessUniversalGridState());
        if (state == null)
            return;
        if (!state.applyCursorPos())
            return;

        com.refinedmods.refinedstorage.common.Platform.INSTANCE.sendPacketToClient(serverPlayer,
            new SetCursorPacketOntoWindowPacket(state.cursorX(), state.cursorY()));

        Platform.setWirelessUniversalGridState(stack, state.cursorX(), state.cursorY(), false);
    }
}
