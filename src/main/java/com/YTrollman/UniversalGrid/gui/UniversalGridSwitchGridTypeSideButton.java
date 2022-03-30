package com.YTrollman.UniversalGrid.gui;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridSettingsUpdateMessage;
import com.YTrollman.UniversalGrid.registry.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.refinedmods.refinedstorage.container.GridContainer;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.KeyInputListener;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.SideButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class UniversalGridSwitchGridTypeSideButton extends SideButton {
    private final int gridType;

    public UniversalGridSwitchGridTypeSideButton(BaseScreen<GridContainer> screen, ItemStack stack) {
        super(screen);
        this.gridType = stack.getTag().getInt("gridType");
    }

    public String getTooltip() {
        return I18n.get("sidebutton.universalgrid.universal_grid.switch." + gridType);
    }

    protected void renderButtonIcon(MatrixStack matrixStack, int x, int y) {
        this.screen.bindTexture("universalgrid", "icons.png");
        this.screen.blit(matrixStack, x, y, 0, gridType * 16, 16, 16);
    }

    public void onPress() {
        UniversalGrid.NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(gridType != 2 ? gridType + 1 : 0));
        KeyInputListener.findAndOpen(ModItems.WIRELESS_UNIVERSAL_GRID, ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID);
    }
}
