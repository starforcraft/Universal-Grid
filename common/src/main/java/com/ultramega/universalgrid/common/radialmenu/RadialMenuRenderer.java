package com.ultramega.universalgrid.common.radialmenu;

import com.ultramega.universalgrid.common.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;

/**
 * This is heavily inspired by <a href="https://github.com/MatyrobbrtMods/KeyBindBundles/blob/main/src/main/java/com/matyrobbrt/keybindbundles/render/RadialMenuRenderer.java">KeybindBundles</a>
 */
public abstract class RadialMenuRenderer<T> {
    public static final float INNER = 40;
    public static final float OUTER = 100;
    public static final float MIDDLE_DISTANCE = (INNER + OUTER) / 2F;

    private static final float DRAWS = 300;

    protected int lastIndexUnderMouse = -1;

    private int[] hoverGrows = new int[0];
    private long lastUpdate = System.currentTimeMillis();

    public abstract List<T> getEntries();

    public abstract Component getTitle(T entry);

    public abstract ItemStack getIcon(T entry);

    public void render(final GuiGraphics graphics) {
        final List<T> entries = this.getEntries();
        if (entries.isEmpty()) {
            return;
        }

        final Minecraft mc = Minecraft.getInstance();
        final PoseStack poseStack = graphics.pose();

        mc.mouseHandler.releaseMouse();

        if (this.hoverGrows.length < entries.size()) {
            this.hoverGrows = ArrayUtils.addAll(this.hoverGrows, IntStream.range(0, entries.size() - this.hoverGrows.length)
                .map(i -> 0).toArray());
        }

        final int count = entries.size();
        final float angleSize = 360F / count;

        final int indexUnderMouse = this.getElementUnderMouse();
        if (!mc.isPaused() && !(mc.screen instanceof ChatScreen)) {
            this.lastIndexUnderMouse = indexUnderMouse;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        final float centerX = graphics.guiWidth() / 2f;
        final float centerY = graphics.guiHeight() / 2f;
        poseStack.pushPose();
        poseStack.translate(centerX, centerY, 0f);

        for (int i = 0; i < entries.size(); i++) {
            final boolean highlight = this.lastIndexUnderMouse == i;
            final float startAngle = -90F + 360F * (-0.5F + i) / count;
            RadialMenuRenderer.drawTorus(graphics, startAngle, angleSize, INNER, OUTER + 10f * (this.hoverGrows[i] / 10f),
                0.3f, 0.3f, 0.3f, highlight ? 0.85F : 0.6F);
        }

        if (!mc.mouseHandler.isMouseGrabbed()) {
            final long current = System.currentTimeMillis();
            if (current >= (this.lastUpdate + 1000 / 40)) {
                this.lastUpdate = current;
                for (int i = 0; i < entries.size(); i++) {
                    if (i == this.lastIndexUnderMouse) {
                        if (this.hoverGrows[i] < 10) {
                            this.hoverGrows[i]++;
                        }
                    } else if (this.hoverGrows[i] > 0) {
                        this.hoverGrows[i]--;
                    }
                }
            }
        }

        record PositionedText(float x, float y, Component text) {
        }

        final List<PositionedText> textToDraw = new ArrayList<>(entries.size());

        float position = 0;
        for (final T key : entries) {
            final float degrees = 270 + 360 * (position++ / count);
            final float angle = Mth.DEG_TO_RAD * degrees;
            final float x = Mth.cos(angle) * RadialMenuRenderer.MIDDLE_DISTANCE;
            final float y = Mth.sin(angle) * RadialMenuRenderer.MIDDLE_DISTANCE;

            final ItemStack icon = this.getIcon(key);
            if (!icon.isEmpty()) {
                textToDraw.add(new PositionedText(x, y, this.getTitle(key)));

                graphics.renderItem(icon, Math.round(x - 8), Math.round(y - 8 - 2 - 9));
            }
        }

        final Font font = mc.font;
        if (!textToDraw.isEmpty()) {
            for (final PositionedText toDraw : textToDraw) {
                poseStack.pushPose();
                poseStack.translate(toDraw.x, toDraw.y, 120F);
                poseStack.scale(0.6F, 0.6F, 0.6F);
                final Component text = toDraw.text;
                graphics.drawString(font, text.getVisualOrderText(), (int) (-font.width(text) / 2F), 8, 0xCCFFFFFF, true);
                poseStack.popPose();
            }
        }

        poseStack.popPose();

        RenderSystem.disableBlend();
    }

    public static MousePos getMousePos() {
        final Minecraft mc = Minecraft.getInstance();
        final MouseHandler mouse = mc.mouseHandler;
        final double mouseX = mouse.xpos() * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getScreenWidth();
        final double mouseY = mouse.ypos() * (double) mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getScreenHeight();
        return new MousePos(mouseX, mouseY);
    }

    public static MousePos getDiffFromCenter(final double mouseX, final double mouseY) {
        final Window window = Minecraft.getInstance().getWindow();
        final float centerX = window.getGuiScaledWidth() / 2f;
        final float centerY = window.getGuiScaledHeight() / 2f;

        final double xDiff = mouseX - centerX;
        final double yDiff = mouseY - centerY;
        return new MousePos(xDiff, yDiff);
    }

    public int getElementUnderMouse() {
        final MousePos mouse = getMousePos();
        final double mouseX = mouse.x;
        final double mouseY = mouse.y;
        final int count = this.getEntries().size();

        final MousePos diffFromCenter = getDiffFromCenter(mouseX, mouseY);
        final double distanceFromCenter = Mth.length(diffFromCenter.x(), diffFromCenter.y());
        if (distanceFromCenter > 30) {
            // draw mouse selection highlight
            final float angle = (float) (Mth.RAD_TO_DEG * Mth.atan2(diffFromCenter.y(), diffFromCenter.x()));
            final float modeSize = 180F / count;

            final float selectionAngle = MathUtils.wrapDegrees(angle + modeSize + 90F);
            return (int) (selectionAngle * (count / 360F));
        }
        return -1;
    }

    public void clearState() {
        this.hoverGrows = new int[0];
    }

    private static void drawTorus(final GuiGraphics graphics,
                                  final float startAngle,
                                  final float sizeAngle,
                                  final float inner,
                                  final float outer,
                                  final float red,
                                  final float green,
                                  final float blue,
                                  final float alpha) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        final var vertexBuffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        final var matrix4f = graphics.pose().last().pose();
        final float draws = DRAWS * (sizeAngle / 360F);
        for (int i = 0; i <= draws; i++) {
            final float degrees = startAngle + (i / DRAWS) * 360;
            final float angle = Mth.DEG_TO_RAD * degrees;
            final float cos = Mth.cos(angle);
            final float sin = Mth.sin(angle);
            vertexBuffer.addVertex(matrix4f, outer * cos, outer * sin, 0)
                .setColor(red, green, blue, alpha);
            vertexBuffer.addVertex(matrix4f, inner * cos, inner * sin, 0)
                .setColor(red, green, blue, alpha);
        }
        BufferUploader.drawWithShader(vertexBuffer.buildOrThrow());
    }

    public record MousePos(double x, double y) {
    }
}
