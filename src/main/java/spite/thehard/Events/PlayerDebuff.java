package spite.thehard.Events;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.function.Supplier;

public class PlayerDebuff {
    private static final Supplier<MobEffectInstance>SLOWNESS = () -> new MobEffectInstance(MobEffects.SLOWNESS,120, 0, true, false);

    public static void playerDebuff(PlayerTickEvent.Post event) {
        if(event.getEntity() instanceof  Player player) {
            EquipmentSlot[] armorSlots = new EquipmentSlot[]{
                    EquipmentSlot.HEAD,
                    EquipmentSlot.CHEST,
                    EquipmentSlot.LEGS,
                    EquipmentSlot.FEET
            };

            for (EquipmentSlot E : armorSlots) {
                ItemStack armorItem = player.getItemBySlot(E);
                if (!armorItem.isEmpty()) {
                    player.addEffect(SLOWNESS.get());
                }
            }

        }

    }

}
