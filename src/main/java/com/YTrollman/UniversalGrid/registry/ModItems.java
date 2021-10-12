package com.YTrollman.UniversalGrid.registry;

import com.YTrollman.UniversalGrid.UniversalGrid;
import com.YTrollman.UniversalGrid.items.WirelessUniversalGridItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UniversalGrid.MOD_ID);

    public static final RegistryObject<Item> WIRELESS_UNIVERSAL_GRID = ITEMS.register("wireless_universal_grid", () -> new WirelessUniversalGridItem(WirelessUniversalGridItem.Type.NORMAL));
    public static final RegistryObject<Item> CREATIVE_WIRELESS_UNIVERSAL_GRID = ITEMS.register("creative_wireless_universal_grid", () -> new WirelessUniversalGridItem(WirelessUniversalGridItem.Type.CREATIVE));
}
