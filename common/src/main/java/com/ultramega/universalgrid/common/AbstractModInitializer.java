package com.ultramega.universalgrid.common;

import com.ultramega.universalgrid.common.registry.DataComponents;
import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridState;

import com.refinedmods.refinedstorage.common.content.RegistryCallback;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public abstract class AbstractModInitializer {
    protected static boolean allowComponentsUpdateAnimation(final ItemStack oldStack, final ItemStack newStack) {
        return oldStack.getItem() != newStack.getItem();
    }

    protected final void registerDataComponents(final RegistryCallback<DataComponentType<?>> callback) {
        DataComponents.INSTANCE.setWirelessUniversalGridState(callback.register(
            createUniversalGridIdentifier("wireless_universal_grid_state"),
            () -> DataComponentType.<WirelessUniversalGridState>builder()
                .persistent(WirelessUniversalGridState.CODEC)
                .networkSynchronized(WirelessUniversalGridState.STREAM_CODEC)
                .build()));
    }
}
