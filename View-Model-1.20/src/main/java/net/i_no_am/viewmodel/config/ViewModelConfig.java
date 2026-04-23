package net.i_no_am.viewmodel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.fabricmc.loader.api.FabricLoader;
import net.i_no_am.viewmodel.ViewModel;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ViewModelConfig {

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("%s.json".formatted(ViewModel.modId));
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // --- POSITIONS ---
    public double mainPositionX = 0.0;
    public double mainPositionY = 0.0;
    public double mainPositionZ = 0.0;
    public double offPositionX = 0.0;
    public double offPositionY = 0.0;
    public double offPositionZ = 0.0;

    // --- ROTATIONS ---
    public double mainRotationX = 0.0;
    public double mainRotationY = 0.0;
    public double mainRotationZ = 0.0;
    public double offRotationX = 0.0;
    public double offRotationY = 0.0;
    public double offRotationZ = 0.0;

    // --- SCALE ---
    public double mainHandScale = 1.0;
    public double offHandScale = 1.0;

    // --- SWING ---
    public int handSpeedSwing = 0;
    public boolean noHandSwingV1 = false;
    public boolean noHandSwingV2 = false;
    public boolean noFoodSwing = false;
    public boolean noHandRender = false;

    // --- EATING ---
    public boolean eatingAnimation = false;
    public float eatX = 0.0f;
    public float eatY = 0.0f;
    public float eatBobAmount = 2.0f;

    private static ViewModelConfig INSTANCE = new ViewModelConfig();

    public static ViewModelConfig getInstance() {
        return INSTANCE;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                INSTANCE = GSON.fromJson(json, ViewModelConfig.class);
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelConfig();
                }
            } catch (IOException e) {
                System.err.println("Failed to load ViewModel config: " + e.getMessage());
                INSTANCE = new ViewModelConfig();
            }
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(INSTANCE);
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            System.err.println("Failed to save ViewModel config: " + e.getMessage());
        }
    }

    public static void createScreen(Screen parent) {
        YetAnotherConfigLib.createBuilder()
                .title(Text.literal("ViewModel Configuration"))
                .category(buildPositionCategory())
                .category(buildRotationCategory())
                .category(buildScaleCategory())
                .category(buildSwingCategory())
                .category(buildEatingCategory())
                .save(ViewModelConfig::save).build()
                .generateScreen(parent);
    }

    private static ConfigCategory buildPositionCategory() {
        ViewModelConfig config = getInstance();

        return ConfigCategory.createBuilder().name(Text.literal("Hand Positions")).group(OptionGroup.createBuilder().name(Text.literal("Main Hand")).option(Option.<Double>createBuilder().name(Text.literal("Position X")).binding(-2.0, () -> config.mainPositionX, val -> config.mainPositionX = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-2.0, 2.0).step(0.05)).build()).option(Option.<Double>createBuilder().name(Text.literal("Position Y")).binding(-2.0, () -> config.mainPositionY, val -> config.mainPositionY = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-2.0, 2.0).step(0.05)).build()).option(Option.<Double>createBuilder().name(Text.literal("Position Z")).binding(-2.0, () -> config.mainPositionZ, val -> config.mainPositionZ = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-2.0, 2.0).step(0.05)).build()).build()).group(OptionGroup.createBuilder().name(Text.literal("Off Hand")).option(Option.<Double>createBuilder().name(Text.literal("Position X")).binding(-2.0, () -> config.offPositionX, val -> config.offPositionX = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-2.0, 2.0).step(0.05)).build()).option(Option.<Double>createBuilder().name(Text.literal("Position Y")).binding(-2.0, () -> config.offPositionY, val -> config.offPositionY = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-2.0, 2.0).step(0.05)).build()).option(Option.<Double>createBuilder().name(Text.literal("Position Z")).binding(-2.0, () -> config.offPositionZ, val -> config.offPositionZ = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-2.0, 2.0).step(0.05)).build()).build()).build();
    }

    private static ConfigCategory buildRotationCategory() {
        ViewModelConfig config = getInstance();

        return ConfigCategory.createBuilder().name(Text.literal("Hand Rotations")).group(OptionGroup.createBuilder().name(Text.literal("Main Hand")).option(Option.<Double>createBuilder().name(Text.literal("Rotation X")).binding(0.0, () -> config.mainRotationX, val -> config.mainRotationX = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-180.0, 180.0).step(1.0)).build()).option(Option.<Double>createBuilder().name(Text.literal("Rotation Y")).binding(0.0, () -> config.mainRotationY, val -> config.mainRotationY = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-180.0, 180.0).step(1.0)).build()).option(Option.<Double>createBuilder().name(Text.literal("Rotation Z")).binding(0.0, () -> config.mainRotationZ, val -> config.mainRotationZ = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-180.0, 180.0).step(1.0)).build()).build()).group(OptionGroup.createBuilder().name(Text.literal("Off Hand")).option(Option.<Double>createBuilder().name(Text.literal("Rotation X")).binding(0.0, () -> config.offRotationX, val -> config.offRotationX = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-180.0, 180.0).step(1.0)).build()).option(Option.<Double>createBuilder().name(Text.literal("Rotation Y")).binding(0.0, () -> config.offRotationY, val -> config.offRotationY = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-180.0, 180.0).step(1.0)).build()).option(Option.<Double>createBuilder().name(Text.literal("Rotation Z")).binding(0.0, () -> config.offRotationZ, val -> config.offRotationZ = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(-180.0, 180.0).step(1.0)).build()).build()).build();
    }

    private static ConfigCategory buildScaleCategory() {
        ViewModelConfig config = getInstance();

        return ConfigCategory.createBuilder().name(Text.literal("Hand Scaling")).option(Option.<Double>createBuilder().name(Text.literal("Main Hand Scale")).binding(1.0, () -> config.mainHandScale, val -> config.mainHandScale = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.1, 2.0).step(0.05)).build()).option(Option.<Double>createBuilder().name(Text.literal("Off Hand Scale")).binding(1.0, () -> config.offHandScale, val -> config.offHandScale = val).controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.1, 2.0).step(0.05)).build()).build();
    }

    private static ConfigCategory buildSwingCategory() {
        ViewModelConfig config = getInstance();

        return ConfigCategory.createBuilder().name(Text.literal("Hand Swing Settings")).option(Option.<Integer>createBuilder().name(Text.literal("Hand Speed Swing")).binding(0, () -> config.handSpeedSwing, val -> config.handSpeedSwing = val).controller(opt -> IntegerSliderControllerBuilder.create(opt).range(-100, 100).step(1)).build()).option(Option.<Boolean>createBuilder().name(Text.literal("No Hand Swing V1")).binding(false, () -> config.noHandSwingV1, val -> config.noHandSwingV1 = val).controller(TickBoxControllerBuilder::create).build()).option(Option.<Boolean>createBuilder().name(Text.literal("No Hand Swing V2")).binding(false, () -> config.noHandSwingV2, val -> config.noHandSwingV2 = val).controller(TickBoxControllerBuilder::create).build()).option(Option.<Boolean>createBuilder().name(Text.literal("No Food Swing")).binding(false, () -> config.noFoodSwing, val -> config.noFoodSwing = val).controller(TickBoxControllerBuilder::create).build()).option(Option.<Boolean>createBuilder().name(Text.literal("No Hand Render")).binding(false, () -> config.noHandRender, val -> config.noHandRender = val).controller(TickBoxControllerBuilder::create).build()).build();
    }

    private static ConfigCategory buildEatingCategory() {
        ViewModelConfig config = getInstance();

        return ConfigCategory.createBuilder().name(Text.literal("Eating Animations")).option(Option.<Boolean>createBuilder().name(Text.literal("Eating Animation")).binding(false, () -> config.eatingAnimation, val -> config.eatingAnimation = val).controller(TickBoxControllerBuilder::create).build()).option(Option.<Float>createBuilder().name(Text.literal("Eat X")).binding(0.0f, () -> config.eatX, val -> config.eatX = val).controller(opt -> FloatSliderControllerBuilder.create(opt).range(-2.0f, 2.0f).step(0.1f)).build()).option(Option.<Float>createBuilder().name(Text.literal("Eat Y")).binding(0.0f, () -> config.eatY, val -> config.eatY = val).controller(opt -> FloatSliderControllerBuilder.create(opt).range(-2.0f, 2.0f).step(0.1f)).build()).option(Option.<Float>createBuilder().name(Text.literal("Eat Bob Amount")).binding(2.0f, () -> config.eatBobAmount, val -> config.eatBobAmount = val).controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 10.0f).step(0.5f)).build()).build();
    }
}