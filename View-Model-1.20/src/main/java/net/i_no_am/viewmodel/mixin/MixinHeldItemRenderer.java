package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.ViewModelConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements Global {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    public void onMainRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrix, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        matrix.push();

        if (hand == Hand.MAIN_HAND) {
            double mainRotX = ViewModelConfig.getInstance().mainRotationX;
            double mainPosX = ViewModelConfig.getInstance().mainPositionX;
            double mainRotZ = ViewModelConfig.getInstance().mainRotationZ;
            double mainPosZ = ViewModelConfig.getInstance().mainPositionZ;
            double mainRotY = ViewModelConfig.getInstance().mainRotationY;
            double mainPosY = ViewModelConfig.getInstance().mainPositionY;

            matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) mainRotX));
            matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) mainRotY));
            matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(((float) mainRotZ)));
            matrix.translate(mainPosX, mainPosY, mainPosZ);
        } else {
            double offRotX = ViewModelConfig.getInstance().offRotationX;
            double offPosX = ViewModelConfig.getInstance().offPositionX;
            double offRotZ = ViewModelConfig.getInstance().offRotationZ;
            double offPosZ = ViewModelConfig.getInstance().offPositionZ;
            double offRotY = ViewModelConfig.getInstance().offRotationY;
            double offPosY = ViewModelConfig.getInstance().offPositionY;

            matrix.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) offRotX));
            matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) offRotY));
            matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) offRotZ));
            matrix.translate(offPosX, offPosY, offPosZ);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("TAIL"))
    public void onAfterRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrix, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        matrix.pop();
    }


    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void scaleForItems(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrix, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        var mainScale = ((float) ViewModelConfig.getInstance().mainHandScale);
        var offScale = ((float) ViewModelConfig.getInstance().offHandScale);

        if (hand == Hand.MAIN_HAND) matrix.scale(mainScale, mainScale, mainScale);
        else matrix.scale(offScale, offScale, offScale);
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFLnet/minecraft/util/Arm;)V"))
    private void noHandsRender(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrix, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (ViewModelConfig.getInstance().noHandRender) matrix.scale(0, 0, 0);
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    public void noEatingAnimations(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
        if (ViewModelConfig.getInstance().noFoodSwing) {
            ci.cancel();
        }
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At(value = "HEAD"), cancellable = true)
    public void onEat(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, CallbackInfo ci) {
        float f = (float) client.player.getItemUseTimeLeft() - tickDelta + 1.0F;
        float g = f / (float) stack.getMaxUseTime();
        float h;
        if (g < 0.8F && ViewModelConfig.getInstance().eatBobAmount > 0.0F) {
            float bob =
                    MathHelper.abs(
                            MathHelper.cos(f / 4.0F * (float) Math.PI)
                    ) * 0.005F * ViewModelConfig.getInstance().eatBobAmount;

            matrices.translate(0.0F, bob, 0.0F);
        }
        h = 1.0F - (float) Math.pow(g, 27.0);
        int i = arm == Arm.RIGHT ? 1 : -1;

        matrices.translate(h * 0.6F * (float) i * ViewModelConfig.getInstance().eatX, h * -0.5F * ViewModelConfig.getInstance().eatY, h * 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) i * h * 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(h * 10.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) i * h * 30.0F));

        ci.cancel();
    }

    @ModifyArgs(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void renderItem(Args args) {
        if (ViewModelConfig.getInstance().noHandSwingV1 && !ViewModelConfig.getInstance().noHandSwingV2) {
            args.set(6, 0.0F);
        }
    }

    @ModifyExpressionValue(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getHandSwingProgress(F)F"))
    private float modifySwing(float original) {
        return original + ViewModelConfig.getInstance().handSpeedSwing;
    }
}

