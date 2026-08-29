package com.yourname.client;

import com.yourname.client.modules.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.add(new FullbrightModule());
        modules.add(new SprintModule());
        modules.add(new KillauraModule());
        modules.add(new TargetStrafeModule());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Module m : modules) {
                if (m.enabled) m.onTick();
            }
        });
    }

    public static List<Module> getModules() {
        return modules;
    }
}
