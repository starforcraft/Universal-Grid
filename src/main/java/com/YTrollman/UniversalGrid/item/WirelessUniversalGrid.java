package com.YTrollman.UniversalGrid.item;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridRemoveTagMessage;
import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridSettingsUpdateMessage;
import com.YTrollman.UniversalGrid.config.UniversalGridConfig;
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
import com.refinedmods.refinedstorage.blockentity.grid.WirelessGrid;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.util.StackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class WirelessUniversalGrid extends WirelessGrid {
    @Nullable
    private final MinecraftServer server;
    private final Level level;
    private Set<ICraftingGridListener> listeners = new HashSet<>();

    private AbstractContainerMenu craftingMenu = new AbstractContainerMenu(null, 0) {
        @Override
        public boolean stillValid(Player player) {
            return false;
        }

        @Override
        public ItemStack quickMoveStack(Player player, int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public void slotsChanged(Container container) {
            if (server != null) {
                onCraftingMatrixChanged();
            }
        }
    };
    private CraftingRecipe currentRecipe;
    private CraftingContainer matrix = new CraftingContainer(craftingMenu, 3, 3);
    private ResultContainer result = new ResultContainer();

    private final int gridType;

    public WirelessUniversalGrid(ItemStack stack, Level level, @Nullable MinecraftServer server, PlayerSlot slot) {
        super(stack, server, slot);

        this.server = server;
        this.level = level;

        gridType = stack.getTag().getInt("gridType");

        if (stack.hasTag()) {
            StackUtils.readItems(matrix, 1, stack.getTag());
        }
    }

    public void setCursorPos(ItemStack stack) {
        if(stack.hasTag()) {
            if(stack.getTag().contains("updateCursor")) {
                if(stack.getTag().getBoolean("updateCursor")) {
                    UniversalGrid.NETWORK_HANDLER.sendToServer(new WirelessUniversalGridRemoveTagMessage());
                } else {
                    return;
                }
            }

            if(stack.getTag().contains("cursorX") || stack.getTag().contains("cursorY")) {
                int cursorX = stack.getTag().getInt("cursorX");
                int cursorY = stack.getTag().getInt("cursorY");

                GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), cursorX, cursorY);
            }
        }
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.universalgrid.universal_grid");
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
    public CraftingContainer getCraftingMatrix() {
        return matrix;
    }

    @Override
    public ResultContainer getCraftingResult() {
        return result;
    }

    @Override
    public IStorageCacheListener createListener(ServerPlayer player) {
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
        if (currentRecipe == null || !currentRecipe.matches(matrix, level)) {
            currentRecipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, matrix, level).orElse(null);
        }

        if (currentRecipe == null) {
            result.setItem(0, ItemStack.EMPTY);
        } else {
            result.setItem(0, currentRecipe.assemble(matrix));
        }

        listeners.forEach(ICraftingGridListener::onCraftingMatrixChanged);

        if (!getStack().hasTag()) {
            getStack().setTag(new CompoundTag());
        }

        StackUtils.writeItems(matrix, 1, getStack().getTag());
    }

    @Override
    public void onCrafted(Player player, @Nullable IStackList<ItemStack> availableItems, @Nullable IStackList<ItemStack> usedItems) {
        UniversalGrid.RSAPI.getCraftingGridBehavior().onCrafted(this, currentRecipe, player, availableItems, usedItems);

        INetwork network = getNetwork();

        if (network != null) {
            network.getNetworkItemManager().drainEnergy(player, UniversalGridConfig.UNIVERSAL_GRID_CRAFT_USAGE.get());
        }
    }

    @Override
    public void onClear(Player player) {
        INetwork network = getNetwork();

        if (network != null && network.getSecurityManager().hasPermission(Permission.INSERT, player)) {
            for (int i = 0; i < matrix.getContainerSize(); ++i) {
                ItemStack slot = matrix.getItem(i);

                if (!slot.isEmpty()) {
                    matrix.setItem(i, network.insertItem(slot, slot.getCount(), Action.PERFORM));

                    network.getItemStorageTracker().changed(player, slot.copy());
                }
            }

            network.getNetworkItemManager().drainEnergy(player, UniversalGridConfig.UNIVERSAL_GRID_CLEAR_USAGE.get());
        }
    }

    @Override
    public void onCraftedShift(Player player) {
        UniversalGrid.RSAPI.getCraftingGridBehavior().onCraftedShift(this, player);
    }

    @Override
    public void onRecipeTransfer(Player player, ItemStack[][] recipe) {
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