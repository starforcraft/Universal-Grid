package com.YTrollman.UniversalGrid.items;

import com.YTrollman.UniversalGrid.config.UniversalGridConfig;
import com.YTrollman.UniversalGrid.network.WirelessUniversalGridNetworkItem;
import com.YTrollman.UniversalGrid.registry.ItemGroupUniversalGrid;
import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.item.NetworkItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class WirelessUniversalGridItem extends NetworkItem {
    public enum Type {
        NORMAL,
        CREATIVE
    }

    private final Type type;

    public WirelessUniversalGridItem(Type type) {
        super(
                new Item.Properties().tab(ItemGroupUniversalGrid.UNIVERSAL_GRID).stacksTo(1),
                type == Type.CREATIVE,
                () -> UniversalGridConfig.UNIVERSAL_GRID_CAPACITY.get()
        );

        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    @Nonnull
    public INetworkItem provide(INetworkItemManager handler, PlayerEntity player, ItemStack stack, int slotId) {
        return new WirelessUniversalGridNetworkItem(handler, player, stack, slotId);
    }
}
