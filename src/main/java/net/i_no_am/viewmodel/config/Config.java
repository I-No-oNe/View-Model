package net.i_no_am.viewmodel.config;

import io.github.itzispyder.improperui.ImproperUIAPI;
import io.github.itzispyder.improperui.config.ConfigReader;
import io.github.itzispyder.improperui.util.ChatUtils;
import net.i_no_am.viewmodel.Global;
import net.i_no_am.viewmodel.config.settings.ConfigSettings;
import net.minecraft.util.Formatting;

public class Config implements Global {

    private static final ConfigReader VMConfig = ImproperUIAPI.getConfigReader(modId, "config.properties");

    /*First Page Settings:*/
    public static ConfigSettings<Double> mainPositionX;
    public static ConfigSettings<Double> mainRotationX;
    public static ConfigSettings<Double> offPositionX;
    public static ConfigSettings<Double> offRotationX;
    public static ConfigSettings<Double> mainPositionY;
    public static ConfigSettings<Double> mainRotationY;
    public static ConfigSettings<Double> offPositionY;
    public static ConfigSettings<Double> offRotationY;
    public static ConfigSettings<Double> mainPositionZ;
    public static ConfigSettings<Double> mainRotationZ;
    public static ConfigSettings<Double> offPositionZ;
    public static ConfigSettings<Double> offRotationZ;
    /*Second Page Settings*/
    public static ConfigSettings<Double> handSpeedSwing;
    public static ConfigSettings<Double> mainHandScale;
    public static ConfigSettings<Double> offHandScale;
    public static ConfigSettings<Boolean> noHandSwingV2;
    public static ConfigSettings<Boolean> noHandSwingV1;
    public static ConfigSettings<Boolean> noFoodSwing;
    public static ConfigSettings<Boolean> noHandRender;
    /*Secret Setting*/
    public static ConfigSettings<Boolean> shouldCheck;

    public static void loadConfig() {
        /*First Page Settings:*/
        mainPositionX = new ConfigSettings<>(VMConfig.readDouble("main-position-x", 0.0));
        mainRotationX = new ConfigSettings<>(VMConfig.readDouble("main-rotation-x", 0.0));
        offPositionX = new ConfigSettings<>(VMConfig.readDouble("off-position-x", 0.0));
        offRotationX = new ConfigSettings<>(VMConfig.readDouble("off-rotation-x", 0.0));
        mainPositionY = new ConfigSettings<>(VMConfig.readDouble("main-position-y", 0.0));
        mainRotationY = new ConfigSettings<>(VMConfig.readDouble("main-rotation-y", 0.0));
        offPositionY = new ConfigSettings<>(VMConfig.readDouble("off-position-y", 0.0));
        offRotationY = new ConfigSettings<>(VMConfig.readDouble("off-rotation-y", 0.0));
        mainPositionZ = new ConfigSettings<>(VMConfig.readDouble("main-position-z", 0.0));
        mainRotationZ = new ConfigSettings<>(VMConfig.readDouble("main-rotation-z", 0.0));
        offPositionZ = new ConfigSettings<>(VMConfig.readDouble("off-position-z", 0.0));
        offRotationZ = new ConfigSettings<>(VMConfig.readDouble("off-rotation-z", 0.0));
        /*Second Page Settings:*/
        handSpeedSwing = new ConfigSettings<>(VMConfig.readDouble("hand-speed-swing", 4.0));
        mainHandScale = new ConfigSettings<>(VMConfig.readDouble("main-hand-scale", 1.0));
        offHandScale = new ConfigSettings<>(VMConfig.readDouble("off-hand-scale", 1.0));
        noHandSwingV2 = new ConfigSettings<>(VMConfig.readBool("no-hand-swing-v2", false));
        noHandSwingV1 = new ConfigSettings<>(VMConfig.readBool("no-hand-swing-v1", false));
        noFoodSwing = new ConfigSettings<>(VMConfig.readBool("no-food-swing", false));
        noHandRender = new ConfigSettings<>(VMConfig.readBool("no-hand-render", false));
        /*Secret Setting*/
        shouldCheck = new ConfigSettings<>(VMConfig.readBool("should-check", true));

        /*No Swing Logic*/
        if (noHandSwingV2.getVal() && (noHandSwingV1.getVal())) {
            VMConfig.write("no-hand-swing", false);
            VMConfig.write("no-hand-swing-v2", false);
            ChatUtils.sendMessage(PREFIX + Formatting.RED + "CHOOSE ONE OPTION!");
        }
    }
}