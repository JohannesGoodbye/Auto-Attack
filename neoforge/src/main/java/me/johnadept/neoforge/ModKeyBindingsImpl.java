package me.johnadept.neoforge;

import me.johnadept.AutoAttackClient;
import me.johnadept.ModKeyBindings;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindingsImpl {
    public static void register(RegisterKeyMappingsEvent event) {
        ModKeyBindings.CATEGORY_AUTO_ATTACK = new KeyMapping.Category(ResourceLocation.fromNamespaceAndPath(AutoAttackClient.MOD_ID, "auto_attack"));

        ModKeyBindings.toggleAttack = new KeyMapping(
                "key.auto_attack.toggleAttack",
                GLFW.GLFW_KEY_UNKNOWN,
                ModKeyBindings.CATEGORY_AUTO_ATTACK
        );
        ModKeyBindings.toggleRotation = new KeyMapping(
                "key.auto_attack.toggleRotation",
                GLFW.GLFW_KEY_UNKNOWN,
                ModKeyBindings.CATEGORY_AUTO_ATTACK
        );

        event.register(ModKeyBindings.toggleAttack);
        event.register(ModKeyBindings.toggleRotation);
    }
}
