package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Random;

public class AddMobSpawn {
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        // 1. 只在服务端且每5秒（100 tick）执行一次，避免性能负担
        if (event.getEntity().tickCount % 50 != 0) return;
        if (event.getEntity().level() instanceof ServerLevel serverLevel){

        Player player = event.getEntity();
        RandomSource random = serverLevel.getRandom();

        // 2. 条件判断：例如玩家在地下（Y<60）且亮度低于7时
        if (player.getY() < 60 && serverLevel.getMaxLocalRawBrightness(player.blockPosition()) < 7) {
            // 3. 概率触发（例如每5秒有10%几率）
            if (random.nextFloat() < 0.2f) {
                // 4. 在玩家周围10-15格范围内随机选择一个位置
                double angle = random.nextDouble() * Math.PI * 2;
                double distance = 10 + random.nextDouble() * 5;
                double dx = player.getX() + Math.cos(angle) * distance;
                double dz = player.getZ() + Math.sin(angle) * distance;
                // 寻找该位置的地表或洞穴高度
                BlockPos spawnPos = BlockPos.containing(dx, player.getY(), dz);

                // 5. 随机选择一种敌对生物生成
                EntityType<?>[] mobTypes = {
                        EntityType.ZOMBIE, EntityType.SKELETON,
                        EntityType.SPIDER, EntityType.CREEPER
                };
                EntityType<?> chosenType = mobTypes[random.nextInt(mobTypes.length)];

                // 6. 创建并生成生物
                Entity entity = chosenType.create(serverLevel, EntitySpawnReason.SPAWNER);
                if (entity != null && entity instanceof Mob mob) {
                    mob.moveOrInterpolateTo(Vec3.atLowerCornerOf(spawnPos), 0.0f,0.0f);
                    // 可选的额外设置，例如让生物直接以玩家为目标
                    mob.setTarget(player);
                    serverLevel.addFreshEntity(mob);

                }
            }
            }
        }
    }
}

