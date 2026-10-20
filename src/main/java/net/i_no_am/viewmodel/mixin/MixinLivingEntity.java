package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Adjusts the local player's swing duration and which hand swings.
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntity implements Global {

    @ModifyReturnValue(method = "getHandSwingDuration", at = @At("RETURN"))
    private int adjustSwingDuration(int original) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!isClientPlayer(self)) return original;
        if (Config.noHandSwing) return 0;
        if (Config.handSpeedSwing != 0 && StatusEffectUtil.hasHaste(self)) {
            original += 1 + StatusEffectUtil.getHasteAmplifier(self);
        }
        return MathHelper.clamp(Config.handSpeedSwing + original, 1, Integer.MAX_VALUE);
    }

    @ModifyVariable(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At("HEAD"), argsOnly = true)
    private Hand modifySwingHand(Hand hand) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!isClientPlayer(self)) return hand;
        return switch (Config.swingMode) {
            case MAINHAND -> Hand.MAIN_HAND;
            case OFFHAND -> Hand.OFF_HAND;
            default -> hand;
        };
    }

    @Unique
    private boolean isClientPlayer(LivingEntity self) {
        return mc.player != null && self == mc.player;
    }
}
