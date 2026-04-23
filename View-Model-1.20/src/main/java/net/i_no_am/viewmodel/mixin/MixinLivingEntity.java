package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.viewmodel.ViewModel;
import net.i_no_am.viewmodel.config.ViewModelConfig;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.i_no_am.viewmodel.Global.mc;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @ModifyReturnValue(method = "getHandSwingProgress", at = @At("RETURN"))
    private float getHandSwingProgress(float original) {
        if (ViewModelConfig.getInstance().noHandSwingV1 || ViewModelConfig.getInstance().noHandSwingV2) return 0.0F;

        return original * (ViewModelConfig.getInstance().handSpeedSwing + 2.0F);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        while (ViewModel.BIND.isPressed()) ViewModelConfig.createScreen(mc.currentScreen);
    }
}