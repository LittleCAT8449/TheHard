package spite.thehard.Events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;


public class PlayerAttackEvent {


    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();


        boolean isFriendly = target instanceof Animal || target instanceof  Villager;

        if (isFriendly) {
            if (player.getRandom().nextDouble() < 0.35) {
                player.hurt(player.damageSources().mobAttack((LivingEntity) event.getTarget()), 5.0f);
            }
        }
    }


}
