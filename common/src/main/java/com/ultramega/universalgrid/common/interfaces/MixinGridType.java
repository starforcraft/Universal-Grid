package com.ultramega.universalgrid.common.interfaces;

import com.ultramega.universalgrid.common.gui.view.GridTypes;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;

public interface MixinGridType {
    GridTypes universalgrid$getGridType();

    void universalgrid$setGridType(GridTypes gridType, @Nullable Player player);
}
