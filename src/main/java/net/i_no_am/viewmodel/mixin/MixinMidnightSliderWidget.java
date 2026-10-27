package net.i_no_am.viewmodel.mixin;

import eu.midnightdust.lib.config.EntryInfo;
import eu.midnightdust.lib.config.MidnightSliderWidget;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.WeakHashMap;

/**
 * Adds exact small adjustments (selection key toggle, arrow keys and mouse wheel)
 * to a transform slider while it is focused.
 * The entry info is captured from the constructor instead of shadowed, so the mixin
 * works with all MidnightLib versions (older builds keep it in fields, 1.10.0 encloses it in lambdas).
 */
@Mixin(MidnightSliderWidget.class)
public abstract class MixinMidnightSliderWidget extends AbstractSliderButton implements Global {

    protected MixinMidnightSliderWidget(int x, int y, int width, int height, Component message, double value) {
        super(x, y, width, height, message, value);
    }

    @Unique
    private static final WeakHashMap<Object, EntryInfo> VIEW_MODEL_SLIDERS = new WeakHashMap<>();

    @Inject(method = "<init>(IIIILnet/minecraft/network/chat/Component;DLeu/midnightdust/lib/config/EntryInfo;)V", at = @At("RETURN"), require = 0)
    private void captureEntryInfo(int x, int y, int width, int height, Component message, double value, EntryInfo info, CallbackInfo ci) {
        if (info == null) return;
        VIEW_MODEL_SLIDERS.put(this, info);
    }

    @Unique
    private static EntryInfo getEntryInfo(MixinMidnightSliderWidget widget) {
        return VIEW_MODEL_SLIDERS.get(widget);
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        if (!isViewModelTransformSlider()) return super.keyPressed(event);

        if (event.isSelection()) {
            canChangeValue = !canChangeValue;
            return true;
        }

        if (canChangeValue && (event.isLeft() || event.isRight())) {
            adjustPrecisely(event.isLeft() ? -1.0 : 1.0);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (isViewModelTransformSlider() && isFocused() && isHovered() && verticalAmount != 0.0) {
            adjustPrecisely(Math.signum(verticalAmount));
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Unique
    private boolean isViewModelTransformSlider() {
        EntryInfo info = getEntryInfo(this);
        return info != null && modId.equals(info.modid) && Config.TRANSFORMS.equals(info.entry.category());
    }

    @Unique
    private void adjustPrecisely(double direction) {
        EntryInfo info = getEntryInfo(this);
        if (info == null) return;
        double range = info.entry.max() - info.entry.min();
        setValue(value + direction / (info.entry.precision() * range));
    }
}
