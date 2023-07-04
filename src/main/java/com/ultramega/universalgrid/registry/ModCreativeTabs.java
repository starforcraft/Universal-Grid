package com.ultramega.universalgrid.registry;

import com.ultramega.universalgrid.UniversalGrid;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class ModCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UniversalGrid.MOD_ID);

	public static final RegistryObject<CreativeModeTab> TAB_UNIVERSAL_GRID = TABS.register(UniversalGrid.MOD_ID, () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.universalgrid")).icon(() -> new ItemStack(ModItems.WIRELESS_UNIVERSAL_GRID.get())).displayItems((featureFlags, output) -> {
		output.accept(new ItemStack(ModItems.WIRELESS_UNIVERSAL_GRID.get()));
		output.accept(new ItemStack(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID.get()));
	}).build());
}
