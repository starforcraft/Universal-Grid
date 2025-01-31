package com.ultramega.universalgrid.common;

import com.ultramega.universalgrid.common.gui.view.GridTypes;

public interface Config {
    WirelessUniversalGridEntry getWirelessUniversalGrid();

    interface WirelessUniversalGridEntry {
        long getEnergyCapacity();

        GridTypes getGridType();

        void setGridType(GridTypes gridType);
    }
}
