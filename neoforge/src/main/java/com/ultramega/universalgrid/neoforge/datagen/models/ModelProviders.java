package com.ultramega.universalgrid.neoforge.datagen.models;

import com.ultramega.universalgrid.common.registry.Items;

import com.refinedmods.refinedstorage.common.support.network.item.NetworkBoundItemModelProperty;

import java.util.Arrays;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.MOD_ID;
import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;

public class ModelProviders extends ModelProvider {
    public ModelProviders(final PackOutput output) {
        super(output, MOD_ID);
    }

    @Override
    protected void registerModels(final BlockModelGenerators blockModels, final ItemModelGenerators itemModels) {
        this.registerNetworkBound(itemModels, "wireless_universal_grid", Items.INSTANCE.getWirelessUniversalGrid(),
            Items.INSTANCE.getCreativeWirelessUniversalGrid());
    }

    private void registerNetworkBound(final ItemModelGenerators itemModels, final String name, final Item... items) {
        final Identifier activeItemModel = ModelTemplates.FLAT_ITEM.create(
            createUniversalGridIdentifier("item/" + name + "/active"),
            TextureMapping.layer0(texture(createUniversalGridIdentifier("item/" + name + "/active"))),
            itemModels.modelOutput
        );
        final Identifier inactiveItemModel = ModelTemplates.FLAT_ITEM.create(
            createUniversalGridIdentifier("item/" + name + "/inactive"),
            TextureMapping.layer0(texture(createUniversalGridIdentifier("item/" + name + "/inactive"))),
            itemModels.modelOutput
        );
        Arrays.stream(items).forEach(item -> itemModels.itemModelOutput.accept(item, ItemModelUtils.conditional(
            new NetworkBoundItemModelProperty(),
            ItemModelUtils.plainModel(activeItemModel),
            ItemModelUtils.plainModel(inactiveItemModel)
        )));
    }

    private static Material texture(final Identifier location) {
        return new Material(location);
    }
}
