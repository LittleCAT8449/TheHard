package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;


public class PlayerLavaFire {

    public static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    public static void onPlayerLavaFire(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) return;

            int X_Minus = (int) (player.getX() - 1);
            int X_Plus = (int) (player.getX() + 1);
            int Z_Minus = (int) (player.getZ() - 1);
            int Z_Plus = (int) (player.getZ() + 1);

            boolean nearLava = false;
            if (player.level().getBlockState(new BlockPos(X_Minus, player.getBlockY()-1, player.getBlockZ())).is(Blocks.LAVA)
                    || player.level().getBlockState(new BlockPos(X_Plus, player.getBlockY()-1, player.getBlockZ())).is(Blocks.LAVA)
                    || player.level().getBlockState(new BlockPos(player.getBlockX(), player.getBlockY()-1, Z_Minus)).is(Blocks.LAVA)
                    || player.level().getBlockState(new BlockPos(player.getBlockX(), player.getBlockY()-1, Z_Plus)).is(Blocks.LAVA)) {
                LOGGER.info("Player is near lava!");
                nearLava = true;
            }

            if (nearLava) {
                if (player.getRemainingFireTicks() < 80) {
                    LOGGER.info("Setting player fire ticks!");
                    player.setRemainingFireTicks(20);


                }
            }
        }
    }

}
