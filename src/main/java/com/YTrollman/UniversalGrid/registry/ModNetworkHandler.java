package com.YTrollman.UniversalGrid.registry;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridRemoveTagMessage;
import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridSettingsUpdateMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworkHandler {
    private final String protocolVersion = Integer.toString(1);
    private final SimpleChannel handler = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(UniversalGrid.MOD_ID, "main_channel"))
            .clientAcceptedVersions(protocolVersion::equals)
            .serverAcceptedVersions(protocolVersion::equals)
            .networkProtocolVersion(() -> protocolVersion)
            .simpleChannel();

    public void register() {
        int id = 0;
        this.handler.registerMessage(id++, WirelessUniversalGridSettingsUpdateMessage.class, WirelessUniversalGridSettingsUpdateMessage::encode, WirelessUniversalGridSettingsUpdateMessage::decode, WirelessUniversalGridSettingsUpdateMessage::handle);
        this.handler.registerMessage(id++, WirelessUniversalGridRemoveTagMessage.class, WirelessUniversalGridRemoveTagMessage::encode, WirelessUniversalGridRemoveTagMessage::decode, WirelessUniversalGridRemoveTagMessage::handle);
    }

    public void sendToServer(Object message) {
        this.handler.sendToServer(message);
    }
}