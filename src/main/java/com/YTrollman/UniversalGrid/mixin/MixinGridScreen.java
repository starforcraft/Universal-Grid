package com.YTrollman.UniversalGrid.mixin;

import com.YTrollman.UniversalGrid.gui.UniversalGridSwitchGridTypeSideButton;
import com.YTrollman.UniversalGrid.item.WirelessUniversalGrid;
import com.refinedmods.refinedstorage.api.network.grid.IGrid;
import com.refinedmods.refinedstorage.container.GridContainerMenu;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.grid.GridScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GridScreen.class, remap = false)
public abstract class MixinGridScreen extends BaseScreen {
    @Shadow public abstract IGrid getGrid();

    protected MixinGridScreen(AbstractContainerMenu containerMenu, int xSize, int ySize, Inventory inventory, Component title) {
        super(containerMenu, xSize, ySize, inventory, title);
    }

    /**
     * @author
     */
    @Inject(at = @At("HEAD"), method = "onPostInit")
    public void onPostInit(int x, int y, CallbackInfo ci) {
        if(getGrid() instanceof WirelessUniversalGrid) {
            this.addSideButton(new UniversalGridSwitchGridTypeSideButton(this, ((GridContainerMenu)this.menu).getPlayer().getMainHandItem()));
        }
    }
}
