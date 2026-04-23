package net.i_no_am.viewmodel;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public interface Global {
    @NotNull Minecraft mc = Minecraft.getInstance();
    String modId = "viewmodel";
}
