package me.johnadept.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
//TODO: Failsafe
public class AutoAttackConfig {
    public boolean enableMod = true;
    public boolean disableOnLowDurability = true;
    public int durabilityThreshold = 10;
    public boolean attackNonHostile = false;
    public boolean protectTamedMobs = true;
    public boolean attackNonLiving = false;
    public int rotationAngle = 30;
    public int rotationSpeed = 3;
    public boolean autoAlignYaw = true;
    public float autoAlignYawOffset = 0;
    public MessageDisplayMode displayMode = MessageDisplayMode.ACTION_BAR;
    public List<String> entityWhitelist = new ArrayList<>();
    public List<String> entityBlacklist = List.of(
            BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ITEM_FRAME).toString()
    );

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static AutoAttackConfig INSTANCE;

    private static Path configPath;

    public static void setConfigPath(Path path) {
        configPath = path;
    }

    public static AutoAttackConfig get() {
        if (INSTANCE == null) {
            load();
        }
        return INSTANCE;
    }

    public static void load() {
        if (configPath == null) throw new IllegalStateException("Config path not set");

        File file = configPath.toFile();
        if (!file.exists()) {
            INSTANCE = new AutoAttackConfig();
            save();
        } else {
            try (Reader reader = new FileReader(file)) {
                INSTANCE = GSON.fromJson(reader, AutoAttackConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                INSTANCE = new AutoAttackConfig(); // fallback
            }
        }
    }

    public static void save() {
        if (configPath == null) throw new IllegalStateException("Config path not set");

        try (Writer writer = new FileWriter(configPath.toFile())) {
            GSON.toJson(get(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
