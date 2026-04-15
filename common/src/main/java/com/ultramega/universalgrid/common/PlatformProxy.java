package com.ultramega.universalgrid.common;

import com.ultramega.universalgrid.common.registry.DataComponents;
import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridState;

import java.util.function.Supplier;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public class PlatformProxy implements Platform {
    @Nullable
    private static Supplier<Config> configProvider = null;

    @Nullable
    private Platform platform;

    public static void loadPlatform(final Platform platform) {
        final PlatformProxy proxy = (PlatformProxy) INSTANCE;
        if (proxy.platform != null) {
            throw new IllegalStateException("Platform already set");
        }
        proxy.platform = platform;
    }

    public static void setConfigProvider(final Supplier<Config> configProvider) {
        PlatformProxy.configProvider = configProvider;
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

    @Override
    public @Nullable ScreenRectangle peekScissorStack(final GuiGraphicsExtractor graphics) {
        return this.ensureLoaded().peekScissorStack(graphics);
    }

    private Platform ensureLoaded() {
        if (this.platform == null) {
            throw new IllegalStateException("Platform not loaded yet");
        }
        return this.platform;
    }
}
