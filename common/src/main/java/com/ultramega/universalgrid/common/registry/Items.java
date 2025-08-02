package com.ultramega.universalgrid.common.registry;

import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridItem;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public final class Items {
    public static final Items INSTANCE = new Items();

    @Nullable
    private Supplier<WirelessUniversalGridItem> wirelessUniversalGrid;
    @Nullable
    private Supplier<WirelessUniversalGridItem> creativeWirelessUniversalGrid;

    private Items() {
    }

    public WirelessUniversalGridItem getWirelessUniversalGrid() {
        return requireNonNull(this.wirelessUniversalGrid).get();
    }

    public void setWirelessUniversalGrid(final Supplier<WirelessUniversalGridItem> supplier) {
        this.wirelessUniversalGrid = supplier;
    }

    public WirelessUniversalGridItem getCreativeWirelessUniversalGrid() {
        return requireNonNull(this.creativeWirelessUniversalGrid).get();
    }

    public void setCreativeWirelessUniversalGrid(final Supplier<WirelessUniversalGridItem> supplier) {
        this.creativeWirelessUniversalGrid = supplier;
    }
}
