package net.i_no_am.viewmodel.mixin;

import eu.midnightdust.lib.config.EntryInfo;
import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightSliderWidget;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.Config;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MidnightSliderWidget.class)
public abstract class MixinMidnightSliderWidget extends AbstractSliderButton implements Global {

    @Shadow @Final private EntryInfo info;
    @Shadow @Final private MidnightConfig.Entry e;

    protected MixinMidnightSliderWidget(int x, int y, int width, int height, Component message, double value) {
        super(x, y, width, height, message, value);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (!isViewModelTransformSlider()) return super.keyPressed(event);

        if (event.isSelection()) {
            canChangeValue = !canChangeValue;
            return true;
        }

        if (canChangeValue && (event.isLeft() || event.isRight())) {
            adjustPrecisely(event.isLeft() ? -1.0D : 1.0D);
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
