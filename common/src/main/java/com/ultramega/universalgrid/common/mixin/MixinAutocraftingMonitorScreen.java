package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.interfaces.MixinTabRenderer;
import com.ultramega.universalgrid.common.packet.c2s.UpdateDisabledSlotPacket;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.autocrafting.monitor.AbstractAutocraftingMonitorContainerMenu;
import com.refinedmods.refinedstorage.common.autocrafting.monitor.AutocraftingMonitorScreen;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AutocraftingMonitorScreen.class)
public abstract class MixinAutocraftingMonitorScreen extends AbstractBaseScreen<AbstractAutocraftingMonitorContainerMenu> {
    protected MixinAutocraftingMonitorScreen(final AbstractAutocraftingMonitorContainerMenu menu,
                                             final Inventory playerInventory,
                                             final Component title,
                                             final int width,
                                             final int height) {
        super(menu, playerInventory, title, width, height);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(final CallbackInfo ci) {
        Platform.INSTANCE.sendPacketToServer(new UpdateDisabledSlotPacket());
    }

    @Override
    protected void extractDefaultBackground(final GuiGraphicsExtractor graphics) {
        if ((Object) this instanceof MixinTabRenderer tabRenderer) {
            tabRenderer.universalgrid$renderGridTabs(graphics, false);
        }
        super.extractDefaultBackground(graphics);
    }

    @Inject(method = "extractBackground",
        at = @At(
            value = "INVOKE",
            target = "Lcom/refinedmods/refinedstorage/common/support/AbstractBaseScreen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
            ordinal = 0,
            shift = At.Shift.AFTER
        ))
    private void extractBackgroundTail(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        if ((Object) this instanceof MixinTabRenderer tabRenderer) {
            tabRenderer.universalgrid$renderGridTabs(graphics, true);
        }
    }
}
