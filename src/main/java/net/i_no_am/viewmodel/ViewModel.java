package net.i_no_am.viewmodel;

import com.mojang.blaze3d.platform.InputConstants;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

public class ViewModel implements ClientModInitializer, Global {

    public static final KeyMapping BIND = KeyMappingHelper.registerKeyMapping(new KeyMapping("Toggle ViewModel GUI", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyMapping.Category.MISC));

    @Override
    public void onInitializeClient() {
        MidnightConfig.init(modId, Config.class);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (BIND.consumeClick() && mc.player != null) {
                Screen screenConfig = Config.getScreen(mc.gui.screen(), modId);
                mc.gui.setScreen(screenConfig);
                if (screenConfig.shouldCloseOnEsc()) MidnightConfig.write(modId);
            }
        });
    }
}