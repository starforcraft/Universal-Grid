package com.ultramega.universalgrid.neoforge;

import com.ultramega.universalgrid.common.AbstractModInitializer;
import com.ultramega.universalgrid.common.ContentIds;
import com.ultramega.universalgrid.common.Platform;
import com.ultramega.universalgrid.common.packet.SetCursorPosStackPacket;
import com.ultramega.universalgrid.common.packet.SetCursorPosWindowPacket;
import com.ultramega.universalgrid.common.packet.SetDisabledSlotPacket;
import com.ultramega.universalgrid.common.packet.UpdateDisabledSlotPacket;
import com.ultramega.universalgrid.common.registry.CreativeModeTabItems;
import com.ultramega.universalgrid.common.registry.Items;
import com.ultramega.universalgrid.common.wirelessuniversalgrid.WirelessUniversalGridItem;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.content.RegistryCallback;
import com.refinedmods.refinedstorage.common.support.packet.PacketHandler;
import com.refinedmods.refinedstorage.neoforge.support.energy.EnergyStorageAdapter;

import java.util.function.Supplier;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.MOD_ID;

@Mod(MOD_ID)
public class ModInitializer extends AbstractModInitializer {
    private final DeferredRegister<Item> itemRegistry =
        DeferredRegister.create(BuiltInRegistries.ITEM, MOD_ID);
    private final DeferredRegister<DataComponentType<?>> dataComponentTypeRegistry =
        DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MOD_ID);

    public ModInitializer(final IEventBus eventBus, final ModContainer modContainer) {
        final ConfigImpl config = new ConfigImpl();
        modContainer.registerConfig(ModConfig.Type.COMMON, config.getSpec());
        Platform.setConfigProvider(() -> config);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(ClientModInitializer::onClientSetup);
            eventBus.addListener(ClientModInitializer::onRegisterKeyMappings);
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
        this.registerContent(eventBus);
        eventBus.addListener(this::registerCapabilities);
        eventBus.addListener(this::registerPackets);
        eventBus.addListener(this::registerCreativeModeTabListener);
    }

    private void registerContent(final IEventBus eventBus) {
        this.registerItems(eventBus);
        this.registerDataComponents(eventBus);
    }

    private void registerItems(final IEventBus eventBus) {
        final RegistryCallback<Item> callback = new ForgeRegistryCallback<>(this.itemRegistry);
        this.registerCustomItems(callback);
        this.itemRegistry.register(eventBus);
    }

    private void registerCustomItems(final RegistryCallback<Item> callback) {
        Items.INSTANCE.setWirelessUniversalGrid(
            callback.register(ContentIds.WIRELESS_UNIVERSAL_GRID, () -> new WirelessUniversalGridItem(
                false,
                RefinedStorageApi.INSTANCE.getEnergyItemHelper(),
                RefinedStorageApi.INSTANCE.getNetworkItemHelper()
            ) {
                @Override
                public boolean shouldCauseReequipAnimation(final ItemStack oldStack,
                                                           final ItemStack newStack,
                                                           final boolean slotChanged) {
                    return AbstractModInitializer.allowComponentsUpdateAnimation(oldStack, newStack);
                }
            })
        );
        Items.INSTANCE.setCreativeWirelessUniversalGrid(
            callback.register(ContentIds.CREATIVE_WIRELESS_UNIVERSAL_GRID, () -> new WirelessUniversalGridItem(
                true,
                RefinedStorageApi.INSTANCE.getEnergyItemHelper(),
                RefinedStorageApi.INSTANCE.getNetworkItemHelper()
            ) {
                @Override
                public boolean shouldCauseReequipAnimation(final ItemStack oldStack,
                                                           final ItemStack newStack,
                                                           final boolean slotChanged) {
                    return AbstractModInitializer.allowComponentsUpdateAnimation(oldStack, newStack);
                }
            })
        );
    }

    private void registerDataComponents(final IEventBus eventBus) {
        final RegistryCallback<DataComponentType<?>> callback = new ForgeRegistryCallback<>(this.dataComponentTypeRegistry);
        this.registerDataComponents(callback);
        this.dataComponentTypeRegistry.register(eventBus);
    }

    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        this.registerEnergyItemProviders(event);
    }

    private void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MOD_ID);
        registrar.playToServer(
            SetCursorPosStackPacket.PACKET_TYPE,
            SetCursorPosStackPacket.STREAM_CODEC,
            wrapHandler(SetCursorPosStackPacket::handle)
        );
        registrar.playToServer(
            UpdateDisabledSlotPacket.PACKET_TYPE,
            UpdateDisabledSlotPacket.STREAM_CODEC,
            wrapHandler(UpdateDisabledSlotPacket::handle)
        );

        registrar.playToClient(
            SetCursorPosWindowPacket.PACKET_TYPE,
            SetCursorPosWindowPacket.STREAM_CODEC,
            wrapHandler(SetCursorPosWindowPacket::handle)
        );
        registrar.playToClient(
            SetDisabledSlotPacket.PACKET_TYPE,
            SetDisabledSlotPacket.STREAM_CODEC,
            wrapHandler(SetDisabledSlotPacket::handle)
        );
    }

    private void registerEnergyItemProviders(final RegisterCapabilitiesEvent event) {
        event.registerItem(
            Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new EnergyStorageAdapter(
                Items.INSTANCE.getWirelessUniversalGrid().createEnergyStorage(stack)
            ),
            Items.INSTANCE.getWirelessUniversalGrid()
        );
    }

    private void registerCreativeModeTabListener(final BuildCreativeModeTabContentsEvent e) {
        final ResourceKey<CreativeModeTab> creativeModeTab = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            RefinedStorageApi.INSTANCE.getCreativeModeTabId()
        );
        if (!e.getTabKey().equals(creativeModeTab)) {
            return;
        }
        CreativeModeTabItems.addItems(e::accept);
    }

    private static <T extends CustomPacketPayload> IPayloadHandler<T> wrapHandler(final PacketHandler<T> handler) {
        return (packet, ctx) -> handler.handle(packet, ctx::player);
    }

    private record ForgeRegistryCallback<T>(DeferredRegister<T> registry) implements RegistryCallback<T> {
        @Override
        public <R extends T> Supplier<R> register(final ResourceLocation id, final Supplier<R> value) {
            return this.registry.register(id.getPath(), value);
        }
    }
}
