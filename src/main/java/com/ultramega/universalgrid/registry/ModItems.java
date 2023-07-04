package com.ultramega.universalgrid.registry;

import com.ultramega.universalgrid.UniversalGrid;
import com.ultramega.universalgrid.item.WirelessUniversalGridItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UniversalGrid.MOD_ID);

    public static final RegistryObject<WirelessUniversalGridItem> WIRELESS_UNIVERSAL_GRID = ITEMS.register("wireless_universal_grid", () -> new WirelessUniversalGridItem(WirelessUniversalGridItem.Type.NORMAL));
    public static final RegistryObject<WirelessUniversalGridItem> CREATIVE_WIRELESS_UNIVERSAL_GRID = ITEMS.register("creative_wireless_universal_grid", () -> new WirelessUniversalGridItem(WirelessUniversalGridItem.Type.CREATIVE));
}
