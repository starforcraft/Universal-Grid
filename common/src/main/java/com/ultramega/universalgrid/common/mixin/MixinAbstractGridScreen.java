package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.ClientUtils;
import com.ultramega.universalgrid.common.gui.GridTypeSideButtonWidget;

import com.refinedmods.refinedstorage.common.grid.screen.AbstractGridScreen;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage.common.support.widget.TextMarquee;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractGridScreen.class)
public abstract class MixinAbstractGridScreen extends AbstractBaseScreen {
    protected MixinAbstractGridScreen(final AbstractBaseContainerMenu menu,
                                      final Inventory playerInventory,
                                      final TextMarquee title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        final AbstractBaseContainerMenu containerMenu = (AbstractBaseContainerMenu) this.getMenu();
        if (ClientUtils.isUniversalGrid(containerMenu, minecraft.player)) {
            this.addSideButton(new GridTypeSideButtonWidget(containerMenu));
        }
    }
}
