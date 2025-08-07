package me.johnadept.fabric;

import me.johnadept.AutoAttackClient;
import me.johnadept.ModEventHandlers;
import me.johnadept.config.AutoAttackConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;

public final class AutoAttackFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoAttackClient.init();
        ModKeyBindingsImpl.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> ModEventHandlers.onClientTick());
    }
}
