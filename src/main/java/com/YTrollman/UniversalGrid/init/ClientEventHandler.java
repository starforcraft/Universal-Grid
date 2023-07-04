package com.YTrollman.UniversalGrid.init;

import com.YTrollman.UniversalGrid.registry.ModItems;
import com.YTrollman.UniversalGrid.registry.ModKeyBindings;
import com.refinedmods.refinedstorage.item.property.NetworkItemPropertyGetter;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventHandler {

    public ClientEventHandler() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeymappings);
    }

    public void init(FMLClientSetupEvent event) {
        ItemProperties.register(ModItems.WIRELESS_UNIVERSAL_GRID.get(), new ResourceLocation("connected"), new NetworkItemPropertyGetter());
        ItemProperties.register(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID.get(), new ResourceLocation("connected"), new NetworkItemPropertyGetter());
    }

    @SubscribeEvent
    public void onRegisterKeymappings(RegisterKeyMappingsEvent e) {
        e.register(ModKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID);
    }
}