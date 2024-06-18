package net.i_no_am.viewmodel.gui;


import io.github.itzispyder.improperui.ImproperUIAPI;
import net.i_no_am.viewmodel.client.Global;

public class ViewModelSettings implements Global {

    public static boolean no_swing = false;
    public static boolean no_food_swing = false;

    public static int normal_division = 10;

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
//        view model
        main_rotation_x = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("main-rotation-x", 0.0F);
        main_position_x = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("main_position_x",0.0F)/normal_division;
        main_rotation_z = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("main-rotation-z",0.0F);
        main_position_z = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("main-position-z",0.0F) / normal_division;
        main_rotation_y = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("main-rotation-y",0.0F);
        main_position_y = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("main-position-y",0.0F) / normal_division;
        off_rotation_x = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("off-rotation-x",0.0F);
        off_position_x = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("off-position-x",0.0F) / normal_division;
        off_rotation_z = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("off-rotation-z",0.0F);
        off_position_z = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("off-position-z",0.0F) / normal_division;
        off_rotation_y = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("off-rotation-y",0.0F);
        off_position_y = (float) ImproperUIAPI.getConfigReader(modId, "config.properties").readFloat("off-position-y",0.0F) / normal_division;
        no_swing = ImproperUIAPI.getConfigReader(modId, "config.properties").readBool("no-hand-swing",false);
        no_food_swing = ImproperUIAPI.getConfigReader(modId, "config.properties").readBool("no-food-swing",false);
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
