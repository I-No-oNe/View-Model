package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * Handles the first person hands and items transforms: position, rotation and scale for
 * both hands, the swing progress offsets and the custom eat and drink animation.
 */
@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class MixinItemInHandRenderer implements Global {

    @Inject(method = "submitArmWithItem", at = @At("HEAD"))
    private void pushArmPose(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState renderState, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        poseStack.pushPose(); // transforms are applied per target: arm at renderPlayerArm, item at submit
    }

    @Inject(method = "submitArmWithItem", at = @At("TAIL"))
    private void popArmPose(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState renderState, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        poseStack.popPose();
    }

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"))
    private void applyItemTransform(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState renderState, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (Config.HandsMode == Config.HandsTarget.ARMS_ONLY) return;
        applyHandTransform(hand, poseStack);
    }

    @Inject(method = "renderPlayerHand", at = @At("HEAD"))
    private void applyArmTransform(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HumanoidArm arm, PlayerRenderState playerState, CallbackInfo ci) {
        if (Config.HandsMode == Config.HandsTarget.HANDS_ONLY) return;
        applyHandTransform(usedHand(), poseStack);
    }

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;renderPlayerArm(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IFFLnet/minecraft/world/entity/HumanoidArm;Lnet/minecraft/client/renderer/state/level/PlayerRenderState;)V"), cancellable = true)
    private void hideArmsOnDemand(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState renderState, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (Config.noHandRender) {
            ci.cancel();
            return;
        }
        if (Config.HandsMode == Config.HandsTarget.HANDS_ONLY) return;
        applyHandTransform(hand, poseStack);
    }

    @ModifyArgs(method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lnet/minecraft/client/renderer/state/level/FirstPersonHandsAndItemsRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;submitArmWithItem(Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lnet/minecraft/client/renderer/state/level/FirstPersonHandsAndItemsRenderState;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", ordinal = 0))
    private void modifyMainHandSwing(Args args) {
        ItemStack itemStack = args.get(6);
        if (Config.swordSlash && itemStack.is(ItemTags.SWORDS)) {
            args.set(5, 0.0F);
        } else {
            args.set(5, (float) args.get(5) + Config.mainSwingProgress);
        }
    }

    @ModifyArgs(method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lnet/minecraft/client/renderer/state/level/FirstPersonHandsAndItemsRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;submitArmWithItem(Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lnet/minecraft/client/renderer/state/level/FirstPersonHandsAndItemsRenderState;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", ordinal = 1))
    private void modifyOffHandSwing(Args args) {
        args.set(5, (float) args.get(5) + Config.offSwingProgress);
    }

    @WrapMethod(method = "applyEatTransform")
    private void applyEatingAnimation(PoseStack poseStack, float partialTicks, HumanoidArm arm, float useItemRemainingTicks, int useDuration, Operation<Void> original) {
        if (Config.noFoodSwing) return;
        if (Config.customEatAnim) {
            applyCustomEatOrDrinkTransform(poseStack, partialTicks, arm, useItemRemainingTicks, useDuration);
            return;
        }
        original.call(poseStack, partialTicks, arm, useItemRemainingTicks, useDuration);
    }

    @Unique
    private static InteractionHand usedHand() {
        return mc.player.getUsedItemHand();
    }

    @Unique
    private static void applyHandTransform(InteractionHand hand, PoseStack poseStack) {
        if (hand == InteractionHand.MAIN_HAND) {
            poseStack.rotate(Axis.XP.rotationDegrees(Config.mainRotationX));
            poseStack.rotate(Axis.YP.rotationDegrees(Config.mainRotationY));
            poseStack.rotate(Axis.ZP.rotationDegrees(Config.mainRotationZ));
            poseStack.translate(Config.mainPositionX, Config.mainPositionY, Config.mainPositionZ);
            float scale = Config.mainHandScale;
            poseStack.scale(scale, scale, scale);
        } else {
            poseStack.rotate(Axis.XP.rotationDegrees(Config.offRotationX));
            poseStack.rotate(Axis.YP.rotationDegrees(Config.offRotationY));
            poseStack.rotate(Axis.ZP.rotationDegrees(Config.offRotationZ));
            poseStack.translate(Config.offPositionX, Config.offPositionY, Config.offPositionZ);
            float scale = Config.offHandScale;
            poseStack.scale(scale, scale, scale);
        }
    }

    /**
     * A custom swirl style eat and drink animation, replacing the vanilla one.
     * The intensity sliders scale the horizontal and vertical part of the animation.
     */
    @Unique
    private static void applyCustomEatOrDrinkTransform(PoseStack poseStack, float frameInterp, HumanoidArm arm, float useItemRemainingTicks, int useDuration) {
        float maxUseTime = (float) useDuration;
        if (maxUseTime <= 0.0F) return;

        float ticksProcessed = (maxUseTime - useItemRemainingTicks) + frameInterp;
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

        poseStack.translate(x * Config.eatX, y * Config.eatY, z);

        poseStack.rotate(Axis.ZP.rotationDegrees(side * (4.0F + 10.0F * intro + 18.0F * finish + orbit * 8.0F)));
        poseStack.rotate(Axis.YP.rotationDegrees(side * (14.0F + 28.0F * intro + 42.0F * reach + 18.0F * finish)));
        poseStack.rotate(Axis.XP.rotationDegrees(6.0F + 10.0F * intro + 14.0F * finish + twist * 90.0F));

        poseStack.translate(side * 0.015F * finish, 0.010F * finish, 0.0F);
    }

    @Unique
    private static float smoothstep(float edge0, float edge1, float x) {
        float t = Mth.clamp((x - edge0) / (edge1 - edge0), 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}
