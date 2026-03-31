package com.ultramega.universalgrid.common.radialmenu;

import com.ultramega.universalgrid.common.gui.view.GridBundle;
import com.ultramega.universalgrid.common.gui.view.GridTypes;

import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class GridSelectionOverlay extends RadialMenuRenderer<GridTypes> implements LayeredDraw.Layer {
    public static final GridSelectionOverlay INSTANCE = new GridSelectionOverlay();

    @Nullable
    private Consumer<GridTypes> onSelectGrid;
    @Nullable
    private GridBundle displayedGrid;

    @Override
    public void render(final GuiGraphics graphics, final DeltaTracker deltaTracker) {
        if (!this.isOpened()) {
            return;
        }

        super.render(graphics);
    }

    @Override
    public Component getTitle(final GridTypes gridType) {
        return Component.literal(gridType.getSerializedName());
    }

    @Override
    public List<GridTypes> getEntries() {
        return this.displayedGrid == null ? List.of() : this.displayedGrid.getEntries();
    }

    @Override
    public ItemStack getIcon(final GridTypes gridType) {
        return gridType.getIcon();
    }

    public void stopAndSelect() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        if (this.onSelectGrid != null) {
            final int selectionIndex = this.getElementUnderMouse();
            if (selectionIndex != -1) {
                final GridTypes gridType = this.getEntries().get(selectionIndex);
                this.onSelectGrid.accept(gridType);
            }
        }

        this.close(true);

        this.cancel();
    }

    public void open(final Consumer<GridTypes> onSelectGrid) {
        this.displayedGrid = new GridBundle();
        this.onSelectGrid = onSelectGrid;
    }

    public boolean isOpened() {
        return this.displayedGrid != null;
    }

    private void cancel() {
        this.close(true);
    }

    public void close(final boolean grabMouse) {
        if (grabMouse) {
            Minecraft.getInstance().mouseHandler.grabMouse();
        }
        this.displayedGrid = null;
        this.clearState();
    }
}
