package me.johnadept;

import me.johnadept.config.AutoAttackConfig;
import me.johnadept.config.MessageDisplayMode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ModEventHandlers {
    public static void onClientTick() {
        AutoAttackConfig config = AutoAttackConfig.get();
        if (!config.enableMod) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (ModKeyBindings.toggleAttack.consumeClick()) {
            AutoAttackClient.autoAttackEnabled = !AutoAttackClient.autoAttackEnabled;
            mc.player.displayClientMessage(Component.translatable("gui.auto_attack.autoAttackPrefix", AutoAttackClient.autoAttackEnabled ? Component.translatable("gui.auto_attack.enabled").withStyle(ChatFormatting.GREEN) : Component.translatable("gui.auto_attack.disabled").withStyle(ChatFormatting.RED)), config.displayMode == MessageDisplayMode.ACTION_BAR);
        }
        if (ModKeyBindings.toggleRotation.consumeClick()) {
            Rotater.handleKeyPress(mc);
        }
        Rotater.tick(mc);

        if (mc.player.isSpectator() && AutoAttackClient.autoAttackEnabled) {
            mc.player.displayClientMessage(Component.translatable("gui.auto_attack.autoAttackPrefix", Component.translatable("gui.auto_attack.disabledDueSpectator").withStyle(ChatFormatting.RED)), config.displayMode == MessageDisplayMode.ACTION_BAR);
            AutoAttackClient.autoAttackEnabled = false;
        }

        if (AutoAttackClient.autoAttackEnabled && mc.player.getAttackStrengthScale(0) == 1.0f) {
            Attacker.tryAttack(mc);
        }
    }
}
