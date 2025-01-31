package com.ultramega.universalgrid.common.wirelessuniversalgrid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record WirelessUniversalGridState(int cursorX, int cursorY, boolean applyCursorPos) {
    public static final Codec<WirelessUniversalGridState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("cursorX").forGetter(WirelessUniversalGridState::cursorX),
        Codec.INT.fieldOf("cursorY").forGetter(WirelessUniversalGridState::cursorY),
        Codec.BOOL.fieldOf("applyCursorPos").forGetter(WirelessUniversalGridState::applyCursorPos)
    ).apply(instance, WirelessUniversalGridState::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WirelessUniversalGridState> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.INT, WirelessUniversalGridState::cursorX,
            ByteBufCodecs.INT, WirelessUniversalGridState::cursorY,
            ByteBufCodecs.BOOL, WirelessUniversalGridState::applyCursorPos,
            WirelessUniversalGridState::new
        );
}
