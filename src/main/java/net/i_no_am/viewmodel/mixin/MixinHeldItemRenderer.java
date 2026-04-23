package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements Global {

    @Shadow private ItemStack mainHand;

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void onRenderHandsPos(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrixStack, OrderedRenderCommandQueue queue, int light, CallbackInfo ci) {
        matrixStack.push(); // pos/rot applied per-target: arm at renderArmHoldingItem, item at renderItem
    }

    @Unique
    private void applyPosRot(Hand hand, MatrixStack matrixStack) {
        if (hand == Hand.MAIN_HAND) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Config.mainRotationX));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(Config.mainRotationY));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(Config.mainRotationZ));
            matrixStack.translate(Config.mainPositionX, Config.mainPositionY, Config.mainPositionZ);
        } else {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Config.offRotationX));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(Config.offRotationY));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(Config.offRotationZ));
            matrixStack.translate(Config.offPositionX, Config.offPositionY, Config.offPositionZ);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("TAIL"))
    private void popMatrix(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrixStack, OrderedRenderCommandQueue queue, int light, CallbackInfo ci) {
        matrixStack.pop();
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V"))
    private void scaleItems(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack ms, OrderedRenderCommandQueue queue, int light, CallbackInfo ci) {
        if (Config.HandsMode != Config.HandsTarget.ARMS_ONLY) {
            applyPosRot(hand, ms);
            float scale = hand == Hand.MAIN_HAND ? Config.mainHandScale : Config.offHandScale;
            ms.scale(scale, scale, scale);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IFFLnet/minecraft/util/Arm;)V"), cancellable = true)
    private void noHands(AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, OrderedRenderCommandQueue queue, int light, CallbackInfo ci) {
        if (Config.noHandRender) {
            ci.cancel();
            return;
        }
        if (Config.HandsMode != Config.HandsTarget.HANDS_ONLY) {
            applyPosRot(hand, matrices);
            float scale = hand == Hand.MAIN_HAND ? Config.mainHandScale : Config.offHandScale;
            matrices.scale(scale, scale, scale);
        }
    }

    @ModifyArg(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V", ordinal = 0), index = 4)
    private float modifyMainHandSwing(float swing) {
        if (Config.swordSlash && mc.player != null && mainHand.isIn(ItemTags.SWORDS)) return 0f;
        return swing + Config.mainSwingProgress;
    }

    @ModifyArg(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V", ordinal = 1), index = 4)
    private float modifyOffHandSwing(float swing) {
        return swing + Config.offSwingProgress;
    }


    @ModifyReturnValue(method = "shouldSkipHandAnimationOnSwap", at = @At("RETURN"))
    private boolean skipSwapAnimation(boolean original) {
        return original || Config.skipSwapping;
    }

    @WrapOperation(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;shouldSkipHandAnimationOnSwap(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", ordinal = 0))
    private boolean skipMainHandSwapAnimation(HeldItemRenderer instance, ItemStack currentlyVisibleItem, ItemStack expectedItem, Operation<Boolean> original) {
        return Config.oldAnimations || original.call(instance, currentlyVisibleItem, expectedItem);
    }

    @ModifyExpressionValue(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getHandEquippingProgress(F)F"))
    private float removeItemSwapScale(float original) {
        return Config.oldAnimations ? 1.0F : original;
    }

    @WrapMethod(method = "applyEatOrDrinkTransformation")
    private void handleEatingAnimation(MatrixStack matrices, float tickProgress, Arm arm, ItemStack stack, PlayerEntity player, Operation<Void> original) {
        UseAction action = stack.getUseAction();
        if (action != UseAction.EAT && action != UseAction.DRINK) return;

        if (Config.noFoodSwing) return;

        if (Config.customEatAnim) {
            applyCustomEatOrDrinkTransformation(matrices, tickProgress, arm, stack);
            return;
        }

        original.call(matrices, tickProgress, arm, stack, player);
    }

    @Unique
    private void applyCustomEatOrDrinkTransformation(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack) {
        if (mc.player == null) return;

        float maxUseTime = (float) stack.getMaxUseTime(mc.player);
        if (maxUseTime <= 0.0F) return;

        float ticksProcessed = (maxUseTime - mc.player.getItemUseTimeLeft()) + tickDelta;
        float progress = MathHelper.clamp(ticksProcessed / maxUseTime, 0.0F, 1.0F);

        float side = arm == Arm.RIGHT ? 1.0F : -1.0F;

        float t = ticksProcessed;

        float intro = smoothstep(0.00F, 0.18F, progress);
        float reach = smoothstep(0.12F, 0.72F, progress);
        float finish = smoothstep(0.68F, 1.00F, progress);

        float orbit = (float) Math.sin(t * 0.55F) * (1.0F - finish);
        float bob = (float) Math.sin(t * 1.15F) * 0.018F * (1.0F - finish);
        float flutter = (float) Math.sin((progress * 18.0F) + (t * 0.25F)) * 0.010F * finish;
        float twist = (float) Math.sin(progress * 8.0F * Math.PI) * 0.012F;

        float x = side * (0.16F + 0.30F * intro + 0.10F * reach + 0.05F * finish) + side * orbit * 0.06F;
        float y = -0.08F - 0.18F * intro - 0.16F * reach - 0.05F * finish + bob + flutter;
        float z = -0.02F - 0.08F * intro - 0.10F * reach - 0.04F * finish;

        matrices.translate(x, y, z);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(side * (4.0F + 10.0F * intro + 18.0F * finish + orbit * 8.0F)));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * (14.0F + 28.0F * intro + 42.0F * reach + 18.0F * finish)));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + 10.0F * intro + 14.0F * finish + twist * 90.0F));

        matrices.translate(side * 0.015F * finish, 0.010F * finish, 0.0F);
    }

    @Unique
    private static float smoothstep(float edge0, float edge1, float x) {
        float t = MathHelper.clamp((x - edge0) / (edge1 - edge0), 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}
