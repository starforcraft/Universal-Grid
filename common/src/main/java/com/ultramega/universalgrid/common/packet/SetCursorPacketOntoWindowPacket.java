package com.ultramega.universalgrid.common.packet;

import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.lwjgl.glfw.GLFW;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record SetCursorPacketOntoWindowPacket(int cursorX, int cursorY) implements CustomPacketPayload {
    public static final Type<SetCursorPacketOntoWindowPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("set_cursor_onto_window"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetCursorPacketOntoWindowPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, SetCursorPacketOntoWindowPacket::cursorX,
        ByteBufCodecs.INT, SetCursorPacketOntoWindowPacket::cursorY,
        SetCursorPacketOntoWindowPacket::new
    );

    public static void handle(final SetCursorPacketOntoWindowPacket packet, final PacketContext ctx) {
        GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), packet.cursorX(), packet.cursorY());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
