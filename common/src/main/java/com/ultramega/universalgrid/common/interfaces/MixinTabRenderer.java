package com.ultramega.universalgrid.common.interfaces;

import java.util.List;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;

public interface MixinTabRenderer {
    void universalgrid$renderGridTabs(GuiGraphics graphics);

    List<Rect2i> universalgrid$getGridTabExclusionAreas();
}
