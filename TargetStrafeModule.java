package com.yourname.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class TargetStrafeModule extends Module {
    private Entity target = null;
    private static final double CIRCLE_RADIUS = 3.0;
    private float angle = 0;

    public TargetStrafeModule() {
        super("TargetStrafe");
    }

    @Override
    public void onTick() {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        if (target == null || !((LivingEntity) target).isAlive() || target.squaredDistanceTo(client.player) > 50) {
            target = findTarget(client);
        }

        if (target != null) {
            angle += 0.05f;
            Vec3d targetPos = target.getPos();
            Vec3d playerPos = client.player.getPos();

            double x = targetPos.x + CIRCLE_RADIUS * Math.cos(angle);
            double z = targetPos.z + CIRCLE_RADIUS * Math.sin(angle);
            Vec3d moveVec = new Vec3d(x - playerPos.x, 0, z - playerPos.z).normalize();

            double speed = 0.2;
            Vec3d velocity = client.player.getVelocity();
            client.player.setVelocity(moveVec.x * speed, velocity.y, moveVec.z * speed);

            Vec3d toTarget = targetPos.subtract(playerPos);
            double yaw = Math.toDegrees(Math.atan2(-toTarget.x, toTarget.z));
            double pitch = Math.toDegrees(Math.asin(-toTarget.y / toTarget.length()));
            client.player.setYaw((float) yaw);
            client.player.setPitch((float) pitch);
        }
    }

    private Entity findTarget(MinecraftClient client) {
        Vec3d pos = client.player.getPos();
        double minDist = 10.0;
        Entity result = null;
        List<Entity> entities = client.world.getEntitiesByClass(PlayerEntity.class,
                Box.of(pos, 10, 10, 10),
                e -> e != client.player && ((LivingEntity) e).isAlive() && !((PlayerEntity) e).isCreative());
        for (Entity e : entities) {
            double dist = e.squaredDistanceTo(pos);
            if (dist < minDist * minDist) {
                minDist = Math.sqrt(dist);
                result = e;
            }
        }
        return result;
    }
    }
