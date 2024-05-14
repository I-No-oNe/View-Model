package net.i_no_am.viewmodel.util;

import net.i_no_am.viewmodel.gui.Config;

public class ViewModelSettings {

    private static float main_rotation_x;
    private static float main_position_x;
    private static float main_rotation_z;
    private static float main_position_z;
    private static float main_rotation_y;
    private static float main_position_y;

    private static float off_rotation_x;
    private static float off_position_x;
    private static float off_rotation_z;
    private static float off_position_z;
    private static float off_rotation_y;
    private static float off_position_y;

    public static void loadConfigValues() {
        main_rotation_x = (float) Config.readDouble("main-rotation-x");
        main_position_x = (float) Config.readDouble("main-position-x");
        main_rotation_z = (float) Config.readDouble("main-rotation-z");
        main_position_z = (float) Config.readDouble("main-position-z");
        main_rotation_y = (float) Config.readDouble("main-rotation-y");
        main_position_y = (float) Config.readDouble("main-position-y");
        off_rotation_x = (float) Config.readDouble("off-rotation-x");
        off_position_x = (float) Config.readDouble("off-position-x");
        off_rotation_z = (float) Config.readDouble("off-rotation-z");
        off_position_z = (float) Config.readDouble("off-position-z");
        off_rotation_y = (float) Config.readDouble("off-rotation-y");
        off_position_y = (float) Config.readDouble("off-position-y");
    }

    public static float getMainRotationX() {
        return main_rotation_x;
    }

    public static float getMainPositionX() {
        return main_position_x;
    }

    public static float getMainRotationZ() {
        return main_rotation_z;
    }

    public static float getMainPositionZ() {
        return main_position_z;
    }

    public static float getMainRotationY() {
        return main_rotation_y;
    }

    public static float getMainPositionY() {
        return main_position_y;
    }

    public static float getOffRotationX() {
        return off_rotation_x;
    }

    public static float getOffPositionX() {
        return off_position_x;
    }

    public static float getOffRotationZ() {
        return off_rotation_z;
    }

    public static float getOffPositionZ() {
        return off_position_z;
    }

    public static float getOffRotationY() {
        return off_rotation_y;
    }

    public static float getOffPositionY() {
        return off_position_y;
    }
}
