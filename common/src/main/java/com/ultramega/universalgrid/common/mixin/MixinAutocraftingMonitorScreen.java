package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.packet.UpdateDisabledSlotPacket;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.autocrafting.monitor.AutocraftingMonitorScreen;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;

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
        Platform.INSTANCE.sendPacketToServer(new UpdateDisabledSlotPacket());
    }
}
