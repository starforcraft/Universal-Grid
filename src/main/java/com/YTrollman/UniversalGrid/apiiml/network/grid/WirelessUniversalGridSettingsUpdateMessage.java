package com.YTrollman.UniversalGrid.apiiml.network.grid;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.YTrollman.UniversalGrid.item.WirelessUniversalGrid;
import com.refinedmods.refinedstorage.api.network.grid.IGrid;
import com.refinedmods.refinedstorage.container.GridContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WirelessUniversalGridSettingsUpdateMessage {
    private final int gridType;
    private final int cursorX;
    private final int cursorY;

    public WirelessUniversalGridSettingsUpdateMessage(int gridType, int cursorX, int cursorY) {
        this.gridType = gridType;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
    }

    public static WirelessUniversalGridSettingsUpdateMessage decode(FriendlyByteBuf buf) {
        return new WirelessUniversalGridSettingsUpdateMessage(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void encode(WirelessUniversalGridSettingsUpdateMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.gridType).writeInt(message.cursorX).writeInt(message.cursorY);
    }

    public static void handle(WirelessUniversalGridSettingsUpdateMessage message, Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();

        if (player != null) {
            ctx.get().enqueueWork(() -> {
                if (player.containerMenu instanceof GridContainerMenu gridContainer) {
                    IGrid grid = gridContainer.getGrid();

                    if (grid instanceof WirelessUniversalGrid universalGrid) {
                        ItemStack stack = universalGrid.getStack();

                        if (!stack.hasTag()) {
                            stack.setTag(new CompoundTag());
                        }

                        CompoundTag tag = stack.getTag();
                        if (tag == null) {
                            tag = new CompoundTag();
                        }

                        tag.putInt("gridType", message.gridType);
                        tag.putInt("cursorX", message.cursorX);
                        tag.putInt("cursorY", message.cursorY);
                        tag.putBoolean("updateCursor", true);
                        stack.setTag(tag);

                        player.inventoryMenu.broadcastChanges();
                        gridContainer.initSlots();
                        player.containerMenu.broadcastChanges();

                        player.closeContainer();
                    }
                }
            });
        }

        ctx.get().setPacketHandled(true);
    }
}