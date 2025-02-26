package com.ultramega.universalgrid.common.interfaces;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;

import javax.annotation.Nullable;

public interface MixinDisabledSlot {
    @Nullable
    SlotReference universalgrid$getDisabledSlot();

    void universalgrid$setDisabledSlot(SlotReference disabledSlot);
}
