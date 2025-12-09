package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.interfaces.MixinTabRenderer;

import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage.common.support.stretching.AbstractStretchingScreen;
import com.refinedmods.refinedstorage.common.support.stretching.ScreenSizeListener;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractStretchingScreen.class)
public abstract class MixinAbstractStretchingScreen<T extends AbstractBaseContainerMenu & ScreenSizeListener> extends AbstractBaseScreen<T> {
    protected MixinAbstractStretchingScreen(final T menu, final Inventory playerInventory, final Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "renderBackground", at = @At("HEAD"))
    private void renderBackground(final GuiGraphics graphics, final int mouseX, final int mouseY, final CallbackInfo ci) {
        if ((Object) this instanceof MixinTabRenderer tabRenderer) {
            tabRenderer.universalgrid$renderGridTabs(graphics);
        }
    }
}
