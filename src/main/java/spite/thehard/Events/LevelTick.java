package spite.thehard.Events;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class LevelTick {

    private static final long CHECK_INTERVAL = 200L;
    private static final float STORM_CHANCE = 0.9f;
    private static final int STORM_DURATION = 6000;
    private static boolean nightStormTriggered = false;

    public static void onNightRainTick(LevelTickEvent.Post event) {
        if (event.getLevel().getGameTime() % CHECK_INTERVAL != 0) {
            return;
        }

        Level level = event.getLevel();

        if (level.dimension() != Level.OVERWORLD) {
            return;
        }


        long dayTime = level.getDayTime() % 24000L;
        boolean isNight = dayTime >= 13000L && dayTime < 23000L;

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (isNight && !nightStormTriggered) {
            if (serverLevel.getRandom().nextFloat() < STORM_CHANCE) {
                triggerNightStorm(serverLevel);
            }
            nightStormTriggered = true;

        }
        else if (!isNight && nightStormTriggered) {
            nightStormTriggered = false;
            clearWeatherAtDawn(serverLevel);
        }
    }

    /**
     * 在服务器端触发一场雷暴
     */
    private static void triggerNightStorm(ServerLevel serverLevel) {
        LevelData worldData = serverLevel.getLevelData();
        serverLevel.setWeatherParameters(0, STORM_DURATION, true, true);

    }

    /**
     * 在黎明时强制结束恶劣天气（可选）
     */
    private static void clearWeatherAtDawn(ServerLevel serverLevel) {
        if (serverLevel.isRaining() || serverLevel.isThundering()) {
            serverLevel.setWeatherParameters(36000, 0, false, false);
        }
    }
}


