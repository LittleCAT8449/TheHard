package spite.thehard.Events;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.Set;
import java.util.function.Supplier;

public class PlayerUseItem {
    private static final Supplier<MobEffectInstance> HUNGER = () -> new MobEffectInstance(MobEffects.HUNGER,240, 1, true, false);
    private static final Supplier<MobEffectInstance>NAUSEA = () -> new MobEffectInstance(MobEffects.NAUSEA,360, 0, true, false);
    private static final Supplier<MobEffectInstance> WEAKNESS = () -> new MobEffectInstance(MobEffects.WEAKNESS, 1200, 0, true, false);
    private static final Set<Item> RAW_MEATS= Set.of(
            Items.MUTTON,
            Items.PORKCHOP,
            Items.RABBIT,
            Items.CHICKEN,
            Items.COD,
            Items.SALMON,
            Items.BEEF,
            Items.TROPICAL_FISH
    );

    public static void onPlayerEatItem(LivingEntityUseItemEvent.Finish event) {

        if(event.getEntity() instanceof Player player){
                if(isRawMeat(event.getItem())){

                    player.addEffect(HUNGER.get());

                }
                else if(event.getItem().getItem() == Items.MILK_BUCKET){

                    player.addEffect(NAUSEA.get());

                }
                else if(event.getItem().getItem()==Items.GOLDEN_APPLE){

                    player.addEffect(WEAKNESS.get());

                }



        }

    }

    private static boolean isRawMeat(ItemStack itemStack) {
        if (RAW_MEATS.contains(itemStack.getItem())) {
            return true;
        }
        return false;
    }
}
