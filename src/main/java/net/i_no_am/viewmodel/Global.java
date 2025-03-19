package net.i_no_am.viewmodel;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface Global {
    @Nullable MinecraftClient mc = MinecraftClient.getInstance();
    String PREFIX = "§7[§aViewModel§7]§r ";
    String LOG = "ViewModel";
    String modId = LOG.toLowerCase(Locale.ROOT);
    boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
}
