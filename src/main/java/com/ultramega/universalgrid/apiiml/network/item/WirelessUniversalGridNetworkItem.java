package com.ultramega.universalgrid.apiiml.network.item;

import com.ultramega.universalgrid.apiiml.network.grid.WirelessUniversalGridGridFactory;
import com.ultramega.universalgrid.config.UniversalGridConfig;
import com.ultramega.universalgrid.item.WirelessUniversalGridItem;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.util.LevelUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class WirelessUniversalGridNetworkItem implements INetworkItem {
    private INetworkItemManager handler;
    private Player player;
    private ItemStack stack;
    private PlayerSlot slot;

    public WirelessUniversalGridNetworkItem(INetworkItemManager handler, Player player, ItemStack stack, PlayerSlot slot) {
        this.handler = handler;
        this.player = player;
        this.stack = stack;
        this.slot = slot;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean onOpen(INetwork network) {
        IEnergyStorage energy = stack.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);

        if (UniversalGridConfig.UNIVERSAL_GRID_USE_ENERGY.get() &&
                ((WirelessUniversalGridItem) stack.getItem()).getType() != WirelessUniversalGridItem.Type.CREATIVE &&
                energy != null && energy.getEnergyStored() <= UniversalGridConfig.UNIVERSAL_GRID_OPEN_USAGE.get()) {
            sendOutOfEnergyMessage();

            return false;
        }

        if (!network.getSecurityManager().hasPermission(Permission.MODIFY, player)) {
            LevelUtils.sendNoPermissionMessage(player);

            return false;
        }

        API.instance().getGridManager().openGrid(WirelessUniversalGridGridFactory.ID, (ServerPlayer) player, stack, slot);

        drainEnergy(UniversalGridConfig.UNIVERSAL_GRID_OPEN_USAGE.get());

        return true;
    }

    @Override
    public void drainEnergy(int energy) {
        if (UniversalGridConfig.UNIVERSAL_GRID_USE_ENERGY.get() && ((WirelessUniversalGridItem) stack.getItem()).getType() != WirelessUniversalGridItem.Type.CREATIVE) {
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyStorage -> {
                energyStorage.extractEnergy(energy, false);

                if (energyStorage.getEnergyStored() <= 0) {
                    handler.close(player);

                    player.closeContainer();

                    sendOutOfEnergyMessage();
                }
            });
        }
    }

    private void sendOutOfEnergyMessage() {
        player.sendSystemMessage(Component.translatable("misc.refinedstorage.network_item.out_of_energy", Component.translatable(stack.getItem().getDescriptionId())));
    }
}