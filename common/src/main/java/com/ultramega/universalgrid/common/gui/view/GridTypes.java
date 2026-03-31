package com.ultramega.universalgrid.common.gui.view;

import com.refinedmods.refinedstorage.common.util.IdentifierUtil;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum GridTypes implements StringRepresentable {
    WIRELESS_GRID(Items.CHEST::getDefaultInstance,
        IdentifierUtil.createTranslation("block", "grid")),
    WIRELESS_CRAFTING_GRID(Items.CRAFTING_TABLE::getDefaultInstance,
        IdentifierUtil.createTranslation("block", "crafting_grid")),
    WIRELESS_AUTOCRAFTING_MONITOR(() -> com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getAutocrafters().getFirst().get().getDefaultInstance(),
        IdentifierUtil.createTranslation("block", "autocrafting_monitor"));

    private final Supplier<ItemStack> stackFactory;
    private final MutableComponent tooltip;
    @Nullable
    private ItemStack cachedStack;

    GridTypes(final Supplier<ItemStack> stackFactory, final MutableComponent tooltip) {
        this.stackFactory = stackFactory;
        this.tooltip = tooltip;
    }

    public ItemStack getIcon() {
        if (this.cachedStack == null) {
            this.cachedStack = this.stackFactory.get();
        }
        return this.cachedStack;
    }

    public MutableComponent getTooltip() {
        return this.tooltip;
    }

    @Override
    public String getSerializedName() {
        return this.getTooltip().getString();
    }

    public static GridTypes getNext(final GridTypes current) {
        final GridTypes[] values = GridTypes.values();
        final int index = (current.ordinal() + 1) % values.length;
        return values[index];
    }
}
