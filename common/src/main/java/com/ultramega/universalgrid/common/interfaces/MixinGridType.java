package com.ultramega.universalgrid.common.interfaces;

import com.ultramega.universalgrid.common.gui.view.GridTypes;

public interface MixinGridType {
    GridTypes universalgrid$getGridType();

    void universalgrid$setGridType(final GridTypes gridType);
}
