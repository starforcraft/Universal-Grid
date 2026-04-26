package com.ultramega.universalgrid.common.mixin;

import com.refinedmods.refinedstorage.common.RefinedStorageApiImpl;
import com.refinedmods.refinedstorage.common.support.slotreference.CompositePlayerSlotReferenceProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RefinedStorageApiImpl.class)
public interface AccessorRefinedStorageApiImpl {
    @Accessor(value = "playerSlotReferenceProvider", remap = false)
    CompositePlayerSlotReferenceProvider getPlayerSlotReferenceProvider();
}
