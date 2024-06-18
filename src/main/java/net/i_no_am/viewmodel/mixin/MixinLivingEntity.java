package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.gui.ViewModelSettings;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "getHandSwingDuration", at= @At("HEAD"), cancellable = true)
    public void onGetHandSwingDuration(CallbackInfoReturnable<Integer> cir){
        if (ViewModelSettings.no_swing) {
            cir.setReturnValue(0);
            cir.cancel();
        }
    }
}
