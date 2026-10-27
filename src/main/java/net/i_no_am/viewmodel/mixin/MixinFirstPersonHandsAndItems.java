package net.i_no_am.viewmodel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class MixinFirstPersonHandsAndItems {

    @ModifyReturnValue(method = "shouldInstantlyReplaceVisibleItem", at = @At("RETURN"))
    private boolean skipSwapAnimation(boolean original) {
        return original || Config.skipSwapping;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/FirstPersonHandsAndItems;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/player/LocalPlayer;)Z", ordinal = 0))
    private boolean skipMainHandSwapAnimation(FirstPersonHandsAndItems instance, ItemStack currentlyVisibleItem, ItemStack expectedItem, LocalPlayer player, Operation<Boolean> original) {
        return Config.oldAnimations || original.call(instance, currentlyVisibleItem, expectedItem, player);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemSwapScale(F)F"))
    private float removeItemSwapScale(float original) {
        return (Config.oldAnimations || Config.skipSwapping) ? 1.0F : original;
    }
}
