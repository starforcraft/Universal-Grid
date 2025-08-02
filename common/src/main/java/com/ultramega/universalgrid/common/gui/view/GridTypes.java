package com.ultramega.universalgrid.common.gui.view;

import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum GridTypes {
    WIRELESS_GRID(Items.CHEST::getDefaultInstance,
        IdentifierUtil.createTranslation("item", "wireless_grid")),
    WIRELESS_CRAFTING_GRID(Items.CRAFTING_TABLE::getDefaultInstance,
        QuartzArsenalIdentifierUtil.createQuartzArsenalTranslation("item", "wireless_crafting_grid")),
    WIRELESS_AUTOCRAFTING_MONITOR(() -> com.refinedmods.refinedstorage.common.content.Items.INSTANCE.getAutocrafters().getFirst().get().getDefaultInstance(),
        IdentifierUtil.createTranslation("item", "wireless_autocrafting_monitor"));

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

    public static GridTypes getNext(final GridTypes current) {
        final GridTypes[] values = GridTypes.values();
        final int index = (current.ordinal() + 1) % values.length;
        return values[index];
    }
}
