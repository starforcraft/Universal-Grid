package com.ultramega.universalgrid.common;

import net.minecraft.resources.ResourceLocation;

public final class UniversalGridIdentifierUtil {
    public static final String MOD_ID = "universalgrid";

    private UniversalGridIdentifierUtil() {
    }

    public static ResourceLocation createUniversalGridIdentifier(final String value) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, value);
    }

    public static String createUniversalGridTranslationKey(final String category, final String value) {
        return String.format("%s.%s.%s", category, MOD_ID, value);
    }
}
