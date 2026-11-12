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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ultramega.universalgrid.common.UniversalGridIdentifierUtil.createUniversalGridIdentifier;
import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED;

@Mixin(AbstractBaseScreen.class)
public abstract class MixinAbstractBaseScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements MixinTabRenderer {
    @Unique
    private static final Identifier SELECTED_TAB_BOTTOM = createUniversalGridIdentifier("selected_tab_bottom");
    @Unique
    private static final Identifier SELECTED_TAB = createUniversalGridIdentifier("selected_tab");
    @Unique
    private static final Identifier UNSELECTED_TAB = createUniversalGridIdentifier("unselected_tab");

    @Unique
    private static final int TAB_AMOUNT = 3;
    @Unique
    private static final int TAB_WIDTH = 32;
    @Unique
    private static final int TAB_HEIGHT = 26;

    public MixinAbstractBaseScreen(final T menu, final Inventory playerInventory, final Component title) {
        super(menu, playerInventory, title);
    }

    @Unique
    @Override
    public void universalgrid$renderGridTabs(final GuiGraphicsExtractor graphics, final boolean onlySelected) {
        if (this.universalgrid$hasGridTabs()) {
            final int selectedIndex = ((MixinGridType) this.getMenu()).universalgrid$getGridType().ordinal();

            if (!onlySelected) {
                for (int i = 0; i < TAB_AMOUNT; i++) {
                    if (i == selectedIndex) {
                        continue;
                    }

                    final GridTypes gridType = GridTypes.values()[i];
                    final int x = this.universalgrid$getTabX();
                    final int y = this.universalgrid$getTabY(i);

                    graphics.blitSprite(GUI_TEXTURED, UNSELECTED_TAB, x, y, TAB_WIDTH, TAB_HEIGHT);
                    graphics.item(gridType.getIcon(), x + 9, y + 5);
                }
            } else {
                final GridTypes selectedType = GridTypes.values()[selectedIndex];
                final int x = this.universalgrid$getTabX();
                final int y = this.universalgrid$getTabY(selectedIndex);

                graphics.blitSprite(GUI_TEXTURED, selectedIndex == TAB_AMOUNT - 1 ? SELECTED_TAB_BOTTOM : SELECTED_TAB, x, y, TAB_WIDTH, TAB_HEIGHT);
                graphics.item(selectedType.getIcon(), x + 9, y + 5);
            }
        }
    }

    @Unique
    @Override
    public List<Rect2i> universalgrid$getGridTabExclusionAreas() {
        if (!this.universalgrid$hasGridTabs()) {
            return List.of();
        }

        final int tabRight = this.universalgrid$getTabX() + TAB_WIDTH;
        final int screenRight = this.leftPos + this.imageWidth;
        return List.of(new Rect2i(screenRight, this.universalgrid$getTabY(0), tabRight - screenRight, TAB_HEIGHT * TAB_AMOUNT));
    }

    @Inject(method = "extractTooltip", at = @At("HEAD"))
    protected void extractTooltip(final GuiGraphicsExtractor graphics, final int x, final int y, final CallbackInfo ci) {
        final int hoveringTab = this.universalgrid$getHoveringTab(x, y);
        if (hoveringTab != -1) {
            final GridTypes gridType = GridTypes.values()[hoveringTab];
            final List<ClientTooltipComponent> tooltip = List.of(ClientTooltipComponent.create(gridType.getTooltip().getVisualOrderText()));
            graphics.tooltip(this.font, tooltip, x, y, DefaultTooltipPositioner.INSTANCE, null);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(final MouseButtonEvent event, final boolean doubleClick, final CallbackInfoReturnable<Boolean> cir) {
        final int hoveringTab = this.universalgrid$getHoveringTab(event.x(), event.y());
        if (hoveringTab != -1) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            final GridTypes gridType = GridTypes.values()[hoveringTab];
            ((MixinGridType) this.getMenu()).universalgrid$setGridType(gridType, Minecraft.getInstance().player);
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        if (KeyMappings.INSTANCE.getSwitchWirelessUniversalGridType() != null
            && Platform.INSTANCE.isKeyDown(KeyMappings.INSTANCE.getSwitchWirelessUniversalGridType())) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            final GridTypes gridType = GridTypes.getNext(((MixinGridType) this.getMenu()).universalgrid$getGridType());
            ((MixinGridType) this.getMenu()).universalgrid$setGridType(gridType, Minecraft.getInstance().player);

            return true;
        }

        return super.keyPressed(event);
    }

    @Unique
    private int universalgrid$getHoveringTab(final double mouseX, final double mouseY) {
        if (this.universalgrid$hasGridTabs()) {
            for (int i = 0; i < TAB_AMOUNT; i++) {
                if (this.isHovering(this.universalgrid$getTabX() - this.leftPos + 3, this.universalgrid$getTabY(i) - this.topPos + 2, 24, 22, mouseX, mouseY)) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Unique
    private boolean universalgrid$hasGridTabs() {
        return this.getMenu() instanceof AbstractBaseContainerMenu containerMenu
            && ClientUtils.isUniversalGrid(containerMenu, Minecraft.getInstance().player);
    }

    @Unique
    private int universalgrid$getTabX() {
        final boolean isAutocraftingMonitor = ((MixinGridType) this.getMenu()).universalgrid$getGridType() == GridTypes.WIRELESS_AUTOCRAFTING_MONITOR;

        return this.leftPos + this.imageWidth - (isAutocraftingMonitor ? 4 : 21);
    }

    @Unique
    private int universalgrid$getTabY(final int i) {
        return this.topPos + this.imageHeight - (TAB_HEIGHT * TAB_AMOUNT) + (TAB_HEIGHT * i);
    }
}
