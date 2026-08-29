package com.yourname.client.modules;

import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {
    public FullbrightModule() {
        super("Fullbright");
    }
    @Override
    public void onEnable() {
        MinecraftClient.getInstance().options.getGamma().setValue(100.0);
    }
    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getGamma().setValue(0.5);
    }
}
