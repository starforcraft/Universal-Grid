package com.ultramega.universalgrid.fabric;

import com.ultramega.universalgrid.common.Config;
import com.ultramega.universalgrid.common.DefaultEnergyUsage;
import com.ultramega.universalgrid.common.UniversalGridIdentifierUtil;
import com.ultramega.universalgrid.common.gui.view.GridTypes;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@me.shedaniel.autoconfig.annotation.Config(name = UniversalGridIdentifierUtil.MOD_ID)
@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "CanBeFinal"})
public class ConfigImpl implements ConfigData, Config {
    @ConfigEntry.Gui.CollapsibleObject
    private WirelessUniversalGridEntryEntryImpl wirelessUniversalGrid = new WirelessUniversalGridEntryEntryImpl();

    public static ConfigImpl get() {
        return AutoConfig.getConfigHolder(ConfigImpl.class).getConfig();
    }

    @Override
    public WirelessUniversalGridEntry getWirelessUniversalGrid() {
        return this.wirelessUniversalGrid;
    }

    private static class WirelessUniversalGridEntryEntryImpl implements WirelessUniversalGridEntry {
        private long energyCapacity = DefaultEnergyUsage.WIRELESS_UNIVERSAL_GRID_CAPACITY;
        private GridTypes gridType = GridTypes.WIRELESS_GRID;

        @Override
        public long getEnergyCapacity() {
            return this.energyCapacity;
        }

        @Override
        public GridTypes getGridType() {
            return this.gridType;
        }

        @Override
        public void setGridType(final GridTypes gridType) {
            this.gridType = gridType;
            save();
        }

        private static void save() {
            AutoConfig.getConfigHolder(ConfigImpl.class).save();
        }
    }
}
