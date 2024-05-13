package net.i_no_am.viewmodel.mixin;

import net.i_no_am.viewmodel.client.Global;
import net.i_no_am.viewmodel.util.ViewModelSettings;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer implements Global {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    public void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack m, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (hand == Hand.MAIN_HAND) {
            double mainRotX = ViewModelSettings.getMainRotationX();
            double mainPosX = ViewModelSettings.getMainPositionX();
            double mainRotZ = ViewModelSettings.getMainRotationZ();
            double mainPosZ = ViewModelSettings.getMainPositionZ();
            double mainRotY = ViewModelSettings.getMainRotationY();
            double mainPosY = ViewModelSettings.getMainPositionY();

            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) mainRotX));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) mainRotY));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) mainRotZ));
            m.translate((float) mainPosX, (float) mainPosY, (float) mainPosZ);
        } else {
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) 0));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) 0));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) 0));
            m.translate((float) 0, (float) 0, (float) 0);
        }
    }
}