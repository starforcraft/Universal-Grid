package com.YTrollman.UniversalGrid.handler;

import com.YTrollman.UniversalGrid.UniversalGrid;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
        //this.handler.registerMessage(id++, WirelessUniversalGridSettingsUpdateMessage2.class, WirelessUniversalGridSettingsUpdateMessage2::encode, WirelessUniversalGridSettingsUpdateMessage2::decode, WirelessUniversalGridSettingsUpdateMessage2::handle);
    }

    public void sendToServer(Object message) {
        this.handler.sendToServer(message);
    }

    public void sendTo(ServerPlayerEntity player, Object message) {
        if (!(player instanceof FakePlayer)) {
            this.handler.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
