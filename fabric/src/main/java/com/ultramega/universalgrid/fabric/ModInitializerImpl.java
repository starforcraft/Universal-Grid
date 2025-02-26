package com.ultramega.universalgrid.fabric;

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
import com.refinedmods.refinedstorage.common.content.DirectRegistryCallback;
import com.refinedmods.refinedstorage.common.content.RegistryCallback;
import com.refinedmods.refinedstorage.common.support.packet.PacketHandler;
import com.refinedmods.refinedstorage.fabric.api.RefinedStoragePlugin;
import com.refinedmods.refinedstorage.fabric.support.energy.EnergyStorageAdapter;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

public class ModInitializerImpl extends AbstractModInitializer implements RefinedStoragePlugin, ModInitializer {
    @Override
    public void onApiAvailable(final RefinedStorageApi refinedStorageApi) {
        Platform.setConfigProvider(ConfigImpl::get);
        registerContent(refinedStorageApi);
        registerCapabilities();
        registerPackets();
        registerPacketHandlers();
        registerCreativeModeTabListener(refinedStorageApi);
    }

    private void registerContent(final RefinedStorageApi refinedStorageApi) {
        final DirectRegistryCallback<Item> itemRegistryCallback = new DirectRegistryCallback<>(BuiltInRegistries.ITEM);
        registerCustomItems(itemRegistryCallback, refinedStorageApi);
        registerDataComponents(new DirectRegistryCallback<>(BuiltInRegistries.DATA_COMPONENT_TYPE));
    }

    private void registerCustomItems(final RegistryCallback<Item> callback,
                                     final RefinedStorageApi refinedStorageApi) {
        Items.INSTANCE.setWirelessUniversalGrid(
            callback.register(ContentIds.WIRELESS_UNIVERSAL_GRID, () -> new WirelessUniversalGridItem(
                false,
                refinedStorageApi.getEnergyItemHelper(),
                refinedStorageApi.getNetworkItemHelper()
            ) {
                @Override
                public boolean allowComponentsUpdateAnimation(final Player player,
                                                              final InteractionHand hand,
                                                              final ItemStack oldStack,
                                                              final ItemStack newStack) {
                    return AbstractModInitializer.allowComponentsUpdateAnimation(oldStack, newStack);
                }
            })
        );
        Items.INSTANCE.setCreativeWirelessUniversalGrid(
            callback.register(ContentIds.CREATIVE_WIRELESS_UNIVERSAL_GRID, () -> new WirelessUniversalGridItem(
                true,
                refinedStorageApi.getEnergyItemHelper(),
                refinedStorageApi.getNetworkItemHelper()
            ) {
                @Override
                public boolean allowComponentsUpdateAnimation(final Player player,
                                                              final InteractionHand hand,
                                                              final ItemStack oldStack,
                                                              final ItemStack newStack) {
                    return AbstractModInitializer.allowComponentsUpdateAnimation(oldStack, newStack);
                }
            })
        );
    }

    private void registerCapabilities() {
        registerEnergyItemProviders();
    }

    private void registerPackets() {
        PayloadTypeRegistry.playC2S().register(SetCursorPosStackPacket.PACKET_TYPE, SetCursorPosStackPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateDisabledSlotPacket.PACKET_TYPE, UpdateDisabledSlotPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SetCursorPosWindowPacket.PACKET_TYPE, SetCursorPosWindowPacket.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(SetDisabledSlotPacket.PACKET_TYPE, SetDisabledSlotPacket.STREAM_CODEC);
    }

    private void registerPacketHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(
            SetCursorPosStackPacket.PACKET_TYPE,
            wrapHandler(SetCursorPosStackPacket::handle)
        );
        ServerPlayNetworking.registerGlobalReceiver(
            UpdateDisabledSlotPacket.PACKET_TYPE,
            wrapHandler(UpdateDisabledSlotPacket::handle)
        );
    }

    private static <T extends CustomPacketPayload> ServerPlayNetworking.PlayPayloadHandler<T> wrapHandler(
        final PacketHandler<T> handler
    ) {
        return (packet, ctx) -> handler.handle(packet, ctx::player);
    }

    private void registerEnergyItemProviders() {
        EnergyStorage.ITEM.registerForItems(
            (stack, context) ->
                new EnergyStorageAdapter(Items.INSTANCE.getWirelessUniversalGrid().createEnergyStorage(stack)),
            Items.INSTANCE.getWirelessUniversalGrid()
        );
    }

    private void registerCreativeModeTabListener(final RefinedStorageApi refinedStorageApi) {
        final ResourceKey<CreativeModeTab> creativeModeTab = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            refinedStorageApi.getCreativeModeTabId()
        );
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(
            entries -> CreativeModeTabItems.addItems(entries::accept)
        );
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(ConfigImpl.class, Toml4jConfigSerializer::new);
    }
}
