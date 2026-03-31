package com.ultramega.universalgrid.common.gui.view;

import java.util.Arrays;
import java.util.List;

public final class GridBundle {
    private final List<GridTypes> entries;

    public GridBundle() {
        this.entries = Arrays.stream(GridTypes.values()).toList();
    }

    public List<GridTypes> getEntries() {
        return this.entries;
    }
}
