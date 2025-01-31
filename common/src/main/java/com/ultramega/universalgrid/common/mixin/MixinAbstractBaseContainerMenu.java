package com.ultramega.universalgrid.common.mixin;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;

import com.ultramega.universalgrid.common.Platform;
import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;
import com.ultramega.universalgrid.common.interfaces.MixinGridType;
import com.ultramega.universalgrid.common.packet.SetCursorPacketOntoStackPacket;
import com.ultramega.universalgrid.common.registry.Items;

import java.nio.DoubleBuffer;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractBaseContainerMenu.class)
public class MixinAbstractBaseContainerMenu implements MixinDisabledSlot, MixinGridType {
    @Shadow @Nullable protected SlotReference disabledSlot;

    @Unique
    @Override
    public @Nullable SlotReference universalgrid$getDisabledSlot() {
        return this.disabledSlot;
    }

    @Unique
    @Override
    public GridTypes universalgrid$getGridType() {
        return Platform.getConfig().getWirelessUniversalGrid().getGridType();
    }

    @Unique
    @Override
    public void universalgrid$setGridType(final GridTypes gridType) {
        Platform.getConfig().getWirelessUniversalGrid().setGridType(gridType);

        Player player = Minecraft.getInstance().player;

        // Save cursor position
        SlotReference gridSlot = this.universalgrid$getDisabledSlot();
        if (gridSlot != null) {
            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

            GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), x, y);

            com.refinedmods.refinedstorage.common.Platform.INSTANCE.sendPacketToServer(new SetCursorPacketOntoStackPacket(
                gridSlot,
                (int) Math.round(x.get()),
                (int) Math.round(y.get()),
                true));
        }

        if (player != null) {
            // Re-open screen with new grid type (the old screen/container will automatically be closed)
            RefinedStorageApi.INSTANCE.useSlotReferencedItem(
                player,
                Items.INSTANCE.getWirelessUniversalGrid(),
                Items.INSTANCE.getCreativeWirelessUniversalGrid()
            );
        }
    }
}
