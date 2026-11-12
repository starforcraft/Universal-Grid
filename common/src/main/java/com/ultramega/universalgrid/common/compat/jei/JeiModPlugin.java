package com.ultramega.universalgrid.common.compat.jei;

import com.ultramega.universalgrid.common.interfaces.MixinTabRenderer;

import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

@JeiPlugin
public final class JeiModPlugin implements IModPlugin {
    private static final ResourceLocation ID = createUniversalGridIdentifier("jei_plugin");

    @Override
    public void registerGuiHandlers(final IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(AbstractBaseScreen.class,
            new IGuiContainerHandler<AbstractBaseScreen<?>>() {
                @Override
                public List<Rect2i> getGuiExtraAreas(final AbstractBaseScreen<?> screen) {
                    if (screen instanceof MixinTabRenderer tabRenderer) {
                        return tabRenderer.universalgrid$getGridTabExclusionAreas();
                    }
                    return List.of();
                }
            });
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
