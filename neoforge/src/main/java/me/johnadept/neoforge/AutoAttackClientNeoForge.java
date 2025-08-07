package me.johnadept.neoforge;

import me.johnadept.AutoAttackClient;
import me.johnadept.ModEventHandlers;
import me.johnadept.config.ConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = AutoAttackClient.MOD_ID, dist = Dist.CLIENT)
public final class AutoAttackClientNeoForge {
    public AutoAttackClientNeoForge() {
        AutoAttackClient.init();

        if (ModList.get().isLoaded("cloth_config")) {
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (container, parent) -> {
                return ConfigScreen.create(parent);
            });
        }
    }

    @EventBusSubscriber(modid = AutoAttackClient.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            ModEventHandlers.onClientTick();
        }
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeyBindingsImpl.register(event);
        }
    }
}
