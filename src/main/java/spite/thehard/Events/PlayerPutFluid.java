package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class PlayerPutFluid {

    public static void onPlayerPutFluid(PlayerTickEvent.Pre event) {

        if (event.getEntity() instanceof Player player) {

            if (player.tickCount % 40 == 0 && player.getRandom().nextDouble() < 0.1) {
                if (player.getMainHandItem().is(Items.WATER_BUCKET)) {

                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                    BlockPos pos = player.getOnPos().above();
                    player.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 3);

                } else if (player.getOffhandItem().is(Items.WATER_BUCKET)) {

                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.BUCKET));
                    BlockPos pos = player.getOnPos();
                    player.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 3);

                }
            }
        }
    }
}
