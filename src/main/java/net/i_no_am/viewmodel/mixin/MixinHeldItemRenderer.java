package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements Global {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    public void viewmodel(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        m.push();
        if (hand == Hand.MAIN_HAND) {
            float mainRotX = Config.mainRotationX;
            float mainPosX = Config.mainPositionX;
            float mainRotZ = Config.mainRotationZ;
            float mainPosZ = Config.mainPositionZ;
            float mainRotY = Config.mainRotationY;
            float mainPosY = Config.mainPositionY;

            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mainRotX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mainRotY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(mainRotZ));
            m.translate(mainPosX, mainPosY, mainPosZ);
        } else {
            float offRotX = Config.offRotationX;
            float offPosX = Config.offPositionX;
            float offRotZ = Config.offRotationZ;
            float offPosZ = Config.offPositionZ;
            float offRotY = Config.offRotationY;
            float offPosY = Config.offPositionY;

            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(offRotX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(offRotY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(offRotZ));
            m.translate(offPosX, offPosY, offPosZ);
        }
    }

    @Inject(method = "swingArm", at = @At("HEAD"), cancellable = true)
    private void doSwing(float swingProgress, float equipProgress, MatrixStack matrices, int armX, Arm arm, CallbackInfo ci) {
        if (Config.noHandSwingV3) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("TAIL"))
    public void popMatrixAfterRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        m.pop();
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void scaleForItems(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack ms, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        float mainScale = Config.mainHandScale;
        float offScale = Config.offHandScale;
        if (hand == Hand.MAIN_HAND) {
            ms.scale(mainScale, mainScale, mainScale);
        } else {
            ms.scale(offScale, offScale, offScale);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFLnet/minecraft/util/Arm;)V"))
    private void noHandsRender(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack ms, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Config.noHandRender) {
            ms.scale(0, 0, 0);
        }
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    public void noEatingAnimations(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, PlayerEntity player, CallbackInfo ci) {
        if (Config.noFoodSwing) {
            ci.cancel();
        }
    }

    @ModifyArgs(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void renderItem(Args args) {
        if (Config.noHandSwingV1 && !Config.noHandSwingV2) {
            args.set(6, 0.0F);
        }
    }
}

