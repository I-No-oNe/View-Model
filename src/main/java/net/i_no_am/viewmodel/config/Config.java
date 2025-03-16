package net.i_no_am.viewmodel.config;

import eu.midnightdust.lib.config.MidnightConfig;
import net.i_no_am.viewmodel.Global;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Config extends MidnightConfig implements Global {

    public final static String FIRST_PAGE = "Settings";
    public final static String SECOND_PAGE = "Advanced Settings";

    @Client
    @Comment(category = FIRST_PAGE, centered = true)
    public static String firstMsg;

    @Comment(category = FIRST_PAGE, centered = true)
    public static String secondMsg;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -45, max = 45, name = "Main hand position on X axis")
    public static float mainPositionX = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -70, max = 70, name = "Main hand rotation on X axis")
    public static float mainRotationX = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -45, max = 45, name = "Off hand position on X axis")
    public static float offPositionX = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -70, max = 70, name = "Off hand rotation on X axis")
    public static float offRotationX = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -45, max = 45, name = "Main hand position on Y axis")
    public static float mainPositionY = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -60, max = 60, name = "Main hand rotation on Y axis")
    public static float mainRotationY = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -45, max = 45, name = "Off hand position on Y axis")
    public static float offPositionY = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -60, max = 60, name = "Off hand rotation on Y axis")
    public static float offRotationY = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -30, max = 30, name = "Main hand position on Z axis")
    public static float mainPositionZ = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -60, max = 60, name = "Main hand rotation on Z axis")
    public static float mainRotationZ = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -30, max = 30, name = "Off hand position on Z axis")
    public static float offPositionZ = 0.0F;

    @Entry(isSlider = true, category = FIRST_PAGE, min = -60, max = 60, name = "Off hand rotation on Z axis")
    public static float offRotationZ = 0.0F;

    // Second Page Settings
    @Comment(category = SECOND_PAGE, centered = true)
    public static String thirdMsg;

    @Entry(isSlider = true, category = SECOND_PAGE, min = 0, max = 5, name = "Speed of the hand swing animation")
    public static int handSpeedSwing = 4;

    @Entry(isSlider = true, category = SECOND_PAGE, min = 0.1F, max = 5.0F, name = "Scale of the main hand")
    public static float mainHandScale = 1.0F;

    @Entry(isSlider = true, category = SECOND_PAGE, min = 0.1F, max = 5.0F, name = "Scale of the off hand")
    public static float offHandScale = 1.0F;

    @Comment(category = SECOND_PAGE, centered = true)
    public static String fourthMsg;
    @Entry(category = SECOND_PAGE, name = "Disables hand swinging animations (V1)")
    public static boolean noHandSwingV1 = false;

    @Entry(category = SECOND_PAGE, name = "Disables hand swinging animations (V2)")
    public static boolean noHandSwingV2 = false;

    @Entry(category = SECOND_PAGE, name = "Disables eating and drinking animations")
    public static boolean noFoodSwing = false;

    @Entry(category = SECOND_PAGE, name = "Hides the hand model")
    public static boolean noHandRender = false;

    public static void register() {
        MidnightConfig.init(modId, Config.class);
    }

    public static void configFix() {
        if (mc.player != null && noHandSwingV2 && noHandSwingV1) {
            noHandSwingV2 = false;
            noHandSwingV1 = false;
            mc.player.sendMessage(Text.of(PREFIX + Formatting.RED + "CHOOSE ONE OPTION!"), false);
        }
    }
}