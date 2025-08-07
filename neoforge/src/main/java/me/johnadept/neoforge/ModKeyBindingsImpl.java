package me.johnadept.neoforge;

import me.johnadept.ModKeyBindings;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindingsImpl {
    public static void register(RegisterKeyMappingsEvent event) {
        ModKeyBindings.toggleAttack = new KeyMapping(
                "key.auto_attack.toggleAttack",
                GLFW.GLFW_KEY_UNKNOWN,
                "key.categories.auto_attack"
        );
        ModKeyBindings.toggleRotation = new KeyMapping(
                "key.auto_attack.toggleRotation",
                GLFW.GLFW_KEY_UNKNOWN,
                "key.categories.auto_attack"
        );

        event.register(ModKeyBindings.toggleAttack);
        event.register(ModKeyBindings.toggleRotation);
    }
}
