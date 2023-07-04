package com.ultramega.universalgrid.item;

import com.ultramega.universalgrid.apiiml.network.item.WirelessUniversalGridNetworkItem;
import com.ultramega.universalgrid.config.UniversalGridConfig;
import com.ultramega.universalgrid.registry.ModCreativeTabs;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.item.NetworkItem;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class WirelessUniversalGridItem extends NetworkItem {
    public enum Type {
        NORMAL,
        CREATIVE
    }

    private final Type type;

    public WirelessUniversalGridItem(Type type) {
        super(new Item.Properties().stacksTo(1), type == Type.CREATIVE, ()-> UniversalGridConfig.UNIVERSAL_GRID_CAPACITY.get());
        this.type = type;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide() && entity instanceof Player) {
            CompoundTag tag = stack.getTag();
            if (tag == null) {
                tag = new CompoundTag();
            }

            if(!tag.contains("gridType")) {
                tag.putInt("gridType", 0);
                stack.setTag(tag);
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ItemStack stack = ctx.getPlayer().getItemInHand(ctx.getHand());
        INetwork network = NetworkUtils.getNetworkFromNode(NetworkUtils.getNodeFromBlockEntity(ctx.getLevel().getBlockEntity(ctx.getClickedPos())));
        if (network != null) {
            CompoundTag tag = stack.getTag();
            if (tag == null) {
                tag = new CompoundTag();
            }

            tag.putInt("NodeX", network.getPosition().getX());
            tag.putInt("NodeY", network.getPosition().getY());
            tag.putInt("NodeZ", network.getPosition().getZ());
            tag.putString("Dimension", ctx.getLevel().dimension().location().toString());
            stack.setTag(tag);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public Type getType() {
        return type;
    }

    @Override
    @Nonnull
    public INetworkItem provide(INetworkItemManager handler, Player player, ItemStack stack, PlayerSlot slot) {
        return new WirelessUniversalGridNetworkItem(handler, player, stack, slot);
    }
}
