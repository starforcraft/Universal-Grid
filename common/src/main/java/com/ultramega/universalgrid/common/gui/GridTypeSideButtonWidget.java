package com.ultramega.universalgrid.common.gui;

import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.interfaces.MixinGridType;

import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.widget.AbstractSideButtonWidget;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import com.refinedmods.refinedstorage.quartzarsenal.common.QuartzArsenalIdentifierUtil;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;
import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridTranslation;

public class GridTypeSideButtonWidget extends AbstractSideButtonWidget {
    private static final MutableComponent TITLE = createUniversalGridTranslation("gui", "grid.grid_type");
    private static final ResourceLocation WIRELESS_GRID = createUniversalGridIdentifier("widget/side_button/wireless_grid/inactive");
    private static final ResourceLocation WIRELESS_CRAFTING_GRID = createUniversalGridIdentifier("widget/side_button/wireless_crafting_grid/inactive");
    private static final ResourceLocation WIRELESS_AUTOCRAFTING_MONITOR = createUniversalGridIdentifier("widget/side_button/wireless_autocrafting_monitor/inactive");
    private static final List<MutableComponent> SUBTEXT_WIRELESS_GRID = List.of(
        IdentifierUtil.createTranslation("item", "wireless_grid")
            .withStyle(ChatFormatting.GRAY));
    private static final List<MutableComponent> SUBTEXT_WIRELESS_CRAFTING_GRID = List.of(
        QuartzArsenalIdentifierUtil.createQuartzArsenalTranslation("item", "wireless_crafting_grid")
            .withStyle(ChatFormatting.GRAY));
    private static final List<MutableComponent> SUBTEXT_AUTOCRAFTING_MONITOR = List.of(
        IdentifierUtil.createTranslation("item", "wireless_autocrafting_monitor")
            .withStyle(ChatFormatting.GRAY));

    private final AbstractBaseContainerMenu menu;

    public GridTypeSideButtonWidget(final AbstractBaseContainerMenu menu) {
        super(createPressAction(menu));
        this.menu = menu;
    }

    private static OnPress createPressAction(final AbstractBaseContainerMenu menu) {
        return btn -> ((MixinGridType) menu).universalgrid$setGridType(toggle(((MixinGridType) menu).universalgrid$getGridType()),
            Minecraft.getInstance().player);
    }

    private static GridTypes toggle(final GridTypes gridType) {
        return switch (gridType) {
            case WIRELESS_GRID -> GridTypes.WIRELESS_CRAFTING_GRID;
            case WIRELESS_CRAFTING_GRID -> GridTypes.WIRELESS_AUTOCRAFTING_MONITOR;
            case WIRELESS_AUTOCRAFTING_MONITOR -> GridTypes.WIRELESS_GRID;
        };
    }

    @Override
    protected ResourceLocation getSprite() {
        return switch (((MixinGridType) menu).universalgrid$getGridType()) {
            case WIRELESS_GRID -> WIRELESS_GRID;
            case WIRELESS_CRAFTING_GRID -> WIRELESS_CRAFTING_GRID;
            case WIRELESS_AUTOCRAFTING_MONITOR -> WIRELESS_AUTOCRAFTING_MONITOR;
        };
    }

    @Override
    protected MutableComponent getTitle() {
        return TITLE;
    }

    @Override
    protected List<MutableComponent> getSubText() {
        return switch (((MixinGridType) menu).universalgrid$getGridType()) {
            case WIRELESS_GRID -> SUBTEXT_WIRELESS_GRID;
            case WIRELESS_CRAFTING_GRID -> SUBTEXT_WIRELESS_CRAFTING_GRID;
            case WIRELESS_AUTOCRAFTING_MONITOR -> SUBTEXT_AUTOCRAFTING_MONITOR;
        };
    }
}
