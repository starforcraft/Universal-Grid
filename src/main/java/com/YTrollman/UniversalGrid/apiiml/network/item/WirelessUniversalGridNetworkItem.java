package com.YTrollman.UniversalGrid.apiiml.network.item;

import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridGridFactory;
import com.YTrollman.UniversalGrid.item.WirelessUniversalGridItem;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.util.WorldUtils;
import com.refinedmods.refinedstorageaddons.RSAddons;
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
    private PlayerSlot slot;

    public WirelessUniversalGridNetworkItem(INetworkItemManager handler, PlayerEntity player, ItemStack stack, PlayerSlot slot) {
        this.handler = handler;
        this.player = player;
        this.stack = stack;
        this.slot = slot;
    }

    @Override
    public PlayerEntity getPlayer() {
        return player;
    }

    @Override
    public boolean onOpen(INetwork network) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

        if (RSAddons.SERVER_CONFIG.getWirelessCraftingGrid().getUseEnergy() &&
                ((WirelessUniversalGridItem) stack.getItem()).getType() != WirelessUniversalGridItem.Type.CREATIVE &&
                energy != null &&
                energy.getEnergyStored() <= RSAddons.SERVER_CONFIG.getWirelessCraftingGrid().getOpenUsage()) {
            sendOutOfEnergyMessage();

            return false;
        }

        if (!network.getSecurityManager().hasPermission(Permission.MODIFY, player)) {
            WorldUtils.sendNoPermissionMessage(player);

            return false;
        }

        API.instance().getGridManager().openGrid(WirelessUniversalGridGridFactory.ID, (ServerPlayerEntity) player, stack, slot);

        drainEnergy(RSAddons.SERVER_CONFIG.getWirelessCraftingGrid().getOpenUsage());

        return true;
    }

    @Override
    public void drainEnergy(int energy) {
        if (RSAddons.SERVER_CONFIG.getWirelessCraftingGrid().getUseEnergy() && ((WirelessUniversalGridItem) stack.getItem()).getType() != WirelessUniversalGridItem.Type.CREATIVE) {
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