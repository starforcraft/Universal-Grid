package com.YTrollman.UniversalGrid;

import com.YTrollman.UniversalGrid.config.Config;
import com.YTrollman.UniversalGrid.handler.ModNetworkHandler;
import com.YTrollman.UniversalGrid.network.WirelessUniversalGridGridFactory;
import com.YTrollman.UniversalGrid.registry.ModItems;
import com.YTrollman.UniversalGrid.registry.RegistryHandler;
import com.refinedmods.refinedstorage.api.IRSAPI;
import com.refinedmods.refinedstorage.api.RSAPIInject;
import com.refinedmods.refinedstorage.item.property.NetworkItemPropertyGetter;
import com.refinedmods.refinedstorage.screen.KeyInputListener;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("universalgrid")
public class UniversalGrid
{
    @RSAPIInject
    public static IRSAPI UNIVERSALGRIDAPI;
    public static final ModNetworkHandler MOD_NETWORK_HANDLER = new ModNetworkHandler();

    public static final String MOD_ID = "universalgrid";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static final ResourceLocation CONNECTED = new ResourceLocation("connected");
    
    public UniversalGrid() { //grid, fluid grid, crafting monitor, crafting grid, portable grid
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.common_config);

        RegistryHandler.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        Config.loadConfig(Config.common_config, FMLPaths.CONFIGDIR.get().resolve("universalgrid-common.toml").toString());

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event)
    {
        UniversalGrid.MOD_NETWORK_HANDLER.register();
        UniversalGrid.UNIVERSALGRIDAPI.getGridManager().add(WirelessUniversalGridGridFactory.ID, new WirelessUniversalGridGridFactory());
    }

    private void doClientStuff(FMLClientSetupEvent event)
    {
        ClientRegistry.registerKeyBinding(UniversalGridKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID);

        ItemModelsProperties.register(ModItems.WIRELESS_UNIVERSAL_GRID.get(), CONNECTED, new NetworkItemPropertyGetter());
        ItemModelsProperties.register(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID.get(), CONNECTED, new NetworkItemPropertyGetter());
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) 
    {
    	
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        if (Minecraft.getInstance().player != null) {
            if (UniversalGridKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID.isDown()) {
                KeyInputListener.findAndOpen(ModItems.WIRELESS_UNIVERSAL_GRID.get(), ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID.get());
            }
        }
    }
}
