package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.ClientUtils;
import com.ultramega.universalgrid.common.Platform;
import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;
import com.ultramega.universalgrid.common.interfaces.MixinGridType;
import com.ultramega.universalgrid.common.registry.Items;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;

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
    public void universalgrid$setDisabledSlot(final SlotReference disabledSlot) {
        this.disabledSlot = disabledSlot;
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
        final SlotReference gridSlot = this.universalgrid$getDisabledSlot();
        if (player != null && gridSlot != null) {
            if (player.level().isClientSide()) {
                ClientUtils.updateCursorPos(gridSlot);
            }

            // Re-open screen with new grid type (the old screen/container will automatically be closed)
            RefinedStorageApi.INSTANCE.useSlotReferencedItem(
                player,
                Items.INSTANCE.getWirelessUniversalGrid(),
                Items.INSTANCE.getCreativeWirelessUniversalGrid()
            );
        }
    }
}
