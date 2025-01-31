package com.ultramega.universalgrid.common.mixin;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage.common.grid.screen.AbstractGridScreen;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;
import com.refinedmods.refinedstorage.common.support.widget.TextMarquee;

import com.ultramega.universalgrid.common.gui.GridTypeSideButtonWidget;
import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;
import com.ultramega.universalgrid.common.registry.Items;

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

        SlotReference gridSlot = ((MixinDisabledSlot)getMenu()).universalgrid$getDisabledSlot();
        if (gridSlot != null) {
            gridSlot.resolve(minecraft.player).ifPresent(stack -> {
                if (stack.is(Items.INSTANCE.getWirelessUniversalGrid()) || stack.is(Items.INSTANCE.getCreativeWirelessUniversalGrid())) {
                    this.addSideButton(new GridTypeSideButtonWidget((AbstractGridContainerMenu) this.getMenu()));
                }
            });
        }
    }
}
