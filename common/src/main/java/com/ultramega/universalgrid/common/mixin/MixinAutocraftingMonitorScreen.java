package com.ultramega.universalgrid.common.mixin;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.autocrafting.monitor.AutocraftingMonitorScreen;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;

import com.ultramega.universalgrid.common.gui.GridTypeSideButtonWidget;
import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;
import com.ultramega.universalgrid.common.registry.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AutocraftingMonitorScreen.class)
public abstract class MixinAutocraftingMonitorScreen extends AbstractBaseScreen {
    public MixinAutocraftingMonitorScreen(final AbstractContainerMenu menu,
                                          final Inventory playerInventory,
                                          final Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(final CallbackInfo ci) {
        // TODO: this doesn't work right now because disabledSlot isn't passed to the autocrafting monitor container menu
        SlotReference gridSlot = ((MixinDisabledSlot)getMenu()).universalgrid$getDisabledSlot();
        if (gridSlot != null) {
            gridSlot.resolve(minecraft.player).ifPresent(stack -> {
                if (stack.is(Items.INSTANCE.getWirelessUniversalGrid()) || stack.is(Items.INSTANCE.getCreativeWirelessUniversalGrid())) {
                    this.addSideButton(new GridTypeSideButtonWidget((AbstractBaseContainerMenu) this.getMenu()));
                }
            });
        }
    }
}
