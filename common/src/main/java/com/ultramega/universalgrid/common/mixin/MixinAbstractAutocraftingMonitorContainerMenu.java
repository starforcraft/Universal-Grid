package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.interfaces.MixinPlayer;

import com.refinedmods.refinedstorage.common.autocrafting.monitor.AbstractAutocraftingMonitorContainerMenu;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractAutocraftingMonitorContainerMenu.class)
public class MixinAbstractAutocraftingMonitorContainerMenu implements MixinPlayer {
    @Final
    @Shadow
    private Player player;

    @Override
    public Player universalgrid$getPlayer() {
        return this.player;
    }
}
