package com.ultramega.universalgrid.common.interfaces;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface MixinTabRenderer {
    void universalgrid$renderGridTabs(GuiGraphicsExtractor graphics, boolean onlySelected);
}
