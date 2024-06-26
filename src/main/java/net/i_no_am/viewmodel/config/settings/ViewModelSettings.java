package net.i_no_am.viewmodel.config.settings;

public enum ViewModelSettings {
    MAIN_ROT_X("main-rotation-x"),
    MAIN_POS_X("main-position-x"),
    MAIN_ROT_Z("main-rotation-z"),
    MAIN_POS_Z("main-position-z"),
    MAIN_ROT_Y("main-rotation-y"),
    MAIN_POS_Y("main-position-y"),
    OFF_ROT_X("off-rotation-x"),
    OFF_POS_X("off-position-x"),
    OFF_ROT_Z("off-rotation-z"),
    OFF_POS_Z("off-position-z"),
    OFF_ROT_Y("off-rotation-y"),
    OFF_POS_Y("off-position-y"),
    NO_SWING("no-hand-swing"),
    NO_SWING_V2("no-hand-swing-v2"),
    HAND_SWING_SPEED("hand-speed-swing"),
    NO_FOOD_SWING("no-food-swing"),
    MAIN_SCALE("main-hand-scale"),
    OFF_SCALE("off-hand-scale"),
    NO_HAND("no-hand-render");

    private final String key;

    ViewModelSettings(String setting) {
        this.key = setting;
    }

    public String getKey() {
        return key;
    }
}
