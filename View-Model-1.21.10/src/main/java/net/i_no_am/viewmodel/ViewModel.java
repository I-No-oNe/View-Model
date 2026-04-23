package net.i_no_am.viewmodel;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.i_no_am.viewmodel.config.Config;
import net.i_no_am.viewmodel.version.Version;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ViewModel implements ModInitializer, Global {

    // todo: merge noHandSwingV1 and noHandSwingV2, add new options for MapHolding in one hand and both hands, switch to yacl3 with auto gen?, cleanup code

    public static final KeyBinding BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("View Model GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.Category.MISC));
    public static final String API = "https://api.github.com/repos/I-No-oNe/View-Model/releases/latest";
    public static final String DOWNLOAD = "https://modrinth.com/mod/no-ones-view-model/versions";

    @Override
    public void onInitialize() {
        Log("Initializing...");
        Log("Checking for updates...");
        Log("Config is registering...");
        Config.register();
        Log("Config has been registered!");
        Log("Has been initialized!");

        WorldRenderEvents.AFTER_ENTITIES.register((context) -> Version.create(API, DOWNLOAD).notifyUpdate(isDev));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (BIND.wasPressed() && mc.player != null) {
                var screenConfig = Config.getScreen(mc.currentScreen, modId);
                mc.setScreen(screenConfig);
                if (screenConfig.shouldCloseOnEsc()) MidnightConfig.write(modId);
            }
//            if (mc.player != null && mc.world != null) Config.configFix();
        });
    }

    public static void Log(String message) {
        if (!isDev) return;
        System.out.println("[ViewModel] " + message);
    }
}