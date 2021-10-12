package com.YTrollman.UniversalGrid.handler;

import com.YTrollman.UniversalGrid.items.WirelessUniversalGrid;
import com.refinedmods.refinedstorage.api.network.grid.IGrid;
import com.refinedmods.refinedstorage.container.GridContainer;
import java.util.function.Supplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class WirelessUniversalGridSettingsUpdateMessage {
    private final int viewType;
    private final int sortingDirection;
    private final int sortingType;
    private final int searchBoxMode;
    private final int size;
    private final int tabSelected;
    private final int tabPage;

    public WirelessUniversalGridSettingsUpdateMessage(int viewType, int sortingDirection, int sortingType, int searchBoxMode, int size, int tabSelected, int tabPage) {
        this.viewType = viewType;
        this.sortingDirection = sortingDirection;
        this.sortingType = sortingType;
        this.searchBoxMode = searchBoxMode;
        this.size = size;
        this.tabSelected = tabSelected;
        this.tabPage = tabPage;
    }

    public static WirelessUniversalGridSettingsUpdateMessage decode(PacketBuffer buf) {
        return new WirelessUniversalGridSettingsUpdateMessage(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void encode(WirelessUniversalGridSettingsUpdateMessage message, PacketBuffer buf) {
        buf.writeInt(message.viewType);
        buf.writeInt(message.sortingDirection);
        buf.writeInt(message.sortingType);
        buf.writeInt(message.searchBoxMode);
        buf.writeInt(message.size);
        buf.writeInt(message.tabSelected);
        buf.writeInt(message.tabPage);
    }

    public static void handle(WirelessUniversalGridSettingsUpdateMessage message, Supplier<Context> ctx) {
        PlayerEntity player = ctx.get().getSender();
        if (player != null) {
            ctx.get().enqueueWork(() -> {
                if (player.containerMenu instanceof GridContainer) {
                    IGrid grid = ((GridContainer)player.containerMenu).getGrid();
                    if (grid instanceof WirelessUniversalGrid) {
                        ItemStack stack = ((WirelessUniversalGrid)grid).getStack();
                        if (!stack.hasTag()) {
                            stack.setTag(new CompoundNBT());
                        }

                        if (IGrid.isValidViewType(message.viewType)) {
                            stack.getTag().putInt("ViewType", message.viewType);
                        }

                        if (IGrid.isValidSortingDirection(message.sortingDirection)) {
                            stack.getTag().putInt("SortingDirection", message.sortingDirection);
                        }

                        if (IGrid.isValidSortingType(message.sortingType)) {
                            stack.getTag().putInt("SortingType", message.sortingType);
                        }

                        if (IGrid.isValidSearchBoxMode(message.searchBoxMode)) {
                            stack.getTag().putInt("SearchBoxMode", message.searchBoxMode);
                        }

                        if (IGrid.isValidSize(message.size)) {
                            stack.getTag().putInt("Size", message.size);
                        }

                        stack.getTag().putInt("TabSelected", message.tabSelected);
                        stack.getTag().putInt("TabPage", message.tabPage);
                    }
                }

            });
        }

        ctx.get().setPacketHandled(true);
    }
}
