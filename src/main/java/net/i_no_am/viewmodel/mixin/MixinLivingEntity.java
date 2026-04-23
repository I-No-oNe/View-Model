package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity implements Global {

    @ModifyReturnValue(method = "getHandSwingDuration", at = @At("RETURN"))
    private int onGetHandSwingDuration(int original) {
        if (mc.player == null || (Object) this != mc.player) return original;
        if (Config.noHandSwing) return 0;
        if (Config.handSpeedSwing != 0 && StatusEffectUtil.hasHaste((LivingEntity) (Object) this)) {
            original += 1 + StatusEffectUtil.getHasteAmplifier((LivingEntity) (Object) this);
        }
        return MathHelper.clamp(Config.handSpeedSwing + original, 1, Integer.MAX_VALUE);
    }

    @ModifyVariable(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At("HEAD"), argsOnly = true)
    private Hand modifySwingHand(Hand hand) {
        if (mc.player == null || (Object) this != mc.player) return hand;
        return switch (Config.swingMode) {
            case MAINHAND -> Hand.MAIN_HAND;
            case OFFHAND -> Hand.OFF_HAND;
            default -> hand;
        };
    }
}
