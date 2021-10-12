package com.YTrollman.UniversalGrid.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class UniversalGridConfig {

    public static ForgeConfigSpec.IntValue UNIVERSAL_GRID_CAPACITY;
    public static ForgeConfigSpec.BooleanValue UNIVERSAL_GRID_USE_ENERGY;
    public static ForgeConfigSpec.IntValue UNIVERSAL_GRID_OPEN_USAGE;
    public static ForgeConfigSpec.IntValue UNIVERSAL_GRID_CLEAR_USAGE;
    public static ForgeConfigSpec.IntValue UNIVERSAL_GRID_CRAFT_USAGE;

    public static void init(ForgeConfigSpec.Builder common) {

            common.comment("Universal Grid Options");

        UNIVERSAL_GRID_CAPACITY = common
                .comment("\nUniversal Grid Capacity")
                .defineInRange("universalGridCapacity", 3200, 0, Integer.MAX_VALUE);

        UNIVERSAL_GRID_USE_ENERGY = common
                .comment("\nUniversal Grid Use Energy")
                .define("universalGridUseEnergy", true);

        UNIVERSAL_GRID_OPEN_USAGE = common
                .comment("\nUniversal Grid Open Usage")
                .defineInRange("universalGridOpenUsage", 30, 0, Integer.MAX_VALUE);

        UNIVERSAL_GRID_CLEAR_USAGE = common
                .comment("\nUniversal Grid Clear Usage")
                .defineInRange("universalGridClearUsage", 10, 0, Integer.MAX_VALUE);

        UNIVERSAL_GRID_CRAFT_USAGE = common
                .comment("\nUniversal Grid Craft Usage")
                .defineInRange("universalGridCraftUsage", 1, 0, Integer.MAX_VALUE);

        common.build();
    }
}
