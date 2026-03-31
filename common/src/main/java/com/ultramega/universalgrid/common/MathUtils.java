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
}
