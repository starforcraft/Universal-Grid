package com.YTrollman.UniversalGrid.registry;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final KeyBinding OPEN_WIRELESS_UNIVERSAL_GRID = new KeyBinding(
            "key.universalgrid.openWirelessUniversalGrid",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "Universal Grid"
    );
}
