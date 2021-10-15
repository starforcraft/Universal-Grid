package com.YTrollman.UniversalGrid.init;

import com.YTrollman.UniversalGrid.registry.ModItems;
import com.refinedmods.refinedstorage.item.property.NetworkItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventHandler {
    public ClientEventHandler() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent e) {
        MinecraftForge.EVENT_BUS.register(this);

        ItemModelsProperties.register(ModItems.WIRELESS_UNIVERSAL_GRID.get(), new ResourceLocation("connected"), new NetworkItemPropertyGetter());
        ItemModelsProperties.register(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID.get(), new ResourceLocation("connected"), new NetworkItemPropertyGetter());
    }
}
