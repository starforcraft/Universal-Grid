package com.ultramega.universalgrid.common;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public final class UniversalGridIdentifierUtil {
    public static final String MOD_ID = "universalgrid";

    private UniversalGridIdentifierUtil() {
    }

    public static ResourceLocation createUniversalGridIdentifier(final String value) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, value);
    }

    public static MutableComponent createUniversalGridTranslation(final String category, final String value) {
        return Component.translatable(createUniversalGridTranslationKey(category, value));
    }

    public static String createUniversalGridTranslationKey(final String category, final String value) {
        return String.format("%s.%s.%s", category, MOD_ID, value);
    }
}
