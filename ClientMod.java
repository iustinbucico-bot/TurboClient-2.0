package com.yourname.client;

import com.yourname.client.gui.ClickGUI;
import com.yourname.client.modules.Module;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClientMod implements ClientModInitializer {
    private static boolean wasShiftPressed = false;

    @Override
    public void onInitializeClient() {
        System.out.println("Чит загружен!");
        ModuleManager.init();

        // Команды в чате
        ClientSendMessageEvents.SEND.register((message) -> {
            String msg = message.getMessage();
            if (msg.startsWith(".")) {
                String[] parts = msg.substring(1).split(" ");
                if (parts.length == 2 && parts[0].equalsIgnoreCase("toggle")) {
                    String name = parts[1];
                    for (Module m : ModuleManager.getModules()) {
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

        // Открытие GUI по правому Shift
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean shiftPressed = GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (shiftPressed && !wasShiftPressed) {
                wasShiftPressed = true;
                if (!(client.currentScreen instanceof ClickGUI)) {
                    client.setScreen(new ClickGUI());
                }
            } else if (!shiftPressed) {
                wasShiftPressed = false;
            }
        });
    }
}
