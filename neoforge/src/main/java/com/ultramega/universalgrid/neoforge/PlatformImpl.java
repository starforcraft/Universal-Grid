package com.ultramega.universalgrid.neoforge;

import com.ultramega.universalgrid.common.Platform;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jspecify.annotations.Nullable;

public class PlatformImpl implements Platform {
    @Override
    public @Nullable ScreenRectangle peekScissorStack(final GuiGraphicsExtractor graphics) {
        return graphics.peekScissorStack();
    }
}
