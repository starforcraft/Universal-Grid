package com.YTrollman.UniversalGrid.apiiml.network.grid;

import com.YTrollman.UniversalGrid.item.WirelessUniversalGrid;
import com.refinedmods.refinedstorage.api.network.grid.IGrid;
import com.refinedmods.refinedstorage.container.GridContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WirelessUniversalGridSettingsUpdateMessage {
    private final int gridType;

    public WirelessUniversalGridSettingsUpdateMessage(int gridType) {
        this.gridType = gridType;
    }

    public static WirelessUniversalGridSettingsUpdateMessage decode(PacketBuffer buf) {
        return new WirelessUniversalGridSettingsUpdateMessage(
                buf.readInt()
        );
    }

    public static void encode(WirelessUniversalGridSettingsUpdateMessage message, PacketBuffer buf) {
        buf.writeInt(message.gridType);
    }

    public static void handle(WirelessUniversalGridSettingsUpdateMessage message, Supplier<NetworkEvent.Context> ctx) {
        PlayerEntity player = ctx.get().getSender();

        if (player != null) {
            ctx.get().enqueueWork(() -> {
                if (player.containerMenu instanceof GridContainer) {
                    IGrid grid = ((GridContainer) player.containerMenu).getGrid();

                    if (grid instanceof WirelessUniversalGrid) {
                        ItemStack stack = ((WirelessUniversalGrid) grid).getStack();

                        if (!stack.hasTag()) {
                            stack.setTag(new CompoundNBT());
                        }

                        CompoundNBT tag = stack.getTag();
                        if (tag == null) {
                            tag = new CompoundNBT();
                        }

                        tag.putInt("gridType", message.gridType);
                        stack.setTag(tag);

                        player.inventoryMenu.broadcastChanges();
                        ((GridContainer) player.containerMenu).initSlots();
                        player.containerMenu.broadcastChanges();

                        //double MouseX = Minecraft.getInstance().mouseHandler.xpos();
                        //double MouseY = Minecraft.getInstance().mouseHandler.ypos();

                        player.closeContainer();
                        stack.use(player.level, player, player.getUsedItemHand());

                        //Minecraft.getInstance().mouseHandler.xpos = MouseX;
                        //Minecraft.getInstance().mouseHandler.ypos = MouseY;
                    }
                }
            });
        }

        ctx.get().setPacketHandled(true);
    }
}