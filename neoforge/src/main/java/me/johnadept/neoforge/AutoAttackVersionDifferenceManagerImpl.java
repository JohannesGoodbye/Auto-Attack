package me.johnadept.neoforge;

import net.minecraft.client.gui.components.EditBox;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class AutoAttackVersionDifferenceManagerImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
    public static String getSuggestion(EditBox editBox) {
        return editBox.suggestion;
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static String getModJarFileName(String modId) {
        return ModList.get().getModFileById(modId)
                .getFile().getFileName();
    }
}
