package spite.thehard.Events;

import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LivingEntityExplosionFix {
    public static final Logger LOGGER = LogManager.getLogger();
    public static void onCreeperDeath(ExplosionEvent.Detonate event) {
        if (event.getExplosion().getDirectSourceEntity() instanceof Creeper creeper) {
            for(int i=0;i<=3;i++) {
                double X = Math.random() * ((creeper.getX() + 1) - (creeper.getX() - 1)) + (creeper.getX() - 1);
                double Z = Math.random() * ((creeper.getZ() + 1) - (creeper.getZ() - 1)) + (creeper.getZ() - 1);
                spawnMob(creeper.level(), EntityType.TNT, X, creeper.getY(), Z);
            }


        }
    }
    public static <T extends net.minecraft.world.entity.Entity> T spawnMob(Level level, EntityType<T> entityType, double X, double Y, double Z) {


        if (level.isClientSide()) {
            return null;
        }
        T mob = entityType.create(level, EntitySpawnReason.SPAWNER);


        Vec3 pos = new Vec3(X, Y, Z);
        mob.moveOrInterpolateTo(pos,level.getRandom().nextFloat() * 36,0.0F );
        boolean spawnSuccess = level.addFreshEntity(mob);
        if (spawnSuccess) {
            if (mob instanceof PrimedTnt tnt) {
                tnt.setFuse(10);
            }
        }
        return mob;
    }
}
