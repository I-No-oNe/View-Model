package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.client.ModVersionChecker;
import net.i_no_am.viewmodel.utils.Utils;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {


    @Inject(method = "getConnection", at = @At("RETURN"))
    private void onWorldLoadMixin(CallbackInfoReturnable<ClientConnection> cir) {
        try {
            ModVersionChecker.updateChecker();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.setChecked(false);
        }
    }
}