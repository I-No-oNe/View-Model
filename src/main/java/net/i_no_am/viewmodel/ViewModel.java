package net.i_no_am.viewmodel;

import io.github.itzispyder.improperui.ImproperUIAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.viewmodel.version.Version;
import net.i_no_am.viewmodel.config.Config;
import net.i_no_am.viewmodel.event.SecondMenuCallBack;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ViewModel implements ModInitializer, Global {

    public static final String API = "https://api.github.com/repos/I-No-oNe/View-Model/releases/latest";
    public static final String DOWNLOAD = "https://modrinth.com/mod/no-ones-view-model/versions";
    public static final KeyBinding BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("binds.viewmodel.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "binds.viewmodel"));

    @Override
    public void onInitialize() {
        ImproperUIAPI.init(modId, ViewModel.class, screens);

        WorldRenderEvents.AFTER_SETUP.register(context -> Version.create(API, DOWNLOAD).notifyUpdate(FabricLoader.getInstance().isDevelopmentEnvironment()));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Config.loadConfig();
            while (BIND.wasPressed()) {
                ImproperUIAPI.parseAndRunFile(modId, "screen.ui",new SecondMenuCallBack());
            }
        });
    }
}
