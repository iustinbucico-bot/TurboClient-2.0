package com.yourname.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class KillauraModule extends Module {
    private int cooldown = 0;
    private static final int ATTACK_COOLDOWN = 4;

    public KillauraModule() {
        super("Killaura");
    }

    @Override
    public void onTick() {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        Entity target = null;
        double minDist = 5.0;
        Vec3d pos = client.player.getPos();
        List<Entity> entities = client.world.getEntitiesByClass(PlayerEntity.class,
                Box.of(pos, 10, 10, 10),
                e -> e != client.player && ((LivingEntity) e).isAlive() && !((PlayerEntity) e).isCreative());

        for (Entity e : entities) {
            double dist = e.squaredDistanceTo(pos);
            if (dist < minDist * minDist) {
                minDist = Math.sqrt(dist);
                target = e;
            }
        }

        if (target != null) {
            client.interactionManager.attackEntity(client.player, target);
            client.player.swingHand(Hand.MAIN_HAND);
            cooldown = ATTACK_COOLDOWN;
        }
    }
}
