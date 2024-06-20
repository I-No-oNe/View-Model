package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.client.Global;
import net.i_no_am.viewmodel.gui.ViewModelSettings;
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

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    public void OnApplyEatOrDrinkTransformation(MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, PlayerEntity player, CallbackInfo ci) {
        if (ViewModelSettings.no_food_swing) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    public void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        double mainRotX = ViewModelSettings.getMainRotationX();
        double mainPosX = ViewModelSettings.getMainPositionX();
        double mainRotZ = ViewModelSettings.getMainRotationZ();
        double mainPosZ = ViewModelSettings.getMainPositionZ();
        double mainRotY = ViewModelSettings.getMainRotationY();
        double mainPosY = ViewModelSettings.getMainPositionY();

        double offRotX = ViewModelSettings.getOffRotationX();
        double offPosX = ViewModelSettings.getOffPositionX();
        double offRotZ = ViewModelSettings.getOffRotationZ();
        double offPosZ = ViewModelSettings.getOffPositionZ();
        double offRotY = ViewModelSettings.getOffRotationY();
        double offPosY = ViewModelSettings.getOffPositionY();

        if (hand == Hand.MAIN_HAND) {
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) mainRotX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) mainRotY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) mainRotZ));
            m.translate(mainPosX, mainPosY, mainPosZ);
        } else {
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) offRotX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) offRotY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) offRotZ));
            m.translate(offPosX, offPosY, offPosZ);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void onRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack ms, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        float mainScale = ViewModelSettings.main_scale;
        float offScale = ViewModelSettings.off_scale;
        if (hand == Hand.MAIN_HAND) {
            ms.scale(mainScale, mainScale, mainScale);
        } else {
            ms.scale(offScale, offScale, offScale);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFLnet/minecraft/util/Arm;)V"))
    private void OnRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack ms, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        float mainScale = ViewModelSettings.main_scale;
        float offScale = ViewModelSettings.off_scale;
        if (hand == Hand.MAIN_HAND) {
            ms.scale(mainScale, mainScale, mainScale);
        } else {
            ms.scale(offScale, offScale, offScale);
        }
        if (ViewModelSettings.no_hand) {
            ms.scale(0, 0, 0);
        }
    }

    @ModifyArgs(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void renderItem(Args args) {
        if (ViewModelSettings.no_swing) {
            args.set(6, 0.0F);
        }
    }
}
