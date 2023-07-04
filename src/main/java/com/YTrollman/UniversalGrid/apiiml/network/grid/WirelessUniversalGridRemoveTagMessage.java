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

public class WirelessUniversalGridRemoveTagMessage {
    public static WirelessUniversalGridRemoveTagMessage decode(FriendlyByteBuf buf) {
        return new WirelessUniversalGridRemoveTagMessage();
    }

    public static void encode(WirelessUniversalGridRemoveTagMessage message, FriendlyByteBuf buf) {

    }

    public static void handle(WirelessUniversalGridRemoveTagMessage message, Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();

        if (player != null) {
            ctx.get().enqueueWork(() -> {
                if (player.containerMenu instanceof GridContainerMenu gridContainer) {
                    IGrid grid = gridContainer.getGrid();

                    if (grid instanceof WirelessUniversalGrid universalGrid) {
                        ItemStack stack = universalGrid.getStack();

                        if (!stack.hasTag()) {
                            return;
                        }

                        CompoundTag tag = stack.getTag();
                        tag.putBoolean("updateCursor", false);
                        stack.setTag(tag);
                    }
                }
            });
        }

        ctx.get().setPacketHandled(true);
    }
}