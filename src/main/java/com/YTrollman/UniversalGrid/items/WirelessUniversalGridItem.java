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
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

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

    public static int getViewType(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("ViewType") ? stack.getTag().getInt("ViewType") : 0;
    }

    public static int getSortingType(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("SortingType") ? stack.getTag().getInt("SortingType") : 0;
    }

    public static int getSortingDirection(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("SortingDirection") ? stack.getTag().getInt("SortingDirection") : 1;
    }

    public static int getSearchBoxMode(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("SearchBoxMode") ? stack.getTag().getInt("SearchBoxMode") : 0;
    }

    public static int getTabSelected(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("TabSelected") ? stack.getTag().getInt("TabSelected") : -1;
    }

    public static Optional<UUID> getTabSelected2(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().hasUUID("TabSelected")) {
            return Optional.of(stack.getTag().getUUID("TabSelected"));
        }

        return Optional.empty();
    }


    public static void setTabSelected2(ItemStack stack, Optional<UUID> tabSelected) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }

        if (tabSelected.isPresent()) {
            stack.getTag().putUUID("TabSelected", tabSelected.get());
        } else {
            stack.getTag().remove("TabSelected" + "Least");
            stack.getTag().remove("TabSelected" + "Most");
        }
    }

    public static int getTabPage(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("TabPage") ? stack.getTag().getInt("TabPage") : 0;
    }

    public static int getSize(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("Size") ? stack.getTag().getInt("Size") : 0;
    }
}
