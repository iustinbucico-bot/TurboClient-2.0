package com.yourname.client.modules;

public class Module {
    public String name;
    public boolean enabled;

    public Module(String name) {
        this.name = name;
        this.enabled = false;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}
