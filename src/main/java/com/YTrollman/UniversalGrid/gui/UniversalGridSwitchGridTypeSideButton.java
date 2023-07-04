package com.YTrollman.UniversalGrid.gui;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridSettingsUpdateMessage;
import com.YTrollman.UniversalGrid.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.container.GridContainerMenu;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.KeyInputListener;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.SideButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

public class UniversalGridSwitchGridTypeSideButton extends SideButton {
    private final int gridType;

    public UniversalGridSwitchGridTypeSideButton(BaseScreen<GridContainerMenu> screen, ItemStack stack) {
        super(screen);
        this.gridType = stack.getTag().getInt("gridType");
    }

    public String getTooltip() {
        return I18n.get("sidebutton.universalgrid.universal_grid.switch." + gridType);
    }

    protected void renderButtonIcon(PoseStack poseStack, int x, int y) {
        this.screen.bindTexture("universalgrid", "icons.png");
        this.screen.blit(poseStack, x, y, 0, gridType * 16, 16, 16);
    }

    public void onPress() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), x, y);

        UniversalGrid.NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(gridType != 2 ? gridType + 1 : 0, (int) Math.round(x.get()), (int) Math.round(y.get())));
        KeyInputListener.findAndOpen(ModItems.WIRELESS_UNIVERSAL_GRID.get(), ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID.get());
    }
}
