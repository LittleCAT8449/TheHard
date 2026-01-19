package spite.thehard.Events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import spite.thehard.Common;

public class PlayerXpEvent {

    public static void onPlayerLevelUp(PlayerTickEvent.Pre event) {

        if (event.getEntity() instanceof Player player) {
            int XpLevel = player.experienceLevel;
            AttributeMap attributes = player.getAttributes();
            if (XpLevel >= 10 && XpLevel < 20) {

                AttributeModifier modifier1 = Common.setAttributes(player, -4, "ADD_VALUE", "health");
                attributes.getInstance(Attributes.MAX_HEALTH).removeModifier(modifier1);
                attributes.getInstance(Attributes.MAX_HEALTH).addTransientModifier(modifier1);
                AttributeModifier modifier2 = Common.setAttributes(player, -4, "ADD_VALUE", "absorption");
                attributes.getInstance(Attributes.MAX_ABSORPTION).removeModifier(modifier2);
                attributes.getInstance(Attributes.MAX_ABSORPTION).addTransientModifier(modifier2);

            } else if (XpLevel >= 20 && XpLevel < 30) {
                AttributeModifier modifier = Common.setAttributes(player, -8, "ADD_VALUE", "health");
                attributes.getInstance(Attributes.MAX_HEALTH).removeModifier(modifier);
                attributes.getInstance(Attributes.MAX_HEALTH).addTransientModifier(modifier);
                AttributeModifier modifier2 = Common.setAttributes(player, -8, "ADD_VALUE", "absorption");
                attributes.getInstance(Attributes.MAX_ABSORPTION).removeModifier(modifier2);
                attributes.getInstance(Attributes.MAX_ABSORPTION).addTransientModifier(modifier2);
            } else if (XpLevel >= 30) {
                AttributeModifier modifier = Common.setAttributes(player, -16, "ADD_VALUE", "health");
                attributes.getInstance(Attributes.MAX_HEALTH).removeModifier(modifier);
                attributes.getInstance(Attributes.MAX_HEALTH).addTransientModifier(modifier);
                AttributeModifier modifier2 = Common.setAttributes(player, -16, "ADD_VALUE", "absorption");
                attributes.getInstance(Attributes.MAX_ABSORPTION).removeModifier(modifier2);

            }

        }
    }
}

