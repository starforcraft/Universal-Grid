package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.interfaces.MixinDisabledSlot;
import com.ultramega.universalgrid.common.interfaces.MixinPlayer;
import com.ultramega.universalgrid.common.interfaces.MixinUpdateSlot;
import com.ultramega.universalgrid.common.packet.s2c.SetDisabledSlotPacket;

import com.refinedmods.refinedstorage.common.api.support.slotreference.SlotReference;
import com.refinedmods.refinedstorage.common.autocrafting.monitor.AbstractAutocraftingMonitorContainerMenu;
import com.refinedmods.refinedstorage.common.autocrafting.monitor.AutocraftingMonitorData;
import com.refinedmods.refinedstorage.common.autocrafting.monitor.WirelessAutocraftingMonitorContainerMenu;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WirelessAutocraftingMonitorContainerMenu.class)
public abstract class MixinWirelessAutocraftingMonitorContainerMenu extends AbstractAutocraftingMonitorContainerMenu implements MixinDisabledSlot, MixinUpdateSlot {
    protected MixinWirelessAutocraftingMonitorContainerMenu(final MenuType<?> menuType,
                                                            final int syncId,
                                                            final Inventory playerInventory,
                                                            final AutocraftingMonitorData data) {
        super(menuType, syncId, playerInventory, data);
    }

    @Override
    public void universalgrid$setDisabledSlot(final SlotReference disabledSlot) {
        this.disabledSlot = disabledSlot;
    }

    @Override
    public @Nullable SlotReference universalgrid$getDisabledSlot() {
        return this.disabledSlot;
    }

    @Override
    public void universalgrid$sendUpdate() {
        final Player player = ((MixinPlayer) this).universalgrid$getPlayer();
        if (player instanceof ServerPlayer serverPlayer && this.disabledSlot != null) {
            com.refinedmods.refinedstorage.common.Platform.INSTANCE.sendPacketToClient(serverPlayer,
                new SetDisabledSlotPacket(this.disabledSlot));
        }
    }
}
