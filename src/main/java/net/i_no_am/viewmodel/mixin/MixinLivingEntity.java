package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.config.ConfigManager;
import net.i_no_am.viewmodel.config.settings.ViewModelSettings;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "getHandSwingDuration", at = @At("HEAD"), cancellable = true)
    private void onGetHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        if ((boolean) ConfigManager.get(ViewModelSettings.NO_SWING).getVal() || (boolean) ConfigManager.get(ViewModelSettings.NO_SWING_V2).getVal()) {
            cir.setReturnValue(0);
            cir.cancel();
        }
        if (!(boolean) ConfigManager.get(ViewModelSettings.NO_SWING).getVal() && !(boolean) ConfigManager.get(ViewModelSettings.NO_SWING_V2).getVal()){
            cir.setReturnValue((int) ConfigManager.get(ViewModelSettings.HAND_SWING_SPEED).getVal());
            cir.cancel();
        }
    }
}
