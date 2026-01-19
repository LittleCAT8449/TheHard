package spite.thehard.Events;

import net.minecraft.world.entity.vehicle.boat.Boat;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class BoatSunkEvent {
    public static void onBoatSunk(EntityTickEvent.Post event) {

        if(event.getEntity() instanceof Boat boat){
            if(boat.isInWater()&&boat.getRandom().nextDouble()<0.01){
                boat.setDeltaMovement(boat.getDeltaMovement().add(0, -0.1, 0));
            }
        }


    }
}
