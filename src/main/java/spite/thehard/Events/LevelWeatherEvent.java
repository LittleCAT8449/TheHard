package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


import java.util.function.Supplier;

public class LevelWeatherEvent {

    private static final Supplier<MobEffectInstance> POISON = () -> new MobEffectInstance(MobEffects.POISON,120, 1, true, false);
    private static final Supplier<MobEffectInstance> BLINDNESS = () -> new MobEffectInstance(MobEffects.BLINDNESS,120, 0, true, false);
    public  static void onLevelWeatherEvent(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (level.isRaining() && level.isRainingAt(player.blockPosition())) {
            player.addEffect(POISON.get());
            if (Math.random() * 1 == 0) {

                if (!level.isClientSide()) {
                    ServerLevelData levelData = (ServerLevelData) level.getLevelData();
                    levelData.setThundering(true);

                }
            }
        }
         if (level.isThundering() && level.isRainingAt(player.blockPosition())) {

            int X_Minus = (int) (player.getX() - 1);
            int X_Plus = (int) (player.getX() + 1);
            int Z_Minus = (int) (player.getZ() - 1);
            int Z_Plus = (int) (player.getZ() + 1);
            if (!player.level().isClientSide() && player.level() instanceof ServerLevel serverLevel) {
                if (level.getRandom().nextInt() < 0.5 && player.tickCount % 20 == 0) {
                    double X = Math.random() * (X_Plus - X_Minus) + (X_Plus);
                    double Z = Math.random() * (Z_Plus - Z_Minus) + (Z_Minus);
                    BlockPos pos = BlockPos.containing(X, player.getY(), Z);
                    EntityType.LIGHTNING_BOLT.spawn(serverLevel, pos, EntitySpawnReason.TRIGGERED);
                }
            }

        }
    }
}