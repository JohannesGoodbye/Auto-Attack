package me.johnadept;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.components.EditBox;

import java.nio.file.Path;

public class AutoAttackVersionDifferenceManager {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String getSuggestion(EditBox editBox) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String getModJarFileName(String modId) {
        throw new AssertionError();
    }
}
