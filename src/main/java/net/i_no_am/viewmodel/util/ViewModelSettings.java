package net.i_no_am.viewmodel.util;

import net.i_no_am.viewmodel.gui.Config;

public class ViewModelSettings {

    private static float main_rotation_x;
    private static float main_position_x;
    private static float main_rotation_z;
    private static float main_position_z;
    private static float main_rotation_y;
    private static float main_position_y;

    public static void loadConfigValues() {
        main_rotation_x = (float) Config.readDouble("main-rotation-x");
        main_position_x = (float) Config.readDouble("main-position-x");
        main_rotation_z = (float) Config.readDouble("main-position-z");
        main_position_z = (float) Config.readDouble("main-rotation-z");
        main_rotation_y = (float) Config.readDouble("main-rotation-y");
        main_position_y = (float) Config.readDouble("main-position-y");
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
}