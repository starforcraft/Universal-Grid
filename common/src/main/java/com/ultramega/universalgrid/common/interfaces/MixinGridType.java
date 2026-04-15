package com.ultramega.universalgrid.common.interfaces;

import com.ultramega.universalgrid.common.gui.view.GridTypes;

import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public interface MixinGridType {
    GridTypes universalgrid$getGridType();

    void universalgrid$setGridType(GridTypes gridType, @Nullable Player player);
}
