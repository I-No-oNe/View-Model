package net.i_no_am.viewmodel.event;

import io.github.itzispyder.improperui.ImproperUIAPI;
import io.github.itzispyder.improperui.script.CallbackHandler;
import io.github.itzispyder.improperui.script.CallbackListener;
import io.github.itzispyder.improperui.script.events.MouseEvent;
import net.i_no_am.viewmodel.client.Global;


public class SecondMenuCallBack implements CallbackListener, Global {


    @CallbackHandler
    public void openHandsSettingScreen(MouseEvent e) {
        if (e.input.isDown())
            ImproperUIAPI.parseAndRunFile(modId, "secondScreen.ui");
    }
}

