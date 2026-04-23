package net.i_no_am.viewmodel.mixin;

import eu.midnightdust.lib.config.EntryInfo;
import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightSliderWidget;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MidnightSliderWidget.class)
public abstract class MixinMidnightSliderWidget extends SliderWidget implements Global {

    @Shadow @Final private EntryInfo info;
    @Shadow @Final private MidnightConfig.Entry e;

    protected MixinMidnightSliderWidget(int x, int y, int width, int height, Text message, double value) {
        super(x, y, width, height, message, value);
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
        return modId.equals(info.modid) && Config.TRANSFORMS.equals(e.category());
    }

    @Unique
    private void adjustPrecisely(double direction) {
        double range = e.max() - e.min();
        setValue(value + direction / (e.precision() * range));
    }
}
