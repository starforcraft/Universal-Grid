package com.ultramega.universalgrid.neoforge;

import com.ultramega.universalgrid.common.Config;
import com.ultramega.universalgrid.common.DefaultEnergyUsage;
import com.ultramega.universalgrid.common.gui.view.GridTypes;

import net.neoforged.neoforge.common.ModConfigSpec;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridTranslationKey;

public class ConfigImpl implements Config {
    private final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    private final ModConfigSpec spec;
    private final WirelessUniversalGridEntry wirelessUniversalGrid;

    public ConfigImpl() {
        wirelessUniversalGrid = new WirelessUniversalGridEntryImpl();
        spec = builder.build();
    }

    public ModConfigSpec getSpec() {
        return spec;
    }

    @Override
    public WirelessUniversalGridEntry getWirelessUniversalGrid() {
        return wirelessUniversalGrid;
    }

    private static String translationKey(final String value) {
        return createUniversalGridTranslationKey("text.autoconfig", "option." + value);
    }

    private class WirelessUniversalGridEntryImpl implements WirelessUniversalGridEntry {
        private final ModConfigSpec.LongValue energyCapacity;
        private final ModConfigSpec.EnumValue<GridTypes> gridType;

        WirelessUniversalGridEntryImpl() {
            builder.translation(translationKey("wirelessUniversalGrid")).push("wirelessUniversalGrid");
            energyCapacity = builder
                .translation(translationKey("wirelessUniversalGrid.energyCapacity"))
                .defineInRange("energyCapacity", DefaultEnergyUsage.WIRELESS_UNIVERSAL_GRID_CAPACITY, 0, Long.MAX_VALUE);
            gridType = builder
                .translation(translationKey("wirelessUniversalGrid.gridType"))
                .defineEnum("gridType", GridTypes.WIRELESS_GRID);
            builder.pop();
        }

        @Override
        public long getEnergyCapacity() {
            return energyCapacity.get();
        }

        @Override
        public GridTypes getGridType() {
            return gridType.get();
        }

        @Override
        public void setGridType(final GridTypes gridType) {
            if (gridType != this.gridType.get()) {
                this.gridType.set(gridType);
                ConfigImpl.this.spec.save();
            }
        }
    }
}
