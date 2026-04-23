package net.i_no_am.viewmodel;

import net.minecraft.client.MinecraftClient;

public interface Global {
    MinecraftClient mc = MinecraftClient.getInstance();
    String modId = "viewmodel";
}
