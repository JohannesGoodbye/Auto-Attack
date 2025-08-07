package me.johnadept.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import me.johnadept.ModKeyBindings;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindingsImpl {
    public static void register() {
        ModKeyBindings.toggleAttack = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.auto_attack.toggleAttack",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "key.categories.auto_attack"
                )
        );

        ModKeyBindings.toggleRotation = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.auto_attack.toggleRotation",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "key.categories.auto_attack"
                )
        );
    }
}
