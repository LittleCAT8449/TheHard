package spite.thehard.Events;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.function.Supplier;

public class PlayerTick {
    public static Supplier<MobEffectInstance> dig_slowdown = ( () -> new MobEffectInstance(MobEffects.MINING_FATIGUE,120, 0, true, false));
    public static void onPlayerJoin(PlayerTickEvent.Pre event) {

        if (event.getEntity() instanceof Player player) {
            player.addEffect(dig_slowdown.get());
        }
    }
    private static final Supplier<MobEffectInstance> SLOWNESS = () -> new MobEffectInstance(MobEffects.SLOWNESS,120, 1, true, false);
    public static void onPlayerSwim(PlayerTickEvent.Pre event){

        if(event.getEntity() instanceof Player player)
            if(player.isInWater()){
                player.addEffect(SLOWNESS.get());
            }


    }
}
