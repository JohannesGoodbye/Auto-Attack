package me.johnadept.config;

import net.minecraft.network.chat.Component;

public enum MessageDisplayMode {
    CHAT,
    ACTION_BAR;

    public Component getDisplayName() {
        return Component.translatable("menu.auto_attack.display_mode." + name().toLowerCase());
    }
}
