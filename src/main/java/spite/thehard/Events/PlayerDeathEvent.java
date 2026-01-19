package spite.thehard.Events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class PlayerDeathEvent {

    public static void onPlayerDeath(LivingDeathEvent event) {

        if (event.getEntity() instanceof Player player) {
            if (player.level() instanceof ServerLevel serverLevel) {

                BlockPos pos = player.blockPosition();

                Zombie zombie = EntityType.ZOMBIE.create(serverLevel, EntitySpawnReason.SPAWNER);
                zombie.moveOrInterpolateTo(
                        Vec3.atLowerCornerOf(pos),
                        serverLevel.getRandom().nextFloat() * 360.0F, // 随机朝向
                        0.0F
                );
                RegistryAccess registryAccess = zombie.level().registryAccess();
                Registry<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
                ItemStack cod = new ItemStack(Items.COD);
                registry.get(Enchantments.KNOCKBACK)
                        .ifPresent(holder ->cod.enchant(holder, 5));
                zombie.setItemSlot(EquipmentSlot.MAINHAND, cod);
                serverLevel.addFreshEntity(zombie);
                zombie.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
                ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
                registry.get(Enchantments.PROTECTION)
                        .ifPresent(holder -> helmet.enchant(holder, 3));
                zombie.setItemSlot(EquipmentSlot.HEAD,helmet);
                zombie.setDropChance(EquipmentSlot.HEAD, 0.0F);
            }

        }

    }
}
