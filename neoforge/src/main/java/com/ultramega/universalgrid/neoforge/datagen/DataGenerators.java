package com.ultramega.universalgrid.neoforge.datagen;

import com.ultramega.universalgrid.neoforge.datagen.models.ModelProviders;
import com.ultramega.universalgrid.neoforge.datagen.recipe.MainRecipeProvider;

import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client e) {
        final DataGenerator generator = e.getGenerator();
        final DataGenerator.PackGenerator pack = generator.getVanillaPack(true);
        pack.addProvider(ModelProviders::new);
        pack.addProvider(output -> new MainRecipeProvider.Runner(output, e.getLookupProvider()));
    }
}
