package spite.thehard.Events;

import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.monster.Creeper;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class CloudEffect {

    public static void onCloudEffectSpawn(EntityJoinLevelEvent event){

        if(event.getEntity() instanceof AreaEffectCloud effectCloud){
            effectCloud.level().getEntitiesOfClass(Creeper.class,effectCloud.getBoundingBox().inflate(5.0D)).forEach(creeper -> {
                event.setCanceled(true);
            });
        }

    }

}
