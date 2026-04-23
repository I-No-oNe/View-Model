package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemInHandRenderer implements Global {

    @Shadow private ItemStack mainHandItem;

    @Inject(method = "submitArmWithItem", at = @At("HEAD"))
    private void onRenderHandsPos(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        poseStack.pushPose(); // pos/rot applied per-target: arm at renderPlayerArm, item at renderItem
    }

    @Unique
    private void applyPosRot(InteractionHand hand, PoseStack poseStack) {
        if (hand == InteractionHand.MAIN_HAND) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Config.mainRotationX));
            poseStack.mulPose(Axis.YP.rotationDegrees(Config.mainRotationY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Config.mainRotationZ));
            poseStack.translate(Config.mainPositionX, Config.mainPositionY, Config.mainPositionZ);
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(Config.offRotationX));
            poseStack.mulPose(Axis.YP.rotationDegrees(Config.offRotationY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Config.offRotationZ));
            poseStack.translate(Config.offPositionX, Config.offPositionY, Config.offPositionZ);
        }
    }

    @Inject(method = "submitArmWithItem", at = @At("TAIL"))
    private void popMatrix(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        matrixStack.popPose();
    }

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"))
    private void scaleItems(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float swingProgress, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (Config.HandsMode != Config.HandsTarget.ARMS_ONLY) {
            applyPosRot(hand, poseStack);
            float scale = hand == InteractionHand.MAIN_HAND ? Config.mainHandScale : Config.offHandScale;
            poseStack.scale(scale, scale, scale);
        }
    }

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderPlayerArm(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IFFLnet/minecraft/world/entity/HumanoidArm;)V"), cancellable = true)
    private void noHands(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (Config.noHandRender) {
            ci.cancel();
            return;
        }
        if (Config.HandsMode != Config.HandsTarget.HANDS_ONLY) {
            applyPosRot(hand, poseStack);
            float scale = hand == InteractionHand.MAIN_HAND ? Config.mainHandScale : Config.offHandScale;
            poseStack.scale(scale, scale, scale);
        }
    }

    @ModifyArg(method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;submitArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", ordinal = 0), index = 4)
    private float modifyMainHandSwing(float swing) {
        if (Config.swordSlash && mc.player != null && mainHandItem.is(ItemTags.SWORDS)) return 0f;
        return swing + Config.mainSwingProgress;
    }

    @ModifyArg(method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;submitArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", ordinal = 1), index = 4)
    private float modifyOffHandSwing(float swing) {
        return swing + Config.offSwingProgress;
    }


    @ModifyReturnValue(method = "shouldInstantlyReplaceVisibleItem", at = @At("RETURN"))
    private boolean skipSwapAnimation(boolean original) {
        return original || Config.skipSwapping;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 0))
    private boolean skipMainHandSwapAnimation(ItemInHandRenderer instance, ItemStack currentlyVisibleItem, ItemStack expectedItem, Operation<Boolean> original) {
        return Config.oldAnimations || original.call(instance, currentlyVisibleItem, expectedItem);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemSwapScale(F)F"))
    private float removeItemSwapScale(float original) {
        return Config.oldAnimations ? 1.0F : original;
    }

    @WrapMethod(method = "applyEatTransform")
    private void handleEatingAnimation(PoseStack poseStack, float frameInterp, HumanoidArm arm, ItemStack itemStack, Player player, Operation<Void> original) {
        ItemUseAnimation action = itemStack.getUseAnimation();
        if (action != ItemUseAnimation.EAT && action != ItemUseAnimation.DRINK) return;

        if (Config.noFoodSwing) return;

        if (Config.customEatAnim) {
            applyCustomEatOrDrinkTransformation(poseStack, frameInterp, arm, itemStack);
            return;
        }

        original.call(poseStack, frameInterp, arm, itemStack, player);
    }

    @Unique
    private void applyCustomEatOrDrinkTransformation(PoseStack poseStack, float frameInterp, HumanoidArm arm,  ItemStack stack) {
        if (mc.player == null) return;

        float maxUseTime = (float) stack.getUseDuration(mc.player);
        if (maxUseTime <= 0.0F) return;

        float ticksProcessed = (maxUseTime - mc.player.getUseItemRemainingTicks()) + frameInterp;
        float progress = Mth.clamp(ticksProcessed / maxUseTime, 0.0F, 1.0F);

        float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;

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

        poseStack.translate(x, y, z);

        poseStack.mulPose(Axis.ZP.rotationDegrees(side * (4.0F + 10.0F * intro + 18.0F * finish + orbit * 8.0F)));
        poseStack.mulPose(Axis.YP.rotationDegrees(side * (14.0F + 28.0F * intro + 42.0F * reach + 18.0F * finish)));
        poseStack.mulPose(Axis.XP.rotationDegrees(6.0F + 10.0F * intro + 14.0F * finish + twist * 90.0F));

        poseStack.translate(side * 0.015F * finish, 0.010F * finish, 0.0F);
    }

    @Unique
    private static float smoothstep(float edge0, float edge1, float x) {
        float t = Mth.clamp((x - edge0) / (edge1 - edge0), 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}
