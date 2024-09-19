package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class MixinCamera implements Global {

    @Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
    private void onGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (mc.player.getHandItems() != null && cir.getReturnValue() == CameraSubmersionType.WATER && (Config.mainHandScale.getVal() != 1 || Config.offHandScale.getVal() != 1 ||
                Config.mainPositionX.getVal() != 0 || Config.mainPositionY.getVal() != 0 || Config.mainPositionZ.getVal() != 0 ||
                Config.offPositionX.getVal() != 0 || Config.offPositionY.getVal() != 0 || Config.offPositionZ.getVal() != 0)) {
            cir.setReturnValue(CameraSubmersionType.NONE);
            cir.cancel();
        }
    }
}