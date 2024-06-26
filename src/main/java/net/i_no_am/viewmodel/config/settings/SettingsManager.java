package net.i_no_am.viewmodel.config.settings;

import net.i_no_am.viewmodel.config.ConfigManager;

public interface SettingsManager {
/**
 * @Param Add here the default settings to every setting
 * After that go to:
 * {@link ConfigManager}
 */
    boolean NO_HAND = false;
    int HAND_SWING_SPEED = 6;
    int DIVISION = 10;
    boolean NO_SWING_V2 = false;
    boolean NO_SWING = false;
    boolean NO_FOOD_SWING = false;
    float MAIN_SCALE = 1.0F;
    float OFF_SCALE = 1.0F;
    float MAIN_ROTATION_X = 0.0F;
    float MAIN_POSITION_X = 0.0F;
    float MAIN_ROTATION_Z = 0.0F;
    float MAIN_POSITION_Z = 0.0F;
    float MAIN_ROTATION_Y = 0.0F;
    float MAIN_POSITION_Y = 0.0F;
    float OFF_ROTATION_X = 0.0F;
    float OFF_POSITION_X = 0.0F;
    float OFF_ROTATION_Z = 0.0F;
    float OFF_POSITION_Z = 0.0F;
    float OFF_ROTATION_Y = 0.0F;
    float OFF_POSITION_Y = 0.0F;
}
