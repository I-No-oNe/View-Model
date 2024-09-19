package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.config.Config;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(method = "getHandSwingDuration", at = @At("HEAD"), cancellable = true)
    private void onGetHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        if (Config.noHandSwingV1.getVal() || Config.noHandSwingV2.getVal()) {
            cir.setReturnValue(0);
            cir.cancel();
        }
        if (!Config.noHandSwingV1.getVal() && !Config.noHandSwingV2.getVal()) {
            cir.setReturnValue(Config.handSpeedSwing.getVal().intValue() + 2);
            cir.cancel();
        }
    }
}