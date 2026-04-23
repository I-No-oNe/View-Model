package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity implements Global {

    @ModifyReturnValue(method = "getCurrentSwingDuration", at = @At("RETURN"))
    private int onGetHandSwingDuration(int original) {
        if (mc.player == null || (Object) this != mc.player) return original;
        if (Config.noHandSwing) return 0;
        if (Config.handSpeedSwing != 0 && MobEffectUtil.hasDigSpeed((LivingEntity) (Object) this)) {
            original += 1 + MobEffectUtil.getDigSpeedAmplification((LivingEntity) (Object) this);
        }
        return Mth.clamp(Config.handSpeedSwing + original, 1, Integer.MAX_VALUE);
    }

    @ModifyVariable(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"), argsOnly = true, name = "hand")
    private InteractionHand modifySwingHand(InteractionHand hand) {
        if (mc.player == null || (Object) this != mc.player) return hand;
        return switch (Config.swingMode) {
            case MAINHAND -> InteractionHand.MAIN_HAND;
            case OFFHAND -> InteractionHand.OFF_HAND;
            default -> hand;
        };
    }
}
