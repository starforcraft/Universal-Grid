package com.ultramega.universalgrid.common.registry;

import net.minecraft.client.KeyMapping;
import org.jspecify.annotations.Nullable;

public final class KeyMappings {
    public static final KeyMappings INSTANCE = new KeyMappings();

    @Nullable
    private KeyMapping openWirelessUniversalGrid;
    @Nullable
    private KeyMapping switchWirelessUniversalGridType;

    private KeyMappings() {
    }

    public void setOpenWirelessUniversalGrid(final KeyMapping openWirelessUniversalGrid) {
        this.openWirelessUniversalGrid = openWirelessUniversalGrid;
    }

    @Nullable
    public KeyMapping getOpenWirelessUniversalGrid() {
        return this.openWirelessUniversalGrid;
    }

    public void setSwitchWirelessUniversalGridType(final KeyMapping switchWirelessUniversalGridType) {
        this.switchWirelessUniversalGridType = switchWirelessUniversalGridType;
    }

    @Nullable
    public KeyMapping getSwitchWirelessUniversalGridType() {
        return this.switchWirelessUniversalGridType;
    }
}
