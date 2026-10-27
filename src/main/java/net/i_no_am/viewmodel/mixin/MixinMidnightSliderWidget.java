package net.i_no_am.viewmodel.mixin;

import eu.midnightdust.lib.config.EntryInfo;
import eu.midnightdust.lib.config.MidnightSliderWidget;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
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
public abstract class MixinMidnightSliderWidget extends SliderWidget implements Global {

    protected MixinMidnightSliderWidget(int x, int y, int width, int height, Text message, double value) {
        super(x, y, width, height, message, value);
    }

    @Unique
    private static final WeakHashMap<Object, EntryInfo> VIEW_MODEL_SLIDERS = new WeakHashMap<>();

    @Inject(method = "<init>(IIIILnet/minecraft/text/Text;DLeu/midnightdust/lib/config/EntryInfo;)V", at = @At("RETURN"), require = 0)
    private void captureEntryInfo(int x, int y, int width, int height, Text message, double value, EntryInfo info, CallbackInfo ci) {
        if (info == null) return;
        VIEW_MODEL_SLIDERS.put(this, info);
    }

    @Unique
    private static EntryInfo getEntryInfo(MixinMidnightSliderWidget widget) {
        return VIEW_MODEL_SLIDERS.get(widget);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (!isViewModelTransformSlider()) return super.keyPressed(input);

        if (input.isEnterOrSpace()) {
            sliderFocused = !sliderFocused;
            return true;
        }

        if (sliderFocused && (input.isLeft() || input.isRight())) {
            adjustPrecisely(input.isLeft() ? -1.0D : 1.0D);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (isViewModelTransformSlider() && isFocused() && isHovered() && verticalAmount != 0.0D) {
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
