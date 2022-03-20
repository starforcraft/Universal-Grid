package com.YTrollman.UniversalGrid;

import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridGridFactory;
import com.YTrollman.UniversalGrid.config.Config;
import com.YTrollman.UniversalGrid.init.ClientEventHandler;
import com.YTrollman.UniversalGrid.item.WirelessUniversalGridItem;
import com.YTrollman.UniversalGrid.registry.ModItems;
import com.YTrollman.UniversalGrid.registry.ModKeyBindings;
import com.YTrollman.UniversalGrid.registry.ModNetworkHandler;
import com.refinedmods.refinedstorage.api.IRSAPI;
import com.refinedmods.refinedstorage.api.RSAPIInject;
import com.refinedmods.refinedstorage.screen.KeyInputListener;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("universalgrid")
public class UniversalGrid {
    public static final String MOD_ID = "universalgrid";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @RSAPIInject
    public static IRSAPI RSAPI;
    public static final ModNetworkHandler NETWORK_HANDLER = new ModNetworkHandler();

    public UniversalGrid() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.common_config);
        Config.loadConfig(Config.common_config, FMLPaths.CONFIGDIR.get().resolve("universalgrid-common.toml").toString());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::onRegisterItems);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEventHandler::new);

        MinecraftForge.EVENT_BUS.addListener(this::onKeyInput);
    }

    private void setup(FMLCommonSetupEvent event) {
        UniversalGrid.NETWORK_HANDLER.register();
        UniversalGrid.RSAPI.getGridManager().add(WirelessUniversalGridGridFactory.ID, new WirelessUniversalGridGridFactory());
    }

    private void doClientStuff(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(ModKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    	
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(new WirelessUniversalGridItem(WirelessUniversalGridItem.Type.NORMAL));
        e.getRegistry().register(new WirelessUniversalGridItem(WirelessUniversalGridItem.Type.CREATIVE));
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent e) {
        if (Minecraft.getInstance().player != null) {
            if (ModKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID.isDown()) {
                KeyInputListener.findAndOpen(ModItems.WIRELESS_UNIVERSAL_GRID, ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID);
            }
        }
    }
}
