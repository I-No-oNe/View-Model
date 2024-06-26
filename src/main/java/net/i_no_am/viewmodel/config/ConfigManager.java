package net.i_no_am.viewmodel.config;

import io.github.itzispyder.improperui.ImproperUIAPI;
import io.github.itzispyder.improperui.config.ConfigReader;
import net.i_no_am.viewmodel.client.Global;
import net.i_no_am.viewmodel.config.settings.ViewModelSettings;
import net.i_no_am.viewmodel.config.settings.SettingsManager;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager implements Global, SettingsManager {

    private static final Map<ViewModelSettings, SettingStructure<?>> system = new HashMap<>();

    /**
     * If you want to add more features, do it via using {@link SettingsManager} + {@link ViewModelSettings}.
     */


    public static void loadConfigValues() {
        ConfigReader VMconfig = ImproperUIAPI.getConfigReader(modId, "config.properties");

        system.put(ViewModelSettings.MAIN_ROT_X, new FloatSettingStructure((float) VMconfig.readFloat(ViewModelSettings.MAIN_ROT_X.getKey(), MAIN_ROTATION_X)));
        system.put(ViewModelSettings.MAIN_POS_X, new FloatSettingStructure((float) (VMconfig.readFloat(ViewModelSettings.MAIN_POS_X.getKey(), MAIN_POSITION_X) / DIVISION)));
        system.put(ViewModelSettings.MAIN_ROT_Z, new FloatSettingStructure((float) VMconfig.readFloat(ViewModelSettings.MAIN_ROT_Z.getKey(), MAIN_ROTATION_Z)));
        system.put(ViewModelSettings.MAIN_POS_Z, new FloatSettingStructure((float) (VMconfig.readFloat(ViewModelSettings.MAIN_POS_Z.getKey(), MAIN_POSITION_Z) / DIVISION)));
        system.put(ViewModelSettings.MAIN_ROT_Y, new FloatSettingStructure((float) VMconfig.readFloat(ViewModelSettings.MAIN_ROT_Y.getKey(), MAIN_ROTATION_Y)));
        system.put(ViewModelSettings.MAIN_POS_Y, new FloatSettingStructure((float) (VMconfig.readFloat(ViewModelSettings.MAIN_POS_Y.getKey(), MAIN_POSITION_Y) / DIVISION)));

        system.put(ViewModelSettings.OFF_ROT_X, new FloatSettingStructure((float) VMconfig.readFloat(ViewModelSettings.OFF_ROT_X.getKey(), OFF_ROTATION_X)));
        system.put(ViewModelSettings.OFF_POS_X, new FloatSettingStructure((float) (VMconfig.readFloat(ViewModelSettings.OFF_POS_X.getKey(), OFF_POSITION_X) / DIVISION)));
        system.put(ViewModelSettings.OFF_ROT_Z, new FloatSettingStructure((float) VMconfig.readFloat(ViewModelSettings.OFF_ROT_Z.getKey(), OFF_ROTATION_Z)));
        system.put(ViewModelSettings.OFF_POS_Z, new FloatSettingStructure((float) (VMconfig.readFloat(ViewModelSettings.OFF_POS_Z.getKey(), OFF_POSITION_Z) / DIVISION)));
        system.put(ViewModelSettings.OFF_ROT_Y, new FloatSettingStructure((float) VMconfig.readFloat(ViewModelSettings.OFF_ROT_Y.getKey(), OFF_ROTATION_Y)));
        system.put(ViewModelSettings.OFF_POS_Y, new FloatSettingStructure((float) (VMconfig.readFloat(ViewModelSettings.OFF_POS_Y.getKey(), OFF_POSITION_Y) / DIVISION)));

        system.put(ViewModelSettings.NO_SWING_V2, new BooleanSetting(VMconfig.readBool(ViewModelSettings.NO_SWING_V2.getKey(), NO_SWING_V2)));
        system.put(ViewModelSettings.NO_SWING, new BooleanSetting(VMconfig.readBool(ViewModelSettings.NO_SWING.getKey(), NO_SWING)));
        system.put(ViewModelSettings.HAND_SWING_SPEED, new IntegerSettingStructure(2 * VMconfig.readInt(ViewModelSettings.HAND_SWING_SPEED.getKey(), HAND_SWING_SPEED)));
        system.put(ViewModelSettings.NO_FOOD_SWING, new BooleanSetting(VMconfig.readBool(ViewModelSettings.NO_FOOD_SWING.getKey(), NO_FOOD_SWING)));
        system.put(ViewModelSettings.MAIN_SCALE, new FloatSettingStructure((float) VMconfig.readFloat(ViewModelSettings.MAIN_SCALE.getKey(), MAIN_SCALE)));
        system.put(ViewModelSettings.OFF_SCALE, new FloatSettingStructure((float) VMconfig.readFloat(ViewModelSettings.OFF_SCALE.getKey(), OFF_SCALE)));
        system.put(ViewModelSettings.NO_HAND, new BooleanSetting(VMconfig.readBool(ViewModelSettings.NO_HAND.getKey(), NO_HAND)));
    }

    public static SettingStructure<?> get(ViewModelSettings key) {
        return system.get(key);
    }
}
