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

    public static final KeyBinding BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("View Model GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.UI_CATEGORY));
    public static final String API = "https://api.github.com/repos/I-No-oNe/View-Model/releases/latest";
    public static final String DOWNLOAD = "https://modrinth.com/mod/no-ones-view-model/versions";

    @Override
    public void onInitialize() {
        Log("ViewModel is initializing...");
        Log("View Model is checking for updates...");
        Log("View Model Config is registering...");
        Config.register();
        Log("View Model Config has been registered!");
        Log("ViewModel has been initialized!");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            new Version(API, DOWNLOAD).notifyV();
            Config.configFix();
            if (BIND.wasPressed()) {
                Log("Opening View Model GUI...");
                mc.setScreen(Config.getScreen(mc.currentScreen, modId));
            }
        });
    }

    public static void Log(String message) {
        if (isDev) return;
        System.out.println(message);
    }
}
/* TODO
 * 1. Update README.md
 * 2. Improve check for updates on startup (Better handling and messages instead of a screen mixin)
 * 3. Better debuging
 * 4. Add more comments to GUI
 * 5. Update features
 */
