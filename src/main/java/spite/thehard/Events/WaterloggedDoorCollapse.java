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

    // ========== 可配置参数 ==========
    private static final int CHECK_INTERVAL = 5; // 每5刻检查一次（0.25秒），平衡性能与响应速度
    private static final double COLLISION_BOX_EXPAND = 0.3; // 碰撞箱检测范围
    // ===============================

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        // 1. 降低检测频率
        if (event.getEntity().tickCount % CHECK_INTERVAL != 0) {
            return;
        }

        // 2. 确保在服务端执行
        if (!(event.getEntity().level() instanceof ServerLevel serverLevel)) {
            return;
        }

        // 3. 获取玩家碰撞箱并稍微扩大，用于检测"接触"到的方块
        AABB playerBox = event.getEntity().getBoundingBox().inflate(COLLISION_BOX_EXPAND);

        // 4. 遍历玩家碰撞箱覆盖的所有方块位置
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

    /**
     * 检查指定位置是否为浸水木门，如果是则破坏它
     */
    private static void checkAndBreakWaterloggedDoor(ServerLevel level, BlockPos pos, Player player) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        // 1. 检查是否为木门（原版所有木门）
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

        // 2. 检查门是否在水里（简化版：检查门方块周围是否有水）
        boolean isInWater = isDoorSurroundedByWater(level, pos, state);

        if (isInWater) {
            breakDoorAndDrop(level, pos, state, block, player);
        }
    }

    /**
     * 检查门是否被水包围
     */
    private static boolean isDoorSurroundedByWater(ServerLevel level, BlockPos pos, BlockState state) {
        // 检查这个门方块周围的6个面是否接触水
        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);
            FluidState adjacentFluid = level.getFluidState(adjacentPos);

            // 检查相邻位置是否有水（水源或流水）
            if (adjacentFluid.getType() == Fluids.WATER ||
                    adjacentFluid.getType() == Fluids.FLOWING_WATER) {
                // 检查水的高度是否足够高（至少覆盖门的一部分）
                if (adjacentFluid.getAmount() >= 8) { // 8代表几乎满的水
                    return true;
                }
            }

            // 也可以检查相邻方块是否本身就是水方块
            if (level.getBlockState(adjacentPos).getBlock() == Blocks.WATER) {
                return true;
            }
        }

        // 如果是双门的一部分，检查另一个门方块周围是否有水
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

    /**
     * 破坏门方块并生成掉落物
     */
    private static void breakDoorAndDrop(ServerLevel level, BlockPos pos, BlockState state, Block doorBlock, Player player) {
        // 1. 播放破坏音效（木门被破坏的声音）
        level.playSound(null, pos,
                net.minecraft.sounds.SoundEvents.WOOD_BREAK,
                net.minecraft.sounds.SoundSource.BLOCKS,
                0.8F, 0.9F + level.getRandom().nextFloat() * 0.2F);

        // 2. 获取对应的门物品
        ItemStack doorItem = getDoorItem(doorBlock);

        // 3. 如果门是双门的一部分，需要同时破坏另一部分
        if (state.hasProperty(DoorBlock.HALF)) {
            BlockPos otherHalfPos = state.getValue(DoorBlock.HALF) ==
                    net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER
                    ? pos.above()
                    : pos.below();

            // 破坏另一部分（但只掉落一个门物品）
            level.destroyBlock(otherHalfPos, false, player);
        }

        // 4. 破坏这个门方块
        level.destroyBlock(pos, false, player);

        // 5. 生成门物品掉落物
        ItemEntity itemEntity = new ItemEntity(level,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                doorItem);
        itemEntity.setDefaultPickUpDelay(); // 设置默认拾取延迟
        level.addFreshEntity(itemEntity);
    }

    /**
     * 根据门方块类型获取对应的物品
     */
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