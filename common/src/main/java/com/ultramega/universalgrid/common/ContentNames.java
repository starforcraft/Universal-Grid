package com.ultramega.universalgrid.common;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.MOD_ID;
import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridTranslationKey;

public final class ContentNames {
    public static final String MOD_TRANSLATION_KEY = "mod." + MOD_ID;
    public static final String OPEN_WIRELESS_UNIVERSAL_GRID_TRANSLATION_KEY = createUniversalGridTranslationKey(
        "key", "open_wireless_universal_grid"
    );

    private ContentNames() {
    }
}
