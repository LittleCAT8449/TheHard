package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;


public class PlayerBreakBlock {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<Block> EXPLOSIVE_BLOCKS = Set.of(
            Blocks.DIAMOND_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.IRON_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.GOLD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.COAL_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.LAPIS_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.EMERALD_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.NETHER_QUARTZ_ORE,
            Blocks.ANCIENT_DEBRIS,
            Blocks.NETHER_GOLD_ORE,
            Blocks.COPPER_ORE,
            Blocks.DEEPSLATE_COPPER_ORE

    );
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event) {

        if(event.getState().getBlock().defaultBlockState().is(BlockTags.LOGS)){

            if(event.getPlayer().getMainHandItem().is(ItemStack.EMPTY.getItem())){
                event.getPlayer().hurt(event.getPlayer().damageSources().stalagmite(), 2.0f);
            }

        }

    }



    public static void onPlayerBreakSpawnSilverFish(BlockEvent.BreakEvent event) {

        if(event.getState().getBlock().defaultBlockState().is(Blocks.STONE)||event.getState().getBlock().defaultBlockState().is(Blocks.DEEPSLATE)){
            if(event.getLevel() instanceof ServerLevel serverLevel&&event.getPlayer().getRandom().nextDouble()<0.3){

                Silverfish silverfish = EntityType.SILVERFISH.create(serverLevel, EntitySpawnReason.SPAWNER);
                silverfish.moveOrInterpolateTo(
                        Vec3.atLowerCornerOf(event.getPos()),
                        0.0f,
                        0.0f

                );
                serverLevel.addFreshEntity(silverfish);

            }


        }
    }
    public static void onBlockSummonTnt(BlockEvent.BreakEvent event) {
        if(EXPLOSIVE_BLOCKS.contains(event.getState().getBlock())) {
            if (event.getLevel() instanceof ServerLevel serverLevel && event.getPlayer().getRandom().nextDouble() < 0.3) {
                BlockPos brokenPos = event.getPos();
                spawnTntAtPosition(serverLevel, brokenPos,10);
                spawnTntAtPosition(serverLevel, brokenPos,20);
                spawnTntAtPosition(serverLevel, brokenPos,30);


            }
        }
    }
    private static void spawnTntAtPosition(ServerLevel world, BlockPos centerPos,int fuse) {
        double offsetX = (world.getRandom().nextDouble() - 0.5) * 2 ;
        double offsetY = world.getRandom().nextDouble() * 1.5;
        double offsetZ = (world.getRandom().nextDouble() - 0.5) * 2 ;

        double spawnX = centerPos.getX() + 0.5 + offsetX;
        double spawnY = centerPos.getY() + offsetY;
        double spawnZ = centerPos.getZ() + 0.5 + offsetZ;
        Vec3 pos =new Vec3(spawnX, spawnY, spawnZ);

        PrimedTnt tnt = net.minecraft.world.entity.EntityType.TNT.create(world,EntitySpawnReason.SPAWNER);
        if (tnt != null) {

            tnt.moveOrInterpolateTo(pos, world.getRandom().nextFloat() * 360.0F, 0.0F);
            tnt.setFuse(fuse);
            tnt.setDeltaMovement(
                    (world.getRandom().nextDouble() - 0.5) * 0.2,
                    world.getRandom().nextDouble() * 0.2,
                    (world.getRandom().nextDouble() - 0.5) * 0.2
            );
            world.addFreshEntity(tnt);
        }
    }
}


