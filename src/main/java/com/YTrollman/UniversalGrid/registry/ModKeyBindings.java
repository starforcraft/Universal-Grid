package com.YTrollman.UniversalGrid.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final KeyMapping OPEN_WIRELESS_UNIVERSAL_GRID = new KeyMapping(
            "key.universalgrid.openWirelessUniversalGrid",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "Universal Grid"
    );
}
