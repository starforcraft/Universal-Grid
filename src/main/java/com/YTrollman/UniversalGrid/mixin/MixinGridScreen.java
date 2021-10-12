package com.YTrollman.UniversalGrid.mixin;

import com.YTrollman.UniversalGrid.gui.UniversalGridSwitchGridSideButton;
import com.YTrollman.UniversalGrid.items.WirelessUniversalGrid;
import com.refinedmods.refinedstorage.api.network.grid.IGrid;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.grid.GridScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GridScreen.class, remap = false)
public abstract class MixinGridScreen extends BaseScreen {

    @Shadow
    public abstract IGrid getGrid();

    protected MixinGridScreen(Container container, int xSize, int ySize, PlayerInventory inventory, ITextComponent title) {
        super(container, xSize, ySize, inventory, title);
    }

    @Inject(method = "onPostInit", at = @At("HEAD"))
    public void onPostInit(int x, int y, CallbackInfo ci) {
        if(getGrid() instanceof WirelessUniversalGrid)
        {
            this.addSideButton(new UniversalGridSwitchGridSideButton(this));
        }
    }
}
