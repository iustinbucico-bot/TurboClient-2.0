package com.yourname.client;

import com.yourname.client.modules.Module;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Чит загружен!");
        ModuleManager.init();

        ClientSendMessageEvents.SEND.register((message) -> {
            String msg = message.getMessage();
            if (msg.startsWith(".")) {
                String[] parts = msg.substring(1).split(" ");
                if (parts.length == 2 && parts[0].equalsIgnoreCase("toggle")) {
                    String name = parts[1];
                    for (Module m : ModuleManager.modules) {
                        if (m.name.equalsIgnoreCase(name)) {
                            m.toggle();
                            if (MinecraftClient.getInstance().player != null) {
                                MinecraftClient.getInstance().player.sendMessage(
                                        Text.literal("Модуль " + name + " теперь: " + (m.enabled ? "включен" : "выключен")),
                                        false
                                );
                            }
                            return false;
                        }
                    }
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("Модуль не найден"), false);
                    }
                    return false;
                }
            }
            return true;
        });
    }
}
