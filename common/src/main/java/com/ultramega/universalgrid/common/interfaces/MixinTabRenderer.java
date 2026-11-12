package com.ultramega.universalgrid.common.interfaces;

import java.util.List;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.Rect2i;

public interface MixinTabRenderer {
    void universalgrid$renderGridTabs(GuiGraphicsExtractor graphics, boolean onlySelected);

    List<Rect2i> universalgrid$getGridTabExclusionAreas();
}
