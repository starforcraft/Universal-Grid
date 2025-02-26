package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.refinedmods.refinedstorage.common.autocrafting.monitor.WirelessAutocraftingMonitorExtendedMenuProvider")
public class MixinWirelessAutocraftingMonitorExtendedMenuProvider implements MixinDisabledSlot {
    @Unique
    private SlotReference universalgrid$disabledSlot;

    @Override
    public void universalgrid$setDisabledSlot(final SlotReference disabledSlot) {
        this.universalgrid$disabledSlot = disabledSlot;
    }

    @Override
    public @Nullable SlotReference universalgrid$getDisabledSlot() {
        return universalgrid$disabledSlot;
    }

    @Inject(method = "createMenu", at = @At("TAIL"))
    public void createMenu(final int syncId,
                           final Inventory inventory,
                           final Player player,
                           final CallbackInfoReturnable<AbstractContainerMenu> cir) {
        ((MixinDisabledSlot) cir.getReturnValue()).universalgrid$setDisabledSlot(universalgrid$disabledSlot);
    }
}
