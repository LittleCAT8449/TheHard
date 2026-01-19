package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class LavaFire {
    public  static  void PlayerHasLavaFireEvent (PlayerTickEvent.Post event){
        if(event.getEntity() instanceof Player player){
            if(player.getMainHandItem().is(Items.LAVA_BUCKET)) {
                player.setRemainingFireTicks(20);
            }
        }
    }

    public static void PlayerOnFire(PlayerTickEvent.Post event){
        if(event.getEntity() instanceof Player player)
            if(player.isOnFire()){
                BlockPos pos =player.blockPosition();
                Level level = player.level();
                if(level.getBlockState(pos).isAir()&&!level.getBlockState(pos.below()).isAir()&& !level.getBlockState(pos.below()).is(Blocks.FIRE)){
                    if (player.getRemainingFireTicks() < 20) {
                        level.setBlock(pos,Blocks.FIRE.defaultBlockState(),3);
                    }


                }
            }
    }
}
