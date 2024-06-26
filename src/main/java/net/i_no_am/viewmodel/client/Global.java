package net.i_no_am.viewmodel.client;

import net.i_no_am.viewmodel.utils.Utils;

public interface Global {
    String PREFIX = "§7[§aViewModel§7]§r ";
    String CURRENT_VERSION = Utils.getModVersion();
    String modId = "viewmodel";
    String[] screens = {
            "assets/viewmodel/improperui/screen.ui",
            "assets/viewmodel/improperui/secondScreen.ui"
    };
}
