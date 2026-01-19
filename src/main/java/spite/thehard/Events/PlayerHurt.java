package spite.thehard.Events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerHurt {
    public static final Logger LOGGER = LogManager.getLogger();
    public static void onPlayerHurtDropItem(LivingDamageEvent.Pre event) {

        if (event.getEntity() instanceof Player player) {

            player.drop(getPlayerItem(player), true, false);


        }

    }
    public  static ItemStack getPlayerItem(Player player) {

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            int playerSlot = player.getInventory().getContainerSize();
            int randomSlot = player.getRandom().nextInt(playerSlot);
            ItemStack itemStack = player.getInventory().getItem(randomSlot);
            if (!itemStack.isEmpty()) {
                player.getInventory().setItem(randomSlot, ItemStack.EMPTY);
                return itemStack;
            }
        }
        return  ItemStack.EMPTY;
    }

    public static void onPlayerHurt(LivingDamageEvent.Pre event) {
        if(event.getEntity() instanceof Player player) {

            event.setNewDamage((float) (event.getOriginalDamage() * 2.5));
            LOGGER.info("Player hurt event triggered, new damage: " + event.getNewDamage());

        }
    }
}
