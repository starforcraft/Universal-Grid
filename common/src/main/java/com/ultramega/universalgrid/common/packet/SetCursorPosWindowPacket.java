package com.ultramega.universalgrid.common.packet;

import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.lwjgl.glfw.GLFW;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public record SetCursorPosWindowPacket(int cursorX, int cursorY) implements CustomPacketPayload {
    public static final Type<SetCursorPosWindowPacket> PACKET_TYPE = new Type<>(createUniversalGridIdentifier("set_cursor_pos_window"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetCursorPosWindowPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, SetCursorPosWindowPacket::cursorX,
        ByteBufCodecs.INT, SetCursorPosWindowPacket::cursorY,
        SetCursorPosWindowPacket::new
    );

    public static void handle(final SetCursorPosWindowPacket packet, final PacketContext ctx) {
        GLFW.glfwSetCursorPos(Minecraft.getInstance().getWindow().getWindow(), packet.cursorX(), packet.cursorY());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
