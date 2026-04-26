package com.ultramega.universalgrid.common.interfaces;

import com.refinedmods.refinedstorage.common.api.support.slotreference.PlayerSlotReference;

import org.jspecify.annotations.Nullable;

public interface MixinDisabledSlot {
    @Nullable
    PlayerSlotReference universalgrid$getDisabledSlot();

    void universalgrid$setDisabledSlot(PlayerSlotReference disabledSlot);
}
