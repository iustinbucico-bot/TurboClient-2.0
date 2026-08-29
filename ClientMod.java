package com.yourname.client;

import com.yourname.client.modules.Module;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Чит загружен!");
        ModuleManager.init();

        // Обработка команд в чате
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

        // Обработка нажатия правого Shift (вывод списка модулей)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            // Проверяем, зажат ли правый Shift
            if (GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS) {
                // Чтобы не спамить, выводим только один раз при нажатии (используем флаг)
                // Но для простоты будем выводить каждый тик, пока зажат, но это не критично.
                // Лучше сделать однократное срабатывание:
                if (!wasShiftPressed) {
                    wasShiftPressed = true;
                    client.player.sendMessage(Text.literal("--- Список модулей ---"), false);
                    for (Module m : ModuleManager.modules) {
                        String status = m.enabled ? "§aВключен" : "§cВыключен";
                        client.player.sendMessage(Text.literal(m.name + ": " + status), false);
                    }
                }
            } else {
                wasShiftPressed = false;
            }
        });
    }

    private static boolean wasShiftPressed = false;
}
