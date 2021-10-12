package com.YTrollman.UniversalGrid.network;

import com.YTrollman.UniversalGrid.config.UniversalGridConfig;
import com.YTrollman.UniversalGrid.items.WirelessUniversalGridItem;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.util.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class WirelessUniversalGridNetworkItem implements INetworkItem {
    private INetworkItemManager handler;
    private PlayerEntity player;
    private ItemStack stack;
    private int slotId;

    public WirelessUniversalGridNetworkItem(INetworkItemManager handler, PlayerEntity player, ItemStack stack, int slotId) {
        this.handler = handler;
        this.player = player;
        this.stack = stack;
        this.slotId = slotId;
    }

    @Override
    public PlayerEntity getPlayer() {
        return player;
    }

    @Override
    public boolean onOpen(INetwork network) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

        if (UniversalGridConfig.UNIVERSAL_GRID_USE_ENERGY.get() &&
                ((WirelessUniversalGridItem) stack.getItem()).getType() != WirelessUniversalGridItem.Type.CREATIVE &&
                energy != null &&
                energy.getEnergyStored() <= UniversalGridConfig.UNIVERSAL_GRID_OPEN_USAGE.get()) {
            sendOutOfEnergyMessage();

            return false;
        }

        if (!network.getSecurityManager().hasPermission(Permission.MODIFY, player)) {
            WorldUtils.sendNoPermissionMessage(player);

            return false;
        }

        API.instance().getGridManager().openGrid(WirelessUniversalGridGridFactory.ID, (ServerPlayerEntity) player, stack, slotId);

        drainEnergy(UniversalGridConfig.UNIVERSAL_GRID_OPEN_USAGE.get());

        return true;
    }

    @Override
    public void drainEnergy(int energy) {
        if (UniversalGridConfig.UNIVERSAL_GRID_USE_ENERGY.get() && ((WirelessUniversalGridItem) stack.getItem()).getType() != WirelessUniversalGridItem.Type.CREATIVE) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
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
        player.sendMessage(new TranslationTextComponent("misc.refinedstorage.network_item.out_of_energy", new TranslationTextComponent(stack.getItem().getDescriptionId())), player.getUUID());
    }
}
