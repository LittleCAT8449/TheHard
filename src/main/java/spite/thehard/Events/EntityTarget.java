package spite.thehard.Events;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class EntityTarget {

    public static void onIronGolemJoinLevelEvent(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof IronGolem ironGolem) || ironGolem.level().isClientSide()) {
            return;
        }
        ironGolem.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                ironGolem,
                Player.class,
                true,
                false
        ));

        ironGolem.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE)
                .setBaseValue(64.0D);


    }

    public static void onPiglinJoinLevelEvent(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Piglin piglin) || piglin.level().isClientSide()) {
            return;
        }
        piglin.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                piglin,
                Player.class,
                true,
                false
        ));
    }

    public static void oZombiedPiglinJoinLevelEvent(EntityJoinLevelEvent event) {

        if (!(event.getEntity() instanceof ZombifiedPiglin zombifiedPiglin) || zombifiedPiglin.level().isClientSide()) {
            return;
        }
        zombifiedPiglin.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                zombifiedPiglin,
                Player.class,
                true,
                false
        ));

    }

}
