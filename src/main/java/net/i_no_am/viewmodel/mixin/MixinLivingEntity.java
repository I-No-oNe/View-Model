package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Adjusts the local player's swing duration and which hand swings.
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntity implements Global {

    @ModifyReturnValue(method = "getCurrentSwingDuration", at = @At("RETURN"))
    private int adjustSwingDuration(int original) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!isClientPlayer(self)) return original;
        if (Config.noHandSwing) return 0;
        if (Config.handSpeedSwing != 0 && MobEffectUtil.hasDigSpeed(self)) {
            original += 1 + MobEffectUtil.getDigSpeedAmplification(self);
        }
        return Mth.clamp(Config.handSpeedSwing + original, 1, Integer.MAX_VALUE);
    }

    @ModifyVariable(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"), argsOnly = true, name = "hand")
    private InteractionHand modifySwingHand(InteractionHand hand) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!isClientPlayer(self)) return hand;
        return switch (Config.swingMode) {
            case MAINHAND -> InteractionHand.MAIN_HAND;
            case OFFHAND -> InteractionHand.OFF_HAND;
            default -> hand;
        };
    }

    @Unique
    private boolean isClientPlayer(LivingEntity self) {
        return mc.player != null && self == mc.player;
    }
}
