package me.johnadept;

import me.johnadept.config.AutoAttackConfig;
import me.johnadept.config.MessageDisplayMode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Attacker {
    private static final RandomSource RANDOM = RandomSource.create();

    /** Ticks left before the next hit is allowed, or -1 when no delay is pending. */
    private static int hitDelayTicks = -1;

    public static void tick(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null || mc.gameMode == null) return;

        if (player.getAttackStrengthScale(0) < 1.0f) {
            // Cooldown is still running, roll a fresh delay once it is full again
            resetHitDelay();
            return;
        }

        AutoAttackConfig config = AutoAttackConfig.get();
        if (config.enableHitDelay) {
            if (hitDelayTicks < 0) {
                hitDelayTicks = rollHitDelay(config);
            }
            if (hitDelayTicks > 0) {
                hitDelayTicks--;
                return;
            }
        }

        if (tryAttack(mc)) resetHitDelay();
    }

    public static void resetHitDelay() {
        hitDelayTicks = -1;
    }

    private static int rollHitDelay(AutoAttackConfig config) {
        int min = Math.clamp(config.minHitDelay, 0, config.maxHitDelay);
        int max = Math.max(0, Math.max(config.minHitDelay, config.maxHitDelay));
        return min == max ? min : min + RANDOM.nextInt(max - min + 1);
    }

    private static boolean tryAttack(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null || mc.gameMode == null) return false;

        AutoAttackConfig config = AutoAttackConfig.get();
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.isDamageableItem() && config.disableOnLowDurability && mainHand.getMaxDamage() - mainHand.getDamageValue() <= config.durabilityThreshold) {
            player.displayClientMessage(Component.translatable("gui.auto_attack.autoAttackPrefix", Component.translatable("gui.auto_attack.disabledDueDurability").withStyle(ChatFormatting.RED)), config.displayMode == MessageDisplayMode.ACTION_BAR);
            AutoAttackClient.autoAttackEnabled = false;
            return false;
        }

        HitResult hit = mc.hitResult;
        if (hit instanceof EntityHitResult entityHit) {
            if (!shouldAttack(entityHit.getEntity(), mainHand, player)) return false;

            mc.gameMode.attack(player, entityHit.getEntity());
            player.swing(InteractionHand.MAIN_HAND);
            return true;
        }
        return false;
    }

    private static boolean shouldAttack(Entity entity, ItemStack weapon, Player player) {
        AutoAttackConfig config = AutoAttackConfig.get();

        if (entity instanceof Player) return false;
        if (isShielding(player)) return false;

        Identifier id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (config.entityBlacklist.contains(id.toString())) return false;
        if (config.entityWhitelist.contains(id.toString())) return true;

        if (!config.attackNonLiving && !(entity instanceof LivingEntity)) return false;
        if (config.protectTamedMobs && entity instanceof TamableAnimal tamableAnimal && tamableAnimal.isTame()) return false;
        if (!config.attackNonHostile && !(entity instanceof Monster)) return false;

        return true;
    }

    private static boolean isShielding(Player player) {
        return player.isUsingItem() && player.getUseItem().getItem() instanceof ShieldItem;
    }
}
