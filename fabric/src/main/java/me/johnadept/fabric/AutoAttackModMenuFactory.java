package me.johnadept.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.johnadept.config.ConfigScreen;
import net.fabricmc.loader.api.FabricLoader;

public class AutoAttackModMenuFactory implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return ConfigScreen::create;
        }
        return null;
    }
}
