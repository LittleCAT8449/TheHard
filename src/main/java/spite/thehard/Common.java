package spite.thehard;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Common {
    public  static AttributeModifier setAttributes(Player player, double value, String countType, String  resourceLocationPath){


        Identifier id =Identifier.fromNamespaceAndPath("thehard",resourceLocationPath);
        AttributeMap attributes = player.getAttributes();


        if(countType.equals("ADD_VALUE")){

            AttributeModifier modifier = new AttributeModifier(
                    id,
                    value,
                    AttributeModifier.Operation.ADD_VALUE
            );
            return modifier;
        }
        else if(countType.equals("ADD_MULTIPLIED_BASE")){

            AttributeModifier modifier = new AttributeModifier(
                    id,
                    1+(value),
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            );
            return modifier;

        }

        return null;
    }
}
