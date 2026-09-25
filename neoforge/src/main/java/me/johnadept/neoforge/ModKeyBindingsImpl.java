package me.johnadept.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import me.johnadept.AutoAttackClient;
import me.johnadept.ModKeyBindings;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class ModKeyBindingsImpl {
    public static void register(RegisterKeyMappingsEvent event) {
        ModKeyBindings.CATEGORY_AUTO_ATTACK = new KeyMapping.Category(Identifier.fromNamespaceAndPath(AutoAttackClient.MOD_ID, "auto_attack"));

        ModKeyBindings.toggleAttack = new KeyMapping(
                "key.auto_attack.toggleAttack",
                InputConstants.Type.KEYBOARD,
                InputConstants.UNKNOWN.getValue(),
                ModKeyBindings.CATEGORY_AUTO_ATTACK
        );
        ModKeyBindings.toggleRotation = new KeyMapping(
                "key.auto_attack.toggleRotation",
                InputConstants.Type.KEYBOARD,
                InputConstants.UNKNOWN.getValue(),
                ModKeyBindings.CATEGORY_AUTO_ATTACK
        );

        event.register(ModKeyBindings.toggleAttack);
        event.register(ModKeyBindings.toggleRotation);
    }
}
