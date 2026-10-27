package net.i_no_am.viewmodel;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ViewModel implements ClientModInitializer, Global {

    public static final KeyBinding BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("Toggle ViewModel GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.Category.MISC));

    @Override
    public void onInitializeClient() {
        MidnightConfig.init(modId, Config.class);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (BIND.wasPressed() && mc.player != null) {
                Screen screenConfig = Config.getScreen(mc.currentScreen, modId);
                mc.setScreen(screenConfig);
                if (screenConfig.shouldCloseOnEsc()) MidnightConfig.write(modId);
            }
        });
    }
}