package com.ultramega.universalgrid.neoforge.datagen.recipe;

import com.refinedmods.refinedstorage.common.content.Items;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

public class MainRecipeProvider extends RecipeProvider {
    public MainRecipeProvider(final HolderLookup.Provider registries, final RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.universalGrid();
    }

    private void universalGrid() {
        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, com.ultramega.universalgrid.common.registry.Items.INSTANCE.getWirelessUniversalGrid())
            .pattern("DFD")
            .pattern("QGQ")
            .pattern("DCD")
            .define('F', Items.INSTANCE.getWirelessAutocraftingMonitor())
            .define('G', Items.INSTANCE.getWirelessGrid())
            .define('C', com.refinedmods.refinedstorage.quartzarsenal.common.Items.INSTANCE.getWirelessCraftingGrid())
            .define('D', net.minecraft.world.item.Items.DIAMOND)
            .define('Q', Items.INSTANCE.getQuartzEnrichedIron())
            .unlockedBy("has_grid", this.has(com.refinedmods.refinedstorage.common.content.Tags.GRIDS))
            .save(this.output);
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(final HolderLookup.Provider registries,
                                                      final RecipeOutput output) {
            return new MainRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Universal Grid recipes";
        }
    }
}
