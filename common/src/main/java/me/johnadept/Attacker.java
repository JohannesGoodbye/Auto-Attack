package me.johnadept;

import me.johnadept.config.AutoAttackConfig;
import me.johnadept.config.MessageDisplayMode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Attacker {
    public static void tryAttack(Minecraft mc) {
        LocalPlayer player = mc.player;
        if (player == null || mc.gameMode == null) return;

        AutoAttackConfig config = AutoAttackConfig.get();
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.isDamageableItem() && config.disableOnLowDurability && mainHand.getMaxDamage() - mainHand.getDamageValue() <= config.durabilityThreshold) {
            player.displayClientMessage(Component.translatable("gui.auto_attack.autoAttackPrefix", Component.translatable("gui.auto_attack.disabledDueDurability").withStyle(ChatFormatting.RED)), config.displayMode == MessageDisplayMode.ACTION_BAR);
            AutoAttackClient.autoAttackEnabled = false;
            return;
        }

        HitResult hit = mc.hitResult;
        if (hit instanceof EntityHitResult entityHit) {
            if (!shouldAttack(entityHit.getEntity(), mainHand, player)) return;

            mc.gameMode.attack(player, entityHit.getEntity());
            player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private static boolean shouldAttack(Entity entity, ItemStack weapon, Player player) {
        AutoAttackConfig config = AutoAttackConfig.get();

        if (entity instanceof Player) return false;
        if (entity instanceof ArmorStand && !isWeapon(weapon)) return false;
        if (isShielding(player)) return false;

        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
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

    private static boolean isWeapon(ItemStack stack) {
        Item item = stack.getItem();
        return item.builtInRegistryHolder().is(ItemTags.SWORDS) || item.builtInRegistryHolder().is(ItemTags.AXES);
    }
}
