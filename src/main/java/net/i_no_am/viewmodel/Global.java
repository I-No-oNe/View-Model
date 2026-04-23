package net.i_no_am.viewmodel;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

public interface Global {
    @NotNull MinecraftClient mc = MinecraftClient.getInstance();
    String modId = "viewmodel";
}
