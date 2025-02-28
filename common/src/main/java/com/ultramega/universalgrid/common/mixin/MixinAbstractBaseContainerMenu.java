package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.ClientUtils;
import com.ultramega.universalgrid.common.Platform;
import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;
import com.ultramega.universalgrid.common.interfaces.MixinGridType;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.packet.c2s.C2SPackets;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractBaseContainerMenu.class)
public abstract class MixinAbstractBaseContainerMenu extends AbstractContainerMenu implements MixinDisabledSlot, MixinGridType {
    @Shadow
    @Nullable
    protected SlotReference disabledSlot;

    protected MixinAbstractBaseContainerMenu(@Nullable final MenuType<?> menuType, final int containerId) {
        super(menuType, containerId);
    }

    @Unique
    @Override
    public @Nullable SlotReference universalgrid$getDisabledSlot() {
        return this.disabledSlot;
    }

    @Override
    public void universalgrid$setDisabledSlot(final SlotReference slotReference) {
        this.disabledSlot = slotReference;
    }

    @Unique
    @Override
    public GridTypes universalgrid$getGridType() {
        return Platform.getConfig().getWirelessUniversalGrid().getGridType();
    }

    @Unique
    @Override
    public void universalgrid$setGridType(final GridTypes gridType, @Nullable final Player player) {
        Platform.getConfig().getWirelessUniversalGrid().setGridType(gridType);

        // Save cursor position
        if (player != null && player.level().isClientSide() && this.disabledSlot != null) {
            ClientUtils.updateCursorPos(this.disabledSlot, gridType);

            // Re-open screen with new grid type (the old screen/container menu will automatically be closed)
            C2SPackets.sendUseSlotReferencedItem(this.disabledSlot);
        }
    }
}
