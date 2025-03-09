package net.i_no_am.viewmodel;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.i_no_am.viewmodel.config.Config;
import net.i_no_am.viewmodel.version.Version;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ViewModel implements ModInitializer, Global {

    public static boolean isOutdated = false;
    public static final KeyBinding BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("View Model GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.UI_CATEGORY));

    @Override
    public void onInitialize() {
        Version.checkUpdates();
        Config.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Config.configFix();
            if (BIND.wasPressed()) {
                mc.setScreen(Config.getScreen(mc.currentScreen, modId));
            }
        });
    }
}
