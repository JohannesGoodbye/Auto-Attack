package me.johnadept.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import me.johnadept.AutoAttackClient;
import me.johnadept.ModKeyBindings;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindingsImpl {
    public static void register() {
        ModKeyBindings.CATEGORY_AUTO_ATTACK = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(AutoAttackClient.MOD_ID, "auto_attack"));

        ModKeyBindings.toggleAttack = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.auto_attack.toggleAttack",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        ModKeyBindings.CATEGORY_AUTO_ATTACK
                )
        );

        ModKeyBindings.toggleRotation = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.auto_attack.toggleRotation",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        ModKeyBindings.CATEGORY_AUTO_ATTACK
                )
        );
    }
}
