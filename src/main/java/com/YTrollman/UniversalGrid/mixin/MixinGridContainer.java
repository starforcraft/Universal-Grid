package com.YTrollman.UniversalGrid.mixin;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.YTrollman.UniversalGrid.items.WirelessUniversalGrid;
import com.refinedmods.refinedstorage.api.network.grid.IGrid;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCache;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCacheListener;
import com.refinedmods.refinedstorage.container.BaseContainer;
import com.refinedmods.refinedstorage.container.GridContainer;
import com.refinedmods.refinedstorage.tile.BaseTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(value = GridContainer.class, remap = false)
public abstract class MixinGridContainer extends BaseContainer {

    @Shadow public abstract IGrid getGrid();

    private IGrid grid;
    private IStorageCache storageCache;
    private IStorageCacheListener storageCacheListener;

    protected MixinGridContainer(@Nullable ContainerType<?> type, @Nullable BaseTile tile, PlayerEntity player, int windowId) {
        super(type, tile, player, windowId);
    }

    /**
     * @author
     */
    @Overwrite
    public void broadcastChanges() {
        if(getGrid() instanceof WirelessUniversalGrid)
        {
            if (!getPlayer().level.isClientSide) {
                if (grid.getStorageCache() == null) {
                    if (storageCacheListener != null) {
                        storageCache.removeListener(storageCacheListener);
                        storageCacheListener = null;
                        storageCache = null;
                    }
                } else {
                    storageCacheListener = grid.createListener((ServerPlayerEntity)this.getPlayer());
                    storageCache = grid.getStorageCache();
                    storageCache.addListener(storageCacheListener);
                }
            }
        }
        else
        {
            if (!this.getPlayer().level.isClientSide) {
                if (this.grid.getStorageCache() == null) {
                    if (this.storageCacheListener != null) {
                        this.storageCache.removeListener(this.storageCacheListener);
                        this.storageCacheListener = null;
                        this.storageCache = null;
                    }
                } else if (this.storageCacheListener == null) {
                    this.storageCacheListener = this.grid.createListener((ServerPlayerEntity)this.getPlayer());
                    this.storageCache = this.grid.getStorageCache();
                    this.storageCache.addListener(this.storageCacheListener);
                }
            }
        }

        super.broadcastChanges();
    }
}
