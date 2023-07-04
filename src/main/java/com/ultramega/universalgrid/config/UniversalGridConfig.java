package com.ultramega.universalgrid.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class UniversalGridConfig {

    public static ForgeConfigSpec.BooleanValue UNIVERSAL_GRID_USE_ENERGY;
    public static ForgeConfigSpec.IntValue UNIVERSAL_GRID_CAPACITY;
    public static ForgeConfigSpec.IntValue UNIVERSAL_GRID_OPEN_USAGE;
    public static ForgeConfigSpec.IntValue UNIVERSAL_GRID_CRAFT_USAGE;
    public static ForgeConfigSpec.IntValue UNIVERSAL_GRID_CLEAR_USAGE;

    public static void init(ForgeConfigSpec.Builder common) {
        common.comment("Universal Grid Options");

        UNIVERSAL_GRID_USE_ENERGY = common
                .comment("\nWhether the Wireless Universal Grid uses energy")
                .define("useEnergy", true);

        UNIVERSAL_GRID_CAPACITY = common
                .comment("\nThe energy capacity of the Universal Crafting Grid")
                .defineInRange("capacity", 6400, 0, Integer.MAX_VALUE);

        UNIVERSAL_GRID_OPEN_USAGE = common
                .comment("\nThe energy used by the Universal Crafting Grid to open")
                .defineInRange("openUsage", 60, 0, Integer.MAX_VALUE);

        UNIVERSAL_GRID_CRAFT_USAGE = common
                .comment("\nThe energy used by the Wireless Universal Grid to craft an item")
                .defineInRange("craftUsage", 2, 0, Integer.MAX_VALUE);

        UNIVERSAL_GRID_CLEAR_USAGE = common
                .comment("\nThe energy used by the Wireless Universal Grid to clear the crafting matrix")
                .defineInRange("clearUsage", 20, 0, Integer.MAX_VALUE);

        common.build();
    }
}
