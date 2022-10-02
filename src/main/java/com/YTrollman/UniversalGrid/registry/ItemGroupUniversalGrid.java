package com.YTrollman.UniversalGrid.registry;

import com.YTrollman.UniversalGrid.UniversalGrid;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ItemGroupUniversalGrid {
	public static final CreativeModeTab UNIVERSAL_GRID = (new CreativeModeTab(UniversalGrid.MOD_ID) {

		@Override
		@Nonnull
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID.get());
		}
	});
}
