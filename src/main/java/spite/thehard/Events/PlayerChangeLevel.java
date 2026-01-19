package spite.thehard.Events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Set;
import java.util.WeakHashMap;

public class PlayerChangeLevel {
    private static final Set<net.minecraft.world.item.Item> ICE_ITEMS = Set.of(
            Items.SNOWBALL,
            Items.SNOW_BLOCK,
            Items.ICE,
            Items.PACKED_ICE,
            Items.BLUE_ICE,
            Items.POWDER_SNOW_BUCKET
    );
    private static final int BASE_CHECK_INTERVAL = 20;
    private static final int CACHE_REFRESH_INTERVAL = BASE_CHECK_INTERVAL * 4;
    private static final int FIRE_TICKS = 20;
    private static final WeakHashMap<Player, Object[]> PLAYER_CACHE = new WeakHashMap<>();


    public static void onPlayerInNether(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        if (player.level().isClientSide() || !player.level().dimension().equals(Level.NETHER)) {
            return;
        }


        long currentTick = player.tickCount;
        Object[] cacheData = PLAYER_CACHE.get(player);
        boolean hasIceItem = false;


        if (cacheData == null
                || currentTick % BASE_CHECK_INTERVAL == 0
                || currentTick - (Long) cacheData[1] >= CACHE_REFRESH_INTERVAL) {
            hasIceItem = checkPlayerInventoryHasIce(player);

            PLAYER_CACHE.put(player, new Object[]{hasIceItem, currentTick});
        } else {

            hasIceItem = (Boolean) cacheData[0];
        }


        if (!hasIceItem) {
            player.setRemainingFireTicks(FIRE_TICKS);
            player.displayClientMessage(Component.translatable("message.nether.fire"), true);
        }
    }


    private static boolean checkPlayerInventoryHasIce(Player player) {

        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && ICE_ITEMS.contains(stack.getItem())) {
                return true;
            }
        }


        ItemStack offhandStack = player.getOffhandItem();
        if (!offhandStack.isEmpty() && ICE_ITEMS.contains(offhandStack.getItem())) {
            return true;
        }


        for (int i = 9; i < player.getInventory().getContainerSize() - 1; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && ICE_ITEMS.contains(stack.getItem())) {
                return true;
            }
        }

        return false;
    }
}