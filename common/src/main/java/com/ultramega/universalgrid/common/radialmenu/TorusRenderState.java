package com.ultramega.universalgrid.common.radialmenu;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2fc;
import org.jspecify.annotations.Nullable;

public record TorusRenderState(Matrix3x2fc pose,
                               float startAngle,
                               float sizeAngle,
                               float inner,
                               float outer,
                               int argb,
                               @Nullable ScreenRectangle scissorArea) implements GuiElementRenderState {
    private static final int DRAWS = 128;

    @Override
    public RenderPipeline pipeline() {
        return RenderPipelines.GUI;
    }

    @Override
    public TextureSetup textureSetup() {
        return TextureSetup.noTexture();
    }

    @Nullable
    @Override
    public ScreenRectangle scissorArea() {
        return this.scissorArea;
    }

    @Nullable
    @Override
    public ScreenRectangle bounds() {
        final ScreenRectangle rect = new ScreenRectangle(
            (int) -this.outer,
            (int) -this.outer,
            (int) (this.outer * 2.0F),
            (int) (this.outer * 2.0F)
        ).transformMaxBounds(this.pose);

        return this.scissorArea != null ? this.scissorArea.intersection(rect) : rect;
    }

    @Override
    public void buildVertices(final VertexConsumer consumer) {
        final int steps = Math.max(1, Mth.ceil(DRAWS * (this.sizeAngle / 360.0F)));

        for (int i = 0; i < steps; i++) {
            final float t0 = i / (float) steps;
            final float t1 = (i + 1) / (float) steps;

            final float deg0 = this.startAngle + t0 * this.sizeAngle;
            final float deg1 = this.startAngle + t1 * this.sizeAngle;

            final float ang0 = (float) Math.toRadians(deg0);
            final float ang1 = (float) Math.toRadians(deg1);

            final float cos0 = (float) Math.cos(ang0);
            final float sin0 = (float) Math.sin(ang0);
            final float cos1 = (float) Math.cos(ang1);
            final float sin1 = (float) Math.sin(ang1);

            final float outerX0 = this.outer * cos0;
            final float outerY0 = this.outer * sin0;
            final float innerX0 = this.inner * cos0;
            final float innerY0 = this.inner * sin0;

            final float outerX1 = this.outer * cos1;
            final float outerY1 = this.outer * sin1;
            final float innerX1 = this.inner * cos1;
            final float innerY1 = this.inner * sin1;

            consumer.addVertexWith2DPose(this.pose, innerX0, innerY0).setUv(0, 0).setColor(this.argb);
            consumer.addVertexWith2DPose(this.pose, innerX1, innerY1).setUv(0, 1).setColor(this.argb);
            consumer.addVertexWith2DPose(this.pose, outerX1, outerY1).setUv(1, 1).setColor(this.argb);
            consumer.addVertexWith2DPose(this.pose, outerX0, outerY0).setUv(1, 0).setColor(this.argb);
        }
    }
}
