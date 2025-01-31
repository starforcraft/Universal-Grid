package com.ultramega.universalgrid.common.registry;

import javax.annotation.Nullable;

import net.minecraft.client.KeyMapping;

public final class KeyMappings {
    public static final KeyMappings INSTANCE = new KeyMappings();

    @Nullable
    private KeyMapping openWirelessUniversalGrid;

    private KeyMappings() {
    }

    public void setOpenWirelessUniversalGrid(final KeyMapping openWirelessUniversalGrid) {
        this.openWirelessUniversalGrid = openWirelessUniversalGrid;
    }

    @Nullable
    public KeyMapping getOpenWirelessUniversalGrid() {
        return openWirelessUniversalGrid;
    }
}
