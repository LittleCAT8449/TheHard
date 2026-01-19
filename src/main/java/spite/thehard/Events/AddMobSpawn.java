package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class AddMobSpawn {
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().tickCount % 50 != 0) return;
        if (event.getEntity().level() instanceof ServerLevel serverLevel){

        Player player = event.getEntity();
        RandomSource random = serverLevel.getRandom();

        if (player.getY() < 60 && serverLevel.getMaxLocalRawBrightness(player.blockPosition()) < 7) {
            if (random.nextFloat() < 0.2f) {
                double angle = random.nextDouble() * Math.PI * 2;
                double distance = 10 + random.nextDouble() * 5;
                double dx = player.getX() + Math.cos(angle) * distance;
                double dz = player.getZ() + Math.sin(angle) * distance;
                BlockPos spawnPos = BlockPos.containing(dx, player.getY(), dz);

                EntityType<?>[] mobTypes = {
                        EntityType.ZOMBIE, EntityType.SKELETON,
                        EntityType.SPIDER, EntityType.CREEPER
                };
                EntityType<?> chosenType = mobTypes[random.nextInt(mobTypes.length)];

                Entity entity = chosenType.create(serverLevel, EntitySpawnReason.SPAWNER);
                if (entity != null && entity instanceof Mob mob) {
                    mob.moveOrInterpolateTo(Vec3.atLowerCornerOf(spawnPos), 0.0f,0.0f);
                    mob.setTarget(player);
                    serverLevel.addFreshEntity(mob);

                }
            }
            }
        }
    }
}

