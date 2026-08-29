package com.yourname.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class SprintModule extends Module {
    public SprintModule() {
        super("Sprint");
    }
    @Override
    public void onTick() {
        if (enabled) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null && player.forwardSpeed > 0 && !player.isSprinting()) {
                player.setSprinting(true);
            }
        }
    }
}
