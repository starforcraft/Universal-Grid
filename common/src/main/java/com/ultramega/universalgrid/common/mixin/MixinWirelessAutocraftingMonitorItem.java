package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;

import com.refinedmods.refinedstorage.common.api.support.network.item.NetworkItemContext;
import com.refinedmods.refinedstorage.common.api.support.slotreference.PlayerSlotReference;
import com.refinedmods.refinedstorage.common.autocrafting.monitor.WirelessAutocraftingMonitorItem;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WirelessAutocraftingMonitorItem.class)
public class MixinWirelessAutocraftingMonitorItem {
    @Unique
    private PlayerSlotReference universalgrid$disabledSlot;

    @Inject(method = "use", at = @At("HEAD"))
    protected void use(final Component name,
                       final ServerPlayer player,
                       final PlayerSlotReference playerSlotReference,
                       final NetworkItemContext context,
                       final CallbackInfo ci) {
        this.universalgrid$disabledSlot = playerSlotReference;
    }

    @ModifyArg(method = "use", at = @At(value = "INVOKE", target =
        "Lcom/refinedmods/refinedstorage/common/support/containermenu/MenuOpener;openMenu(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/MenuProvider;)V"))
    private MenuProvider addDisabledSlotReference(final MenuProvider provider) {
        if (provider instanceof MixinDisabledSlot extendedMenuProvider) {
            extendedMenuProvider.universalgrid$setDisabledSlot(this.universalgrid$disabledSlot);
        }
        return provider;
    }
}
