package com.ultramega.universalgrid.common;

import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;
import com.ultramega.universalgrid.common.packet.SetCursorPosStackPacket;
import com.ultramega.universalgrid.common.registry.Items;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;

import java.nio.DoubleBuffer;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

public class ClientUtils {
    private ClientUtils() {
    }

    public static void updateCursorPos(final SlotReference gridSlot) {
        final DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        final DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), x, y);

        com.refinedmods.refinedstorage.common.Platform.INSTANCE.sendPacketToServer(new SetCursorPosStackPacket(
            gridSlot,
            (int) Math.round(x.get()),
            (int) Math.round(y.get()),
            true));
    }

    public static boolean checkForSideButton(final AbstractBaseContainerMenu menu, @Nullable final Player player) {
        final SlotReference gridSlot = ((MixinDisabledSlot) menu).universalgrid$getDisabledSlot();
        if (gridSlot != null && player != null) {
            return gridSlot.resolve(player)
                .map(stack -> stack.is(Items.INSTANCE.getWirelessUniversalGrid()) || stack.is(Items.INSTANCE.getCreativeWirelessUniversalGrid()))
                .orElse(false);
        }

        return false;
    }
}
