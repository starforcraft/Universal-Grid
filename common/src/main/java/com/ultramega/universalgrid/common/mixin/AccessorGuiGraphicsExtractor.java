package com.ultramega.universalgrid.common.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphicsExtractor.class)
public interface AccessorGuiGraphicsExtractor {
    @Accessor(value = "guiRenderState", remap = false)
    GuiRenderState getGuiRenderState();
}
