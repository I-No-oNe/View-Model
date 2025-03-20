package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
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
    public void onRenderHandsPos(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        m.push();

        if (hand == Hand.MAIN_HAND) {
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Config.mainRotationX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(Config.mainRotationY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(Config.mainRotationZ));
            m.translate(Config.mainPositionX, Config.mainPositionY, Config.mainPositionZ);
        } else {
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Config.offRotationX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(Config.offRotationY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(Config.offRotationZ));
            m.translate(Config.offPositionX, Config.offPositionY, Config.offPositionZ);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void scaleItems(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack ms, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        float mainScale = Config.mainHandScale;
        float offScale = Config.offHandScale;
        if (hand == Hand.MAIN_HAND) {
            ms.scale(mainScale, mainScale, mainScale);
        } else {
            ms.scale(offScale, offScale, offScale);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFLnet/minecraft/util/Arm;)V"), cancellable = true)
    private void noHands(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack ms, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Config.noHandRender) ci.cancel();
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    public void noEating(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, PlayerEntity player, CallbackInfo ci) {
        if (Config.noFoodSwing && mc.player.getActiveItem().getUseAction().equals(UseAction.EAT)) ci.cancel();
    }

    @ModifyArgs(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void renderItem(Args args) {
        if (Config.noHandSwingV1 && !Config.noHandSwingV2) {
            args.set(6, 0.0F);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("TAIL"))
    public void popMatrix(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        m.pop();
    }
}