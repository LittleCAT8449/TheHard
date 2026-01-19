package spite.thehard.Events;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;



public class DynamicDragonHealthModifier {
    private static final double CUSTOM_DRAGON_HEALTH = 1500.0D;


    public static void onDragonSpawn(FinalizeSpawnEvent event) {

        if (event.getEntity() instanceof EnderDragon dragon) {

            ResourceKey<Level> dragonDimension = dragon.level().dimension();
            if(dragonDimension.equals(BuiltinDimensionTypes.END) ){
                dragon.getAttribute(Attributes.MAX_HEALTH).setBaseValue(CUSTOM_DRAGON_HEALTH);
                dragon.setHealth((float) CUSTOM_DRAGON_HEALTH);
            }
        }

    }
}