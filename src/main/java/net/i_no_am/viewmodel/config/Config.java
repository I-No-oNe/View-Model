package net.i_no_am.viewmodel.config;

import eu.midnightdust.lib.config.MidnightConfig;
import net.i_no_am.viewmodel.Global;

public class Config extends MidnightConfig implements Global {

    public final static String TRANSFORMS = "transforms";
    public final static String SWING = "swing";
    public final static String EATING = "eating";
    public final static String MISC = "misc";

    public enum SwingState { DEFAULT, MAINHAND, OFFHAND }
    public enum HandsTarget { BOTH, HANDS_ONLY, ARMS_ONLY }

    // todo: page 1

    @Comment(category = TRANSFORMS, centered = true, url = "https://modrinth.com/user/I-No-oNe")
    public static String credits;

    @Comment(category = TRANSFORMS, centered = true)
    public static String transformTargetTitle;

    @Entry(category = TRANSFORMS, name = "Transform Target")
    public static HandsTarget HandsMode = HandsTarget.BOTH;

    @Comment(category = TRANSFORMS, centered = true)
    public static String mainHandTitle;

    @Entry(isSlider = true, category = TRANSFORMS, min = 0.1F, max = 5.0F, name = "Scale")
    public static float mainHandScale = 1.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -45, max = 45, name = "Position X")
    public static float mainPositionX = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -45, max = 45, name = "Position Y")
    public static float mainPositionY = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -30, max = 30,  name = "Position Z")
    public static float mainPositionZ = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -70, max = 70, name = "Rotation X (Pitch)")
    public static float mainRotationX = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -60, max = 60, name = "Rotation Y (Yaw)")
    public static float mainRotationY = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -60, max = 60, name = "Rotation Z (Roll)")
    public static float mainRotationZ = 0.0F;

    @Comment(category = TRANSFORMS, centered = true)
    public static String offHandTitle;

    @Entry(isSlider = true, category = TRANSFORMS, min = 0.1F, max = 5.0F, name = "Scale")
    public static float offHandScale = 1.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -45, max = 45, name = "Position X")
    public static float offPositionX = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -45, max = 45, name = "Position Y")
    public static float offPositionY = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -30, max = 30,  name = "Position Z")
    public static float offPositionZ = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -70, max = 70, name = "Rotation X (Pitch)")
    public static float offRotationX = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -60, max = 60, name = "Rotation Y (Yaw)")
    public static float offRotationY = 0.0F;

    @Entry(precision = 200, isSlider = true, category = TRANSFORMS, min = -60, max = 60, name = "Rotation Z (Roll)")
    public static float offRotationZ = 0.0F;

    // todo: page 2

    @Comment(category = SWING, centered = true)
    public static String swingSettingsTitle;

    @Entry(category = SWING, name = "Active Swing Mode")
    public static SwingState swingMode = SwingState.DEFAULT;

    @Entry(precision = 200, isSlider = true, category = SWING, min = -5, max = 20, name = "Speed Modifier (Ticks)")
    public static int handSpeedSwing = 0;

    @Comment(category = SWING, centered = true)
    public static String swingProgressTitle;

    @Entry(precision = 100, isSlider = true, category = SWING, min = 0.0F, max = 1.0F, name = "Main Hand Offset")
    public static float mainSwingProgress = 0.0F;

    @Entry(precision = 100, isSlider = true, category = SWING, min = 0.0F, max = 1.0F, name = "Off Hand Offset")
    public static float offSwingProgress = 0.0F;

    @Comment(category = SWING, centered = true)
    public static String swingTogglesTitle;

    @Entry(category = SWING, name = "Disable Swing Animation")
    public static boolean noHandSwing = false;

    // todo: page 3

    @Comment(category = EATING, centered = true)
    public static String eatingTitle;

    @Entry(category = EATING, name = "Enable Custom Animation")
    public static boolean customEatAnim = false;

    @Entry(category = EATING, name = "Disable Vanilla Animation")
    public static boolean noFoodSwing = false;

    @Comment(category = EATING, centered = true)
    public static String eatingIntensityTitle;

    @Entry(precision = 100, isSlider = true, category = EATING, min = 0.0F, max = 3.0F, name = "Intensity X")
    public static float eatX = 1.0F;

    @Entry(precision = 100, isSlider = true, category = EATING, min = 0.0F, max = 3.0F, name = "Intensity Y")
    public static float eatY = 1.0F;

    // todo: page 4

    @Comment(category = MISC, centered = true)
    public static String miscTitle;

    @Entry(category = MISC, name = "Old 1.8 Animations")
    public static boolean oldAnimations = false;

    @Entry(category = MISC, name = "Idle Sword Slash Pose")
    public static boolean swordSlash = false;

    @Entry(category = MISC, name = "Disable Item Equip Animation")
    public static boolean skipSwapping = false;

    @Entry(category = MISC, name = "Hide Hand Models Completely")
    public static boolean noHandRender = false;
}
