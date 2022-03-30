package com.YTrollman.UniversalGrid.apiiml.network.grid;

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

    public WirelessUniversalGridSettingsUpdateMessage(int gridType) {
        this.gridType = gridType;
    }

    public static WirelessUniversalGridSettingsUpdateMessage decode(FriendlyByteBuf buf) {
        return new WirelessUniversalGridSettingsUpdateMessage(
                buf.readInt()
        );
    }

    public static void encode(WirelessUniversalGridSettingsUpdateMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.gridType);
    }

    public static void handle(WirelessUniversalGridSettingsUpdateMessage message, Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();

        if (player != null) {
            ctx.get().enqueueWork(() -> {
                if (player.containerMenu instanceof GridContainerMenu) {
                    IGrid grid = ((GridContainerMenu) player.containerMenu).getGrid();

                    if (grid instanceof WirelessUniversalGrid) {
                        ItemStack stack = ((WirelessUniversalGrid) grid).getStack();

                        if (!stack.hasTag()) {
                            stack.setTag(new CompoundTag());
                        }

                        CompoundTag tag = stack.getTag();
                        if (tag == null) {
                            tag = new CompoundTag();
                        }

                        tag.putInt("gridType", message.gridType);
                        stack.setTag(tag);

                        player.inventoryMenu.broadcastChanges();
                        ((GridContainerMenu) player.containerMenu).initSlots();
                        player.containerMenu.broadcastChanges();

                        player.closeContainer();
                    }
                }
            });
        }

        ctx.get().setPacketHandled(true);
    }
}