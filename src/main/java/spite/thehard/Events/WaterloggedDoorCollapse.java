package spite.thehard.Events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;

public class WaterloggedDoorCollapse {


    private static final int CHECK_INTERVAL = 5;
    private static final double COLLISION_BOX_EXPAND = 0.3;


    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().tickCount % CHECK_INTERVAL != 0) {
            return;
        }


        if (!(event.getEntity().level() instanceof ServerLevel serverLevel)) {
            return;
        }


        AABB playerBox = event.getEntity().getBoundingBox().inflate(COLLISION_BOX_EXPAND);


        int minX = (int) Math.floor(playerBox.minX);
        int minY = (int) Math.floor(playerBox.minY);
        int minZ = (int) Math.floor(playerBox.minZ);
        int maxX = (int) Math.floor(playerBox.maxX);
        int maxY = (int) Math.floor(playerBox.maxY);
        int maxZ = (int) Math.floor(playerBox.maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if(event.getEntity() instanceof Player player) {
                        checkAndBreakWaterloggedDoor(serverLevel, pos, player);
                    }
                }
            }
        }
    }


    private static void checkAndBreakWaterloggedDoor(ServerLevel level, BlockPos pos, Player player) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();


        boolean isWoodenDoor =
                block == Blocks.OAK_DOOR
                        || block == Blocks.SPRUCE_DOOR
                        || block == Blocks.BIRCH_DOOR
                        || block == Blocks.JUNGLE_DOOR
                        || block == Blocks.ACACIA_DOOR
                        || block == Blocks.DARK_OAK_DOOR
                        || block == Blocks.MANGROVE_DOOR
                        || block == Blocks.CHERRY_DOOR
                        || block == Blocks.BAMBOO_DOOR
                        || block == Blocks.CRIMSON_DOOR
                        || block == Blocks.WARPED_DOOR;

        if (!isWoodenDoor) {
            return;
        }

        boolean isInWater = isDoorSurroundedByWater(level, pos, state);

        if (isInWater) {
            breakDoorAndDrop(level, pos, state, block, player);
        }
    }


    private static boolean isDoorSurroundedByWater(ServerLevel level, BlockPos pos, BlockState state) {

        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            FluidState adjacentFluid = level.getFluidState(adjacentPos);


            if (adjacentFluid.getType() == Fluids.WATER ||
                    adjacentFluid.getType() == Fluids.FLOWING_WATER) {

                if (adjacentFluid.getAmount() >= 8) {
                    return true;
                }
            }


            if (level.getBlockState(adjacentPos).getBlock() == Blocks.WATER) {
                return true;
            }
        }


        if (state.hasProperty(DoorBlock.HALF)) {
            BlockPos otherHalfPos = state.getValue(DoorBlock.HALF) ==
                    net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER
                    ? pos.above()
                    : pos.below();

            for (Direction direction : Direction.values()) {
                BlockPos adjacentPos = otherHalfPos.relative(direction);
                FluidState adjacentFluid = level.getFluidState(adjacentPos);

                if (adjacentFluid.getType() == Fluids.WATER ||
                        adjacentFluid.getType() == Fluids.FLOWING_WATER) {
                    if (adjacentFluid.getAmount() >= 8) {
                        return true;
                    }
                }

                if (level.getBlockState(adjacentPos).getBlock() == Blocks.WATER) {
                    return true;
                }
            }
        }

        return false;
    }


    private static void breakDoorAndDrop(ServerLevel level, BlockPos pos, BlockState state, Block doorBlock, Player player) {

        level.playSound(null, pos,
                net.minecraft.sounds.SoundEvents.WOOD_BREAK,
                net.minecraft.sounds.SoundSource.BLOCKS,
                0.8F, 0.9F + level.getRandom().nextFloat() * 0.2F);

        ItemStack doorItem = getDoorItem(doorBlock);

        if (state.hasProperty(DoorBlock.HALF)) {
            BlockPos otherHalfPos = state.getValue(DoorBlock.HALF) ==
                    net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER
                    ? pos.above()
                    : pos.below();


            level.destroyBlock(otherHalfPos, false, player);
        }


        level.destroyBlock(pos, false, player);


        ItemEntity itemEntity = new ItemEntity(level,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                doorItem);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }


    private static ItemStack getDoorItem(Block doorBlock) {
        if (doorBlock == Blocks.OAK_DOOR) {
            return new ItemStack(Items.OAK_DOOR);
        } else if (doorBlock == Blocks.SPRUCE_DOOR) {
            return new ItemStack(Items.SPRUCE_DOOR);
        } else if (doorBlock == Blocks.BIRCH_DOOR) {
            return new ItemStack(Items.BIRCH_DOOR);
        } else if (doorBlock == Blocks.JUNGLE_DOOR) {
            return new ItemStack(Items.JUNGLE_DOOR);
        } else if (doorBlock == Blocks.ACACIA_DOOR) {
            return new ItemStack(Items.ACACIA_DOOR);
        } else if (doorBlock == Blocks.DARK_OAK_DOOR) {
            return new ItemStack(Items.DARK_OAK_DOOR);
        } else if (doorBlock == Blocks.MANGROVE_DOOR) {
            return new ItemStack(Items.MANGROVE_DOOR);
        } else if (doorBlock == Blocks.CHERRY_DOOR) {
            return new ItemStack(Items.CHERRY_DOOR);
        } else if (doorBlock == Blocks.BAMBOO_DOOR) {
            return new ItemStack(Items.BAMBOO_DOOR);
        } else if (doorBlock == Blocks.CRIMSON_DOOR) {
            return new ItemStack(Items.CRIMSON_DOOR);
        } else if (doorBlock == Blocks.WARPED_DOOR) {
            return new ItemStack(Items.WARPED_DOOR);
        } else {
            return new ItemStack(Items.OAK_DOOR); // 默认
        }
    }
}