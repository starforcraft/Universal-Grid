package com.ultramega.universalgrid.common;

import com.ultramega.universalgrid.common.registry.DataComponents;
import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridState;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;

import static java.util.Objects.requireNonNull;

public final class Platform {
    @Nullable
    private static Supplier<Config> configProvider = null;

    private Platform() {
    }

    public static void setConfigProvider(final Supplier<Config> configProvider) {
        Platform.configProvider = configProvider;
    }

    public static Config getConfig() {
        return requireNonNull(configProvider, "Config isn't loaded yet").get();
    }

    public static void setWirelessUniversalGridState(final ItemStack stack, final int cursorX, final int cursorY, final boolean applyCursorPos) {
        stack.set(
            DataComponents.INSTANCE.getWirelessUniversalGridState(),
            new WirelessUniversalGridState(cursorX, cursorY, applyCursorPos)
        );
    }
}
