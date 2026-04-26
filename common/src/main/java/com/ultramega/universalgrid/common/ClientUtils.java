package com.ultramega.universalgrid.common;

import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;
import com.ultramega.universalgrid.common.packet.c2s.SetCursorPosStackPacket;
import com.ultramega.universalgrid.common.registry.Items;

import com.refinedmods.refinedstorage.common.api.support.slotreference.PlayerSlotReference;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class ClientUtils {
    private ClientUtils() {
    }

    public static void updateCursorPos(final PlayerSlotReference gridSlot, final GridTypes gridType) {
        final double[] x = new double[1];
        final double[] y = new double[1];

        GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().handle(), x, y);

        PlatformProxy.getConfig().getWirelessUniversalGrid().setGridType(gridType);

        com.refinedmods.refinedstorage.common.Platform.INSTANCE.sendPacketToServer(new SetCursorPosStackPacket(
            gridSlot,
            (int) Math.round(x[0]),
            (int) Math.round(y[0]),
            true));
    }

    public static boolean isUniversalGrid(final AbstractBaseContainerMenu menu, @Nullable final Player player) {
        final PlayerSlotReference gridSlot = ((MixinDisabledSlot) menu).universalgrid$getDisabledSlot();
        if (gridSlot != null && player != null) {
            final ItemStack stack = gridSlot.get(player);
            return stack.is(Items.INSTANCE.getWirelessUniversalGrid()) || stack.is(Items.INSTANCE.getCreativeWirelessUniversalGrid());
        }

        return false;
    }
}
