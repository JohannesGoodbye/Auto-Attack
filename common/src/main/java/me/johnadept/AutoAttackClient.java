package me.johnadept;

import me.johnadept.config.AutoAttackConfig;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class AutoAttackClient {
    public static final String MOD_ID = "auto_attack";

    public static boolean autoAttackEnabled = false;
    public static boolean rotationModeEnabled = false;

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    public static final Function<String, Optional<Component>> ENTITY_ID_BLACKLIST_VALIDATOR = str -> {
        if (str == null || str.trim().isEmpty()) {
            return Optional.of(Component.translatable("menu.auto_attack.config.entity_id.empty"));
        }
        try {
            ResourceLocation id = ResourceLocation.tryParse(str);
            if (id == null) {
                return Optional.of(Component.translatable("menu.auto_attack.config.entity_id.invalid_format"));
            }
            if (!BuiltInRegistries.ENTITY_TYPE.containsKey(id)) {
                return Optional.of(Component.translatable("menu.auto_attack.config.entity_id.not_found", str));
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(Component.translatable("menu.auto_attack.config.entity_id.invalid_format"));
        }
    };

    public static final Function<String, Optional<Component>> ENTITY_ID_WHITELIST_VALIDATOR = str -> {
        Optional<Component> baseValidation = ENTITY_ID_BLACKLIST_VALIDATOR.apply(str);
        if (baseValidation.isPresent()) {
            return baseValidation;
        }
        ResourceLocation id = ResourceLocation.tryParse(str);
        if (id.equals(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PLAYER))) {
            return Optional.of(Component.translatable("menu.auto_attack.config.entity_id.disallowed_player"));
        }

        return Optional.empty();
    };

    public static boolean handleTabLogic(EditBox widget) {
        String currentText = widget.getValue();
        String suggestion = AutoAttackVersionDifferenceManager.getSuggestion(widget);

        if (suggestion != null && !suggestion.isEmpty()) {
            // Case 1: user hasn't typed the modid yet → autocomplete modid:
            if (!currentText.contains(":") && suggestion.indexOf(':') > 0) {
                int colonIndex = (currentText+suggestion).indexOf(':');
                if (colonIndex != -1) {
                    String modIdPart = (currentText+suggestion).substring(0, colonIndex + 1);
                    widget.setValue(modIdPart);
                    return true; // prevent tab navigation
                }
            }

            // Case 2: user already typed modid → autocomplete full ID
            else {
                widget.setValue(currentText + suggestion);
                widget.setSuggestion(null);
                return true; // prevent tab navigation
            }
        }
        return false;
    }

    public static boolean handleShiftTabLogic(EditBox widget) {
        String currentText = widget.getValue();

        if (currentText.isEmpty()) return false;
        if (!currentText.contains(":") || (currentText.chars().filter(ch -> ch == ':').count() == 1 && currentText.indexOf(':') == currentText.length() - 1)) {
            widget.setValue("");
            return true;
        } else if ((currentText.chars().filter(ch -> ch == ':').count() >= 1 || currentText.lastIndexOf(':') < currentText.length() - 1) && currentText.indexOf(':') != -1) {
            widget.setValue(currentText.substring(0, currentText.indexOf(':') + 1));
            return true;
        }
        return false;
    }

    private static final Pattern PARTIAL_RESOURCE_LOCATION = Pattern.compile("^[a-z0-9._-]*(:[a-z0-9._/-]*)?$");
    public static boolean isValidResourceLocationText(@NotNull String text) {
        return PARTIAL_RESOURCE_LOCATION.matcher(text).matches();
    }

    public static void init() {
        AutoAttackConfig.setConfigPath(AutoAttackVersionDifferenceManager.getConfigDirectory().resolve("auto_attack.json"));
        AutoAttackConfig.loadInitially();

        if (AutoAttackVersionDifferenceManager.isModLoaded("auto-attack")) {
            String fileName = AutoAttackVersionDifferenceManager.getModJarFileName("auto-attack");
            String fileInfo = fileName.isEmpty() ? ")" : ", file: '" + fileName + "')";

            LOGGER.warn("The old version of AutoAttack (mod ID: 'auto-attack'" + fileInfo + " is installed. Please remove it to avoid conflicts.");
        }
    }
}
