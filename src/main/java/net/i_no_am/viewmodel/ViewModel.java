package net.i_no_am.viewmodel;

import net.i_no_am.viewmodel.client.Global;
import net.i_no_am.viewmodel.gui.MenuCallbacks;
import io.github.itzispyder.improperui.ImproperUIAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.i_no_am.viewmodel.util.ViewModelSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ViewModel implements ModInitializer, Global {

    private boolean isInitialized = false;

    public static final KeyBinding BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "binds.viewmodel.menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "binds.viewmodel"
    ));

    @Override
    public void onInitialize() {
        ImproperUIAPI.init(modId, ViewModel.class, screens);
        isInitialized = true;
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ImproperUIAPI.reload(modId, ViewModel.class, screens);
            ImproperUIAPI.reInit(modId, ViewModel.class, screens);
            while (BIND.wasPressed()) {
                ImproperUIAPI.parseAndRunFile(modId, "screen.ui", new MenuCallbacks());
            }
            if (isInitialized) {
                ViewModelSettings.loadConfigValues();
            }
        });
    }
}
