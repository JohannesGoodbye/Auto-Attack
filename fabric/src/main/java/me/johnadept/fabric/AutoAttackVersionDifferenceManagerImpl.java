package me.johnadept.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.components.EditBox;

import java.io.File;
import java.nio.file.Path;

public class AutoAttackVersionDifferenceManagerImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
    public static String getSuggestion(EditBox editBox) {
        return editBox.suggestion;
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static String getModJarFileName(String modId) {
        return FabricLoader.getInstance()
                .getModContainer(modId)
                .map(ModContainer::getOrigin)
                .map(origin -> new File(origin.getPaths().get(0).toUri()).getName())
                .orElse("");
    }
}
