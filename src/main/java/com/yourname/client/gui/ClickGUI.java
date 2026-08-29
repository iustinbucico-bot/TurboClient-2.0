package com.yourname.client.gui;

import com.yourname.client.ModuleManager;
import com.yourname.client.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGUI extends Screen {
    public ClickGUI() {
        super(Text.literal("Чит-меню"));
    }

    @Override
    protected void init() {
        super.init();
        int y = 20;
        for (Module module : ModuleManager.getModules()) {
            ButtonWidget button = ButtonWidget.builder(
                    Text.literal(module.name + ": " + (module.enabled ? "ON" : "OFF")),
                    b -> {
                        module.toggle();
                        b.setMessage(Text.literal(module.name + ": " + (module.enabled ? "ON" : "OFF")));
                    }
            ).dimensions(this.width / 2 - 50, y, 100, 20).build();
            this.addDrawableChild(button);
            y += 25;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawText(textRenderer, "Чит-меню", this.width / 2 - 30, 5, 0xFFFFFF, true);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
