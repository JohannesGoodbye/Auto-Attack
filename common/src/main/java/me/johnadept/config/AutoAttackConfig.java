package me.johnadept.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.johnadept.AutoAttackClient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.io.*;
import java.nio.file.*;
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

    private static boolean initialLoadDone;

    // Watcher management
    private static WatchService watchService;
    private static Thread watcherThread;
    private static boolean savingInternally;

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

    public static void loadInitially() {
        if (configPath == null) throw new IllegalStateException("Config path not set");
        if (initialLoadDone) {
            throw new IllegalStateException(
                    "loadInitially() has already been called — this method must only be invoked once."
            );
        }
        load();
        startWatching();
        initialLoadDone = true;
    }

    public static void save() {
        if (configPath == null) throw new IllegalStateException("Config path not set");
        savingInternally = true;
        try (Writer writer = new FileWriter(configPath.toFile())) {
            GSON.toJson(get(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            savingInternally = false;
        }
    }


    private static void startWatching() {
        stopWatching(); // Ensure old watcher is stopped first

        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path dir = configPath.getParent();
            dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            watcherThread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            Path changed = dir.resolve((Path) event.context());
                            if (!savingInternally && changed.getFileName().equals(configPath.getFileName())) {
                                AutoAttackClient.LOGGER.info("Detected config file change. Reloading...");
                                load();
                            }
                        }
                        key.reset();
                    }
                } catch (InterruptedException ignored) {
                    // Thread stopped
                }
            }, "AutoAttackConfig-FileWatcher");

            watcherThread.setDaemon(true); // Will not block game exit
            watcherThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void stopWatching() {
        if (watcherThread != null && watcherThread.isAlive()) {
            watcherThread.interrupt();
            watcherThread = null;
        }
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException ignored) {}
            watchService = null;
        }
    }
}
