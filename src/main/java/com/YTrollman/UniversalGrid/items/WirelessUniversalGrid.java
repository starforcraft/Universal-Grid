package com.YTrollman.UniversalGrid.items;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.YTrollman.UniversalGrid.config.UniversalGridConfig;
import com.YTrollman.UniversalGrid.gui.UniversalGridSwitchGridSideButton;
import com.YTrollman.UniversalGrid.handler.WirelessUniversalGridSettingsUpdateMessage;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.grid.GridType;
import com.refinedmods.refinedstorage.api.network.grid.ICraftingGridListener;
import com.refinedmods.refinedstorage.api.network.grid.IGridTab;
import com.refinedmods.refinedstorage.api.network.grid.INetworkAwareGrid;
import com.refinedmods.refinedstorage.api.network.grid.handler.IFluidGridHandler;
import com.refinedmods.refinedstorage.api.network.grid.handler.IItemGridHandler;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCache;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCacheListener;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.api.util.IFilter;
import com.refinedmods.refinedstorage.api.util.IStackList;
import com.refinedmods.refinedstorage.apiimpl.storage.cache.listener.FluidGridStorageCacheListener;
import com.refinedmods.refinedstorage.apiimpl.storage.cache.listener.ItemGridStorageCacheListener;
import com.refinedmods.refinedstorage.inventory.item.FilterItemHandler;
import com.refinedmods.refinedstorage.item.NetworkItem;
import com.refinedmods.refinedstorage.item.WirelessGridItem;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.grid.GridScreen;
import com.refinedmods.refinedstorage.util.NetworkUtils;
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
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WirelessUniversalGrid implements INetworkAwareGrid {
    private ItemStack stack;
    @Nullable
    private final MinecraftServer server;
    private final World world;
    private final RegistryKey<World> nodeDimension;
    private final BlockPos nodePos;
    private final int slotId;
    private int viewType;
    private int sortingType;
    private int sortingDirection;
    private int searchBoxMode;
    private int tabSelected;
    private int tabPage;
    private int size;
    private final List<IFilter> filters = new ArrayList();
    private final List<IGridTab> tabs = new ArrayList();
    private final FilterItemHandler filter;
    private Set<ICraftingGridListener> listeners = new HashSet<>();

    public static int type;

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

    public WirelessUniversalGrid(ItemStack stack, World world, @Nullable MinecraftServer server, int slotId) {
        this.filter = (FilterItemHandler)(new FilterItemHandler(this.filters, this.tabs)).addListener((handler, slot, reading) -> {
            if (!this.stack.hasTag()) {
                this.stack.setTag(new CompoundNBT());
            }

            StackUtils.writeItems(handler, 0, this.stack.getTag());
        });
        this.stack = stack;
        this.server = server;
        this.world = world;
        this.nodeDimension = NetworkItem.getDimension(stack);
        this.nodePos = new BlockPos(NetworkItem.getX(stack), NetworkItem.getY(stack), NetworkItem.getZ(stack));
        this.slotId = slotId;
        this.viewType = WirelessGridItem.getViewType(stack);
        this.sortingType = WirelessGridItem.getSortingType(stack);
        this.sortingDirection = WirelessGridItem.getSortingDirection(stack);
        this.searchBoxMode = WirelessGridItem.getSearchBoxMode(stack);
        this.tabSelected = WirelessGridItem.getTabSelected(stack);
        this.tabPage = WirelessGridItem.getTabPage(stack);
        this.size = WirelessGridItem.getSize(stack);
        if (stack.hasTag()) {
            StackUtils.readItems(this.filter, 0, stack.getTag());
        }
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public GridType getGridType() {
        if(getGridMode() == 0)
        {
            return GridType.NORMAL;
        }
        else if(getGridMode() == 1)
        {
            return GridType.CRAFTING;
        }
        else if(getGridMode() == 2)
        {
            return GridType.FLUID;
        }
        return null;
    }

    @Nullable
    public INetwork getNetwork() {
        World world = this.server.getLevel(this.nodeDimension);
        return world != null ? NetworkUtils.getNetworkFromNode(NetworkUtils.getNodeFromTile(world.getBlockEntity(this.nodePos))) : null;
    }

    public IStorageCacheListener createListener(ServerPlayerEntity player) {
        if(getGridType() == GridType.FLUID)
        {
            return new FluidGridStorageCacheListener(player, this.getNetwork());
        }
        else
        {
            return new ItemGridStorageCacheListener(player, this.getNetwork());
        }
    }

    @Nullable
    public IStorageCache getStorageCache() {
        if(getGridType() == GridType.FLUID)
        {
            INetwork network = this.getNetwork();
            return network != null ? network.getFluidStorageCache() : null;
        }
        else
        {
            INetwork network = this.getNetwork();
            return network != null ? network.getItemStorageCache() : null;
        }
    }

    @Nullable
    public IItemGridHandler getItemHandler() {
        if(getGridType() != GridType.FLUID)
        {
            INetwork network = this.getNetwork();
            return network != null ? network.getItemGridHandler() : null;
        }
        else
        {
            return null;
        }
    }

    @Nullable
    public IFluidGridHandler getFluidHandler() {
        if(getGridType() == GridType.FLUID)
        {
            INetwork network = this.getNetwork();
            return network != null ? network.getFluidGridHandler() : null;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void addCraftingListener(ICraftingGridListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeCraftingListener(ICraftingGridListener listener) {
        listeners.remove(listener);
    }

    public ITextComponent getTitle() {
        return new TranslationTextComponent("gui.universalgrid.universal_grid");
    }

    public int getViewType() {
        return this.viewType;
    }

    public int getSortingType() {
        return this.sortingType;
    }

    public int getSortingDirection() {
        return this.sortingDirection;
    }

    public int getSearchBoxMode() {
        return this.searchBoxMode;
    }

    public int getTabSelected() {
        return this.tabSelected;
    }

    public int getTabPage() {
        return Math.min(this.tabPage, this.getTotalTabPages());
    }

    public int getTotalTabPages() {
        return (int)Math.floor((float)Math.max(0, this.tabs.size() - 1) / 5.0F);
    }

    public int getSize() {
        return this.size;
    }

    public void onViewTypeChanged(int type) {
        UniversalGrid.MOD_NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(type, this.getSortingDirection(), this.getSortingType(), this.getSearchBoxMode(), this.getSize(), this.getTabSelected(), this.getTabPage()));
        this.viewType = type;
        BaseScreen.executeLater(GridScreen.class, (grid) -> {
            grid.getView().sort();
        });
    }

    public void onSortingTypeChanged(int type) {
        UniversalGrid.MOD_NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(this.getViewType(), this.getSortingDirection(), type, this.getSearchBoxMode(), this.getSize(), this.getTabSelected(), this.getTabPage()));
        this.sortingType = type;
        BaseScreen.executeLater(GridScreen.class, (grid) -> {
            grid.getView().sort();
        });
    }

    public void onSortingDirectionChanged(int direction) {
        UniversalGrid.MOD_NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(this.getViewType(), direction, this.getSortingType(), this.getSearchBoxMode(), this.getSize(), this.getTabSelected(), this.getTabPage()));
        this.sortingDirection = direction;
        BaseScreen.executeLater(GridScreen.class, (grid) -> {
            grid.getView().sort();
        });
    }

    public void onSearchBoxModeChanged(int searchBoxMode) {
        UniversalGrid.MOD_NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(this.getViewType(), this.getSortingDirection(), this.getSortingType(), searchBoxMode, this.getSize(), this.getTabSelected(), this.getTabPage()));
        this.searchBoxMode = searchBoxMode;
    }

    public void onSizeChanged(int size) {
        UniversalGrid.MOD_NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(this.getViewType(), this.getSortingDirection(), this.getSortingType(), this.getSearchBoxMode(), size, this.getTabSelected(), this.getTabPage()));
        this.size = size;
        BaseScreen.executeLater(GridScreen.class, BaseScreen::init);
    }

    public void onTabSelectionChanged(int tab) {
        this.tabSelected = tab == this.tabSelected ? -1 : tab;
        UniversalGrid.MOD_NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(this.getViewType(), this.getSortingDirection(), this.getSortingType(), this.getSearchBoxMode(), this.getSize(), this.tabSelected, this.getTabPage()));
        BaseScreen.executeLater(GridScreen.class, (grid) -> {
            grid.getView().sort();
        });
    }

    public void onTabPageChanged(int page) {
        if (page >= 0 && page <= this.getTotalTabPages()) {
            UniversalGrid.MOD_NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(this.getViewType(), this.getSortingDirection(), this.getSortingType(), this.getSearchBoxMode(), this.getSize(), this.getTabSelected(), page));
            this.tabPage = page;
        }
    }

    public List<IFilter> getFilters() {
        return this.filters;
    }

    public List<IGridTab> getTabs() {
        return this.tabs;
    }

    public IItemHandlerModifiable getFilter() {
        return this.filter;
    }

    public CraftingInventory getCraftingMatrix() {
        return matrix;
    }

    public CraftResultInventory getCraftingResult() {
        return result;
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
        RSAddons.RSAPI.getCraftingGridBehavior().onCrafted(this, currentRecipe, player, availableItems, usedItems);

        INetwork network = getNetwork();

        if (network != null) {
            network.getNetworkItemManager().drainEnergy(player, UniversalGridConfig.UNIVERSAL_GRID_CRAFT_USAGE.get());
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

            network.getNetworkItemManager().drainEnergy(player, UniversalGridConfig.UNIVERSAL_GRID_CLEAR_USAGE.get());
        }
    }

    @Override
    public void onCraftedShift(PlayerEntity player) {
        RSAddons.RSAPI.getCraftingGridBehavior().onCraftedShift(this, player);
    }

    @Override
    public void onRecipeTransfer(PlayerEntity player, ItemStack[][] recipe) {
        RSAddons.RSAPI.getCraftingGridBehavior().onRecipeTransfer(this, player, recipe);
    }

    public boolean isGridActive() {
        return true;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public void onClosed(PlayerEntity player) {
        INetwork network = this.getNetwork();
        if (network != null) {
            network.getNetworkItemManager().close(player);
        }
    }

    public int getGridMode()
    {
        return type;
    }
}
