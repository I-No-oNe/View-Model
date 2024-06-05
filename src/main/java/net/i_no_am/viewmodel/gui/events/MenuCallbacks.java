package net.i_no_am.viewmodel.gui.events;

import io.github.itzispyder.improperui.script.CallbackHandler;
import io.github.itzispyder.improperui.script.CallbackListener;
import io.github.itzispyder.improperui.script.events.MouseEvent;
import net.i_no_am.viewmodel.client.Global;
import net.minecraft.util.Util;

public class MenuCallbacks implements CallbackListener, Global {

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