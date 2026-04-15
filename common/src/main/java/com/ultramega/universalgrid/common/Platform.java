package com.ultramega.universalgrid.common;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jspecify.annotations.Nullable;

public interface Platform {
    Platform INSTANCE = new PlatformProxy();

    @Nullable
    ScreenRectangle peekScissorStack(GuiGraphicsExtractor graphics);
}
