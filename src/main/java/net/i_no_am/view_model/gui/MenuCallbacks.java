package net.i_no_am.view_model.gui;

import io.github.itzispyder.improperui.script.CallbackHandler;
import io.github.itzispyder.improperui.script.CallbackListener;
import io.github.itzispyder.improperui.script.events.MouseEvent;
import net.minecraft.util.Util;

public class MenuCallbacks implements CallbackListener {

    @CallbackHandler
    public void handleMouseCallbacks(MouseEvent e) {
        switch (e.input) {
            case CLICK -> onClick(e);
            case RELEASE -> onRelease(e);
        }
    }

    public void onClick(MouseEvent e) {
        if ("check-updates".equals(e.target.getId())) {
            Util.getOperatingSystem().open("https://github.com/I-No-oNe/view-model/releases");
        }
    }

    public void onRelease(MouseEvent e) {

    }
}