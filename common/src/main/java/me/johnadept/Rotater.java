package me.johnadept;

import me.johnadept.config.AutoAttackConfig;
import me.johnadept.config.MessageDisplayMode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class Rotater {
    private static float currentYaw = 0f;
    private static float rotationProgress = 0f;
    private static boolean rotatingRight = true;
    public static void handleKeyPress(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null || mc.gameMode == null || mc.level == null) return;
        AutoAttackConfig config = AutoAttackConfig.get();
        if ((player.getDirection() == Direction.DOWN || player.getDirection() == Direction.UP) && config.autoAlignYaw) {
            if (!AutoAttackClient.rotationModeEnabled) {
                player.displayClientMessage(Component.translatable("gui.auto_attack.rotationModePrefix", Component.translatable("gui.auto_attack.invalidFacing").withStyle(ChatFormatting.RED)), config.displayMode == MessageDisplayMode.ACTION_BAR);
                return;
            }
        }

        AutoAttackClient.rotationModeEnabled = !AutoAttackClient.rotationModeEnabled;

        if (AutoAttackClient.rotationModeEnabled) {
            // Align initial yaw
            String suffix = null;
            if (config.autoAlignYaw) {
                switch (player.getDirection()) {
                    case NORTH ->  {
                        currentYaw = 180f + config.autoAlignYawOffset;
                        suffix = "north";
                    }
                    case SOUTH -> {
                        currentYaw = 0f + config.autoAlignYawOffset;
                        suffix = "south";
                    }
                    case EAST -> {
                        currentYaw = -90f + config.autoAlignYawOffset;
                        suffix = "east";
                    }
                    case WEST -> {
                        currentYaw = 90f + config.autoAlignYawOffset;
                        suffix = "west";
                    }
                    default -> currentYaw = player.getYRot(); // fallback
                }
                player.setYRot(currentYaw);
                player.setYHeadRot(currentYaw);
            } else {
                currentYaw = player.getYRot();
            }

            rotationProgress = 0f;
            rotatingRight = true;

            player.displayClientMessage(Component.translatable("gui.auto_attack.rotationModePrefix",
                    suffix == null ? Component.translatable("gui.auto_attack.enabled").withStyle(ChatFormatting.GREEN).append(Component.literal(", ").withStyle(ChatFormatting.WHITE)).append(Component.literal(String.valueOf(config.rotationAngle)).withStyle(ChatFormatting.AQUA)).append(Component.literal("°").withStyle(ChatFormatting.AQUA)) :
                            Component.translatable("gui.auto_attack.direction." + suffix).withStyle(ChatFormatting.GOLD).append(Component.literal(", ").withStyle(ChatFormatting.WHITE)).append(Component.literal(String.valueOf(config.rotationAngle)).withStyle(ChatFormatting.AQUA)).append(Component.literal("°").withStyle(ChatFormatting.AQUA))
                    ),
                    config.displayMode == MessageDisplayMode.ACTION_BAR
            );
        } else player.displayClientMessage(Component.translatable("gui.auto_attack.rotationModePrefix", Component.translatable("gui.auto_attack.disabled").withStyle(ChatFormatting.RED)), config.displayMode == MessageDisplayMode.ACTION_BAR);
    }

    public static void tick(Minecraft mc) {
        if (!AutoAttackClient.rotationModeEnabled) return;
        LocalPlayer player = mc.player;
        if (player == null || mc.gameMode == null || mc.level == null) return;

        AutoAttackConfig config = AutoAttackConfig.get();

        float angle = config.rotationAngle;
        float speed = config.rotationSpeed * 0.4f;

        // Update rotation direction
        if (rotatingRight) {
            rotationProgress += speed;
            if (rotationProgress >= angle) {
                rotationProgress = angle;
                rotatingRight = false;
            }
        } else {
            rotationProgress -= speed;
            if (rotationProgress <= -angle) {
                rotationProgress = -angle;
                rotatingRight = true;
            }
        }

        float targetYaw = currentYaw + rotationProgress;
//        float smoothedYaw = lerp(player.getYRot(), targetYaw, 0.2f); // smooth 20% per tick

        player.setYRot(targetYaw);
        player.setYHeadRot(targetYaw);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
