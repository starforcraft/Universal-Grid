package com.ultramega.universalgrid.fabric;

import com.ultramega.universalgrid.common.Platform;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jspecify.annotations.Nullable;

public class PlatformImpl implements Platform {
    @Nullable
    @Override
    public ScreenRectangle peekScissorStack(final GuiGraphicsExtractor graphics) {
        return graphics.scissorStack.peek();
    }
}
