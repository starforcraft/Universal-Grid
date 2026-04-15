package com.ultramega.universalgrid.common;

import net.minecraft.resources.Identifier;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public final class ContentIds {
    public static final Identifier WIRELESS_UNIVERSAL_GRID =
        createUniversalGridIdentifier("wireless_universal_grid");
    public static final Identifier CREATIVE_WIRELESS_UNIVERSAL_GRID =
        createUniversalGridIdentifier("creative_wireless_universal_grid");

    private ContentIds() {
    }
}
