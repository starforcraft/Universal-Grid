package com.ultramega.universalgrid.common.mixin;

import com.ultramega.universalgrid.common.ClientUtils;
import com.ultramega.universalgrid.common.gui.view.GridTypes;
import com.ultramega.universalgrid.common.interfaces.MixinGridType;
import com.ultramega.universalgrid.common.interfaces.MixinTabRenderer;
import com.ultramega.universalgrid.common.registry.KeyMappings;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.MOD_ID;

@Mixin(AbstractBaseScreen.class)
public abstract class MixinAbstractBaseScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements MixinTabRenderer {
    @Unique
    private static final ResourceLocation SELECTED_TAB_BOTTOM = ResourceLocation.fromNamespaceAndPath(MOD_ID, "selected_tab_bottom");
    @Unique
    private static final ResourceLocation SELECTED_TAB = ResourceLocation.fromNamespaceAndPath(MOD_ID, "selected_tab");
    @Unique
    private static final ResourceLocation UNSELECTED_TAB = ResourceLocation.fromNamespaceAndPath(MOD_ID, "unselected_tab");

    @Unique
    private static final int TAB_AMOUNT = 3;

    public MixinAbstractBaseScreen(final T menu, final Inventory playerInventory, final Component title) {
        super(menu, playerInventory, title);
    }

    @Unique
    @Override
    public void universalgrid$renderGridTabs(final GuiGraphics graphics) {
        if (this.getMenu() instanceof AbstractBaseContainerMenu containerMenu && ClientUtils.isUniversalGrid(containerMenu, Minecraft.getInstance().player)) {
            for (int i = 0; i < TAB_AMOUNT; i++) {
                final GridTypes gridType = GridTypes.values()[i];
                final boolean selected = i == ((MixinGridType) containerMenu).universalgrid$getGridType().ordinal();

                final int x = this.universalgrid$getTabX();
                final int y = this.universalgrid$getTabY(i);

                final PoseStack poseStack = graphics.pose();
                poseStack.pushPose();
                poseStack.translate(0.0F, 0.0F, selected ? 1.0F : 0.0F);

                graphics.blitSprite(selected
                    ? (i == TAB_AMOUNT - 1 ? SELECTED_TAB_BOTTOM : SELECTED_TAB)
                    : UNSELECTED_TAB, x, y, 32, 26);
                graphics.renderItem(gridType.getIcon(), x + 9, y + 5);

                poseStack.popPose();
            }
        }
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"))
    protected void renderTooltip(final GuiGraphics graphics, final int mouseX, final int mouseY, final CallbackInfo ci) {
        final int hoveringTab = this.universalgrid$getHoveringTab(mouseX, mouseY);
        if (hoveringTab != -1) {
            final GridTypes gridType = GridTypes.values()[hoveringTab];
            Platform.INSTANCE.renderTooltip(graphics, List.of(ClientTooltipComponent.create(gridType.getTooltip().getVisualOrderText())), mouseX, mouseY);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(final double mouseX, final double mouseY, final int clickedButton, final CallbackInfoReturnable<Boolean> cir) {
        final int hoveringTab = this.universalgrid$getHoveringTab(mouseX, mouseY);
        if (hoveringTab != -1) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            final GridTypes gridType = GridTypes.values()[hoveringTab];
            ((MixinGridType) this.getMenu()).universalgrid$setGridType(gridType, Minecraft.getInstance().player);
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (KeyMappings.INSTANCE.getSwitchWirelessUniversalGridType() != null
            && Platform.INSTANCE.isKeyDown(KeyMappings.INSTANCE.getSwitchWirelessUniversalGridType())) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            final GridTypes gridType = GridTypes.getNext(((MixinGridType) this.getMenu()).universalgrid$getGridType());
            ((MixinGridType) this.getMenu()).universalgrid$setGridType(gridType, Minecraft.getInstance().player);

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Unique
    private int universalgrid$getHoveringTab(final double mouseX, final double mouseY) {
        if (this.getMenu() instanceof AbstractBaseContainerMenu containerMenu && ClientUtils.isUniversalGrid(containerMenu, Minecraft.getInstance().player)) {
            for (int i = 0; i < TAB_AMOUNT; i++) {
                if (this.isHovering(this.universalgrid$getTabX() - this.leftPos + 3, this.universalgrid$getTabY(i) - this.topPos + 2, 24, 22, mouseX, mouseY)) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Unique
    private int universalgrid$getTabX() {
        final boolean isAutocraftingMonitor = ((MixinGridType) this.getMenu()).universalgrid$getGridType() == GridTypes.WIRELESS_AUTOCRAFTING_MONITOR;

        return this.leftPos + this.imageWidth - (isAutocraftingMonitor ? 4 : 21);
    }

    @Unique
    private int universalgrid$getTabY(final int i) {
        return this.topPos + this.imageHeight - (26 * TAB_AMOUNT) + (26 * i);
    }
}
