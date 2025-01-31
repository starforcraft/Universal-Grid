package com.ultramega.universalgrid.common.registry;

import java.util.function.Consumer;

import net.minecraft.world.item.ItemStack;

public final class CreativeModeTabItems {
    private CreativeModeTabItems() {
    }

    public static void addItems(final Consumer<ItemStack> consumer) {
        consumer.accept(Items.INSTANCE.getWirelessUniversalGrid().getDefaultInstance());
        consumer.accept(Items.INSTANCE.getWirelessUniversalGrid().createAtEnergyCapacity());
        consumer.accept(Items.INSTANCE.getCreativeWirelessUniversalGrid().getDefaultInstance());
    }
}
