package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingEvent;


public class PlayerJump {

    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player) || player.level().isClientSide()) {
            return;
        }

        BlockState headBlock = getPlayerHeadBlock(player);
        if (!headBlock.isAir()) {
            player.hurt(player.level().damageSources().magic(), 2.0f);
        }
    }

    private static BlockState getPlayerHeadBlock(Player player) {
        Level level = player.level();
        BlockPos playerPos = player.blockPosition();
        BlockPos headPos = playerPos.above(2);
        return level.getBlockState(headPos);
    }

}
