package spite.thehard.Events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;

public class PlayerSleepEvent {
    public static void onPlayerSleep(CanPlayerSleepEvent event) {
        event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
    }
}
