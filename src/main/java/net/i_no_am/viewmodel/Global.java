package net.i_no_am.viewmodel;

import net.minecraft.client.MinecraftClient;

public interface Global {
    MinecraftClient mc = MinecraftClient.getInstance();
    String PREFIX = "§7[§aViewModel§7]§r ";
    String modId = "viewmodel";
}
