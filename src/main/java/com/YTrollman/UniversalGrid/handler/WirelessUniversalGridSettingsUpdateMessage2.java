package com.YTrollman.UniversalGrid.handler;

import com.refinedmods.refinedstorage.container.CraftingMonitorContainer;
import com.refinedmods.refinedstorage.tile.craftingmonitor.WirelessCraftingMonitor;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class WirelessUniversalGridSettingsUpdateMessage2 {
    private final Optional<UUID> tabSelected;
    private final int tabPage;

    public WirelessUniversalGridSettingsUpdateMessage2(Optional<UUID> tabSelected, int tabPage) {
        this.tabSelected = tabSelected;
        this.tabPage = tabPage;
    }

    public static WirelessUniversalGridSettingsUpdateMessage2 decode(PacketBuffer buf) {
        Optional<UUID> tabSelected = Optional.empty();
        if (buf.readBoolean()) {
            tabSelected = Optional.of(buf.readUUID());
        }

        int tabPage = buf.readInt();
        return new WirelessUniversalGridSettingsUpdateMessage2(tabSelected, tabPage);
    }

    public static void encode(WirelessUniversalGridSettingsUpdateMessage2 message, PacketBuffer buf) {
        buf.writeBoolean(message.tabSelected.isPresent());
        message.tabSelected.ifPresent(buf::writeUUID);
        buf.writeInt(message.tabPage);
    }

    public static void handle(WirelessUniversalGridSettingsUpdateMessage2 message, Supplier<Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        if (player != null) {
            ctx.get().enqueueWork(() -> {
                if (player.containerMenu instanceof CraftingMonitorContainer) {
                    ((WirelessCraftingMonitor)((CraftingMonitorContainer)player.containerMenu).getCraftingMonitor()).setSettings(message.tabSelected, message.tabPage);
                }

            });
        }

        ctx.get().setPacketHandled(true);
    }
}

