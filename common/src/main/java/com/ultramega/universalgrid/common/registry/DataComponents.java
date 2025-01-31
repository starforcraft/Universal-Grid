package com.ultramega.universalgrid.common.registry;

import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridState;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.core.component.DataComponentType;

import static java.util.Objects.requireNonNull;

public final class DataComponents {
    public static final DataComponents INSTANCE = new DataComponents();

    @Nullable
    private Supplier<DataComponentType<WirelessUniversalGridState>> wirelessUniversalGridState;

    private DataComponents() {
    }

    public DataComponentType<WirelessUniversalGridState> getWirelessUniversalGridState() {
        return requireNonNull(wirelessUniversalGridState).get();
    }

    public void setWirelessUniversalGridState(
        @Nullable final Supplier<DataComponentType<WirelessUniversalGridState>> supplier
    ) {
        this.wirelessUniversalGridState = supplier;
    }
}
