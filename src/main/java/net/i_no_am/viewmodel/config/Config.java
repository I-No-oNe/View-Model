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

    public static void loadConfig() {
        /*First Page Settings:*/
        mainPositionX = new ConfigSettings<>(Double.class, VMConfig.readDouble("main-position-x", 0.0));
        mainRotationX = new ConfigSettings<>(Double.class, VMConfig.readDouble("main-rotation-x", 0.0));
        offPositionX = new ConfigSettings<>(Double.class, VMConfig.readDouble("off-position-x", 0.0));
        offRotationX = new ConfigSettings<>(Double.class, VMConfig.readDouble("off-rotation-x", 0.0));
        mainPositionY = new ConfigSettings<>(Double.class, VMConfig.readDouble("main-position-y", 0.0));
        mainRotationY = new ConfigSettings<>(Double.class, VMConfig.readDouble("main-rotation-y", 0.0));
        offPositionY = new ConfigSettings<>(Double.class, VMConfig.readDouble("off-position-y", 0.0));
        offRotationY = new ConfigSettings<>(Double.class, VMConfig.readDouble("off-rotation-y", 0.0));
        mainPositionZ = new ConfigSettings<>(Double.class, VMConfig.readDouble("main-position-z", 0.0));
        mainRotationZ = new ConfigSettings<>(Double.class, VMConfig.readDouble("main-rotation-z", 0.0));
        offPositionZ = new ConfigSettings<>(Double.class, VMConfig.readDouble("off-position-z", 0.0));
        offRotationZ = new ConfigSettings<>(Double.class, VMConfig.readDouble("off-rotation-z", 0.0));
        /*Second Page Settings:*/
        handSpeedSwing = new ConfigSettings<>(Double.class, VMConfig.readDouble("hand-speed-swing", 4.0));///it was 1 - 7, from now its 0 - 5 (2 and below didnt work)
        mainHandScale = new ConfigSettings<>(Double.class, VMConfig.readDouble("main-hand-scale", 1.0));
        offHandScale = new ConfigSettings<>(Double.class, VMConfig.readDouble("off-hand-scale", 1.0));
        noHandSwingV2 = new ConfigSettings<>(Boolean.class, VMConfig.readBool("no-hand-swing-v2", false));
        noHandSwingV1 = new ConfigSettings<>(Boolean.class, VMConfig.readBool("no-hand-swing-v1", false));
        noFoodSwing = new ConfigSettings<>(Boolean.class, VMConfig.readBool("no-food-swing", false));
        noHandRender = new ConfigSettings<>(Boolean.class, VMConfig.readBool("no-hand-render", false));
        /*No Swing Logic*/
        if (noHandSwingV2.getVal() && (noHandSwingV1.getVal())) {
            VMConfig.write("no-hand-swing", false);
            VMConfig.write("no-hand-swing-v2", false);
            ChatUtils.sendMessage(PREFIX + Formatting.RED + "CHOOSE ONE OPTION!");
        }
    }
}