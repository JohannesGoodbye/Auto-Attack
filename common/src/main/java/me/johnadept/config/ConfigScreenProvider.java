package me.johnadept.config;

import net.minecraft.client.gui.screens.Screen;

public interface ConfigScreenProvider {
    Screen create(Screen parent);
}
