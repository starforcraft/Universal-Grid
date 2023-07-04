package com.ultramega.universalgrid.gui;

import com.ultramega.universalgrid.UniversalGrid;
import com.ultramega.universalgrid.apiiml.network.grid.WirelessUniversalGridSettingsUpdateMessage;
import com.ultramega.universalgrid.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.container.GridContainerMenu;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.KeyInputListener;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.SideButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
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

    @Override
    protected String getSideButtonTooltip() {
        return I18n.get("sidebutton.universalgrid.universal_grid.switch." + gridType);
    }

    @Override
    protected void renderButtonIcon(GuiGraphics graphics, int x, int y) {
        graphics.blit(new ResourceLocation("universalgrid", "textures/icons.png"), x, y, 0, gridType * 16, 16, 16);
    }

    @Override
    public void onPress() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), x, y);

        UniversalGrid.NETWORK_HANDLER.sendToServer(new WirelessUniversalGridSettingsUpdateMessage(gridType != 2 ? gridType + 1 : 0, (int) Math.round(x.get()), (int) Math.round(y.get())));
        KeyInputListener.findAndOpen(ModItems.WIRELESS_UNIVERSAL_GRID.get(), ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID.get());
    }
}
