package com.YTrollman.UniversalGrid.item;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.api.network.grid.ICraftingGridListener;
import com.refinedmods.refinedstorage.api.network.grid.handler.IFluidGridHandler;
import com.refinedmods.refinedstorage.api.network.grid.handler.IItemGridHandler;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCache;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCacheListener;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.api.util.IStackList;
import com.refinedmods.refinedstorage.apiimpl.storage.cache.listener.FluidGridStorageCacheListener;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.tile.grid.WirelessGrid;
import com.refinedmods.refinedstorage.util.StackUtils;
import com.refinedmods.refinedstorageaddons.RSAddons;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class WirelessUniversalGrid extends WirelessGrid {
    @Nullable
    private final MinecraftServer server;
    private final World world;
    private Set<ICraftingGridListener> listeners = new HashSet<>();

    private Container craftingContainer = new Container(null, 0) {
        @Override
        public boolean stillValid(PlayerEntity player) {
            return false;
        }

        @Override
        public void slotsChanged(IInventory inventory) {
            if (server != null) {
                onCraftingMatrixChanged();
            }
        }
    };
    private ICraftingRecipe currentRecipe;
    private CraftingInventory matrix = new CraftingInventory(craftingContainer, 3, 3);
    private CraftResultInventory result = new CraftResultInventory();

    private final int gridType;

    public WirelessUniversalGrid(ItemStack stack, World world, @Nullable MinecraftServer server, PlayerSlot slot) {
        super(stack, server, slot);

        this.server = server;
        this.world = world;

        gridType = stack.getTag().getInt("gridType");

        if (stack.hasTag()) {
            StackUtils.readItems(matrix, 1, stack.getTag());
        }
    }

    @Override
    public ITextComponent getTitle() {
        return new TranslationTextComponent("gui.universalgrid.universal_grid");
    }

    @Override
    public GridType getGridType() {
        if(gridType == 0) {
            return GridType.NORMAL;
        } else if(gridType == 1) {
            return GridType.CRAFTING;
        } else if(gridType == 2) {
            return GridType.FLUID;
        } else {
            return null;
        }
    }

    @Override
    public CraftingInventory getCraftingMatrix() {
        return matrix;
    }

    @Override
    public CraftResultInventory getCraftingResult() {
        return result;
    }

    @Override
    public IStorageCacheListener createListener(ServerPlayerEntity player) {
        if(getGridType() != GridType.FLUID) {
            return super.createListener(player);
        } else {
            return new FluidGridStorageCacheListener(player, this.getNetwork());
        }
    }

    @Nullable
    @Override
    public IStorageCache getStorageCache() {
        if(getGridType() != GridType.FLUID) {
            return super.getStorageCache();
        } else {
            INetwork network = this.getNetwork();
            return network != null ? network.getFluidStorageCache() : null;
        }
    }

    @Nullable
    @Override
    public IItemGridHandler getItemHandler() {
        if(getGridType() != GridType.FLUID) {
            return super.getItemHandler();
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public IFluidGridHandler getFluidHandler() {
        if(getGridType() != GridType.FLUID) {
            return null;
        } else {
            INetwork network = this.getNetwork();
            return network != null ? network.getFluidGridHandler() : null;
        }
    }

    @Override
    public void onCraftingMatrixChanged() {
        if (currentRecipe == null || !currentRecipe.matches(matrix, world)) {
            currentRecipe = world.getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, matrix, world).orElse(null);
        }

        if (currentRecipe == null) {
            result.setItem(0, ItemStack.EMPTY);
        } else {
            result.setItem(0, currentRecipe.assemble(matrix));
        }

        listeners.forEach(ICraftingGridListener::onCraftingMatrixChanged);

        if (!getStack().hasTag()) {
            getStack().setTag(new CompoundNBT());
        }

        StackUtils.writeItems(matrix, 1, getStack().getTag());
    }

    @Override
    public void onCrafted(PlayerEntity player, @Nullable IStackList<ItemStack> availableItems, @Nullable IStackList<ItemStack> usedItems) {
        UniversalGrid.RSAPI.getCraftingGridBehavior().onCrafted(this, currentRecipe, player, availableItems, usedItems);

        INetwork network = getNetwork();

        if (network != null) {
            network.getNetworkItemManager().drainEnergy(player, RSAddons.SERVER_CONFIG.getWirelessCraftingGrid().getCraftUsage());
        }
    }

    @Override
    public void onClear(PlayerEntity player) {
        INetwork network = getNetwork();

        if (network != null && network.getSecurityManager().hasPermission(Permission.INSERT, player)) {
            for (int i = 0; i < matrix.getContainerSize(); ++i) {
                ItemStack slot = matrix.getItem(i);

                if (!slot.isEmpty()) {
                    matrix.setItem(i, network.insertItem(slot, slot.getCount(), Action.PERFORM));

                    network.getItemStorageTracker().changed(player, slot.copy());
                }
            }

            network.getNetworkItemManager().drainEnergy(player, RSAddons.SERVER_CONFIG.getWirelessCraftingGrid().getClearUsage());
        }
    }

    @Override
    public void onCraftedShift(PlayerEntity player) {
        UniversalGrid.RSAPI.getCraftingGridBehavior().onCraftedShift(this, player);
    }

    @Override
    public void onRecipeTransfer(PlayerEntity player, ItemStack[][] recipe) {
        UniversalGrid.RSAPI.getCraftingGridBehavior().onRecipeTransfer(this, player, recipe);
    }

    @Override
    public void addCraftingListener(ICraftingGridListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeCraftingListener(ICraftingGridListener listener) {
        listeners.remove(listener);
    }
}