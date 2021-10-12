package com.YTrollman.UniversalGrid.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.refinedmods.refinedstorage.container.GridContainer;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.SideButton;
import net.minecraft.client.resources.I18n;

public class UniversalGridSwitchGridSideButton extends SideButton {

    public static int type;

    public UniversalGridSwitchGridSideButton(BaseScreen<GridContainer> screen) {
        super(screen);
    }

    public String getTooltip() {
        return I18n.get("sidebutton.universalgrid.universal_grid.switch." + type);
    }

    protected void renderButtonIcon(MatrixStack matrixStack, int x, int y) {
        this.screen.bindTexture("universalgrid", "icons.png");
        this.screen.blit(matrixStack, x, y, 0, type * 16, 16, 16);
    }

    public void onPress() {
        type = type != 2 ? type + 1 : 0;

        screen.init();
        screen.getMenu().broadcastChanges();
    }
}
