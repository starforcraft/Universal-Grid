package com.YTrollman.UniversalGrid.registry;

import com.YTrollman.UniversalGrid.UniversalGrid;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ItemGroupUniversalGrid {
	public static final ItemGroup UNIVERSAL_GRID = (new ItemGroup(UniversalGrid.MOD_ID) {

		@Override
		@Nonnull
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID);
		}
	});
}
