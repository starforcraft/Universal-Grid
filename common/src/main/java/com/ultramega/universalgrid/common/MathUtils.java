package com.ultramega.universalgrid.common;

public final class MathUtils {
    private MathUtils() {
    }

    public static float wrapDegrees(final float angle) {
        float newAngle = angle % 360;
        if (newAngle < 0) {
            newAngle += 360;
        }
        return newAngle;
    }

    public static int rgbaToArgb(final float r, final float g, final float b, final float a) {
        return ((int) (a * 255) << 24)
            | ((int) (r * 255) << 16)
            | ((int) (g * 255) << 8)
            | (int) (b * 255);
    }
}
