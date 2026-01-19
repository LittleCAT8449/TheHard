package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

public class GuardianSpawn {
    public static void onGuardianSpawn(FinalizeSpawnEvent event){

        if(event.getEntity().getType()==EntityType.SALMON||event.getEntity().getType()==EntityType.COD||event.getEntity().getType()==EntityType.TROPICAL_FISH){

            BlockPos salmonPos = event.getEntity().blockPosition();
            if(event.getLevel() instanceof ServerLevel serverLevel){
                Guardian guardian = EntityType.GUARDIAN.create(serverLevel, EntitySpawnReason.TRIGGERED);
                guardian.moveOrInterpolateTo(
                        Vec3.atLowerCornerOf(salmonPos),
                        0.0F,
                        0.0F
                );
                serverLevel.addFreshEntity(guardian);
            }

        }
    }

}
