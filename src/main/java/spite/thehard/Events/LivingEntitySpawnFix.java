package spite.thehard.Events;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.skeleton.Bogged;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

import java.util.function.Supplier;

public class LivingEntitySpawnFix {
    public static Supplier<MobEffectInstance> Invisible = ( () -> new MobEffectInstance(MobEffects.INVISIBILITY,216000, 0, true, false));
    public static Supplier<MobEffectInstance> Speed = ( () -> new MobEffectInstance(MobEffects.SPEED,216000, 1, true, false));
    public static Supplier<MobEffectInstance> Strength = ( () -> new MobEffectInstance(MobEffects.STRENGTH,216000, 4, true, false));
    public static void CreeperSpawnFix(FinalizeSpawnEvent event){

        if(event.getEntity() instanceof Creeper creeper){
            if(event.getLevel() instanceof ServerLevel serverLevel) {
                LightningBolt dummyLightning = EntityType.LIGHTNING_BOLT.create(serverLevel, EntitySpawnReason.SPAWNER);
                dummyLightning.moveOrInterpolateTo(Vec3.atLowerCornerOf(creeper.getOnPos()),0.0f,0.0f);
                creeper.thunderHit(serverLevel,dummyLightning);
                creeper.addEffect(Invisible.get());
                dummyLightning.discard();
            }
        }

    }

    public static void SkeletonSpawnFix(FinalizeSpawnEvent event){

        if(event.getEntity() instanceof Skeleton skeleton){

            RegistryAccess registryAccess = skeleton.level().registryAccess();

            Registry<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

            ItemStack helmet = new ItemStack(Items.NETHERITE_HELMET);

            registry.get(Enchantments.PROTECTION).ifPresent(holder -> helmet.enchant(holder, 3));
            skeleton.setItemSlot(EquipmentSlot.HEAD,helmet);
            skeleton.addEffect(Strength.get());

            skeleton.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
            skeleton.setDropChance(EquipmentSlot.HEAD, 0.0F);


        }
        if(event.getEntity() instanceof WitherSkeleton witherSkeleton){


            RegistryAccess registryAccess = witherSkeleton.level().registryAccess();
            Registry<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

            ItemStack helmet = new ItemStack(Items.NETHERITE_HELMET);

            registry.get(Enchantments.PROTECTION).ifPresent(holder -> helmet.enchant(holder, 3));

            witherSkeleton.setItemSlot(EquipmentSlot.HEAD,helmet);
            witherSkeleton.addEffect(Strength.get());

            witherSkeleton.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
            witherSkeleton.setDropChance(EquipmentSlot.HEAD, 0.0F);
        }
        if(event.getEntity()instanceof Stray stray){

            RegistryAccess registryAccess = stray.level().registryAccess();
            Registry<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

            ItemStack helmet = new ItemStack(Items.NETHERITE_HELMET);

            registry.get(Enchantments.PROTECTION).ifPresent(holder -> helmet.enchant(holder, 3));

            stray.setItemSlot(EquipmentSlot.HEAD,helmet);
            stray.addEffect(Strength.get());


            stray.setDropChance(EquipmentSlot.MAINHAND,0.0F);
            stray.setDropChance(EquipmentSlot.HEAD, 0.0F);
        }
        if (event.getEntity() instanceof Bogged bogged){

            RegistryAccess registryAccess = bogged.level().registryAccess();
            Registry<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);

            ItemStack helmet = new ItemStack(Items.NETHERITE_HELMET);


            registry.get(Enchantments.PROTECTION).ifPresent(holder -> helmet.enchant(holder, 3));

            bogged.setItemSlot(EquipmentSlot.HEAD,helmet);
            bogged.addEffect(Strength.get());

            bogged.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
            bogged.setDropChance(EquipmentSlot.HEAD, 0.0F);
        }

    }

    public static void ZombieSpawnFix(FinalizeSpawnEvent event){
        if(event.getEntity() instanceof Zombie zombie){
            RegistryAccess registryAccess = zombie.level().registryAccess();
            Registry<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
            ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
            registry.get(Enchantments.KNOCKBACK).
                    ifPresent(holder -> sword.enchant(holder, 3));
            zombie.setItemSlot(EquipmentSlot.MAINHAND,sword);
            zombie.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        }
    }

    public static void SpiderSpawnFix(FinalizeSpawnEvent event){
        if(event.getEntity() instanceof Spider spider){
          spider.addEffect(Invisible.get());
          spider.addEffect(Speed.get());
        }
    }

    public static void EndermanSpawnFix(FinalizeSpawnEvent event){
        if(event.getEntity() instanceof EnderMan enderman){
            enderman.addEffect(Speed.get());
            RegistryAccess registryAccess = enderman.level().registryAccess();
            Registry<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
            ItemStack cod = new ItemStack(Items.COD);
            registry.get(Enchantments.KNOCKBACK)
                    .ifPresent(holder ->cod.enchant(holder, 5));
            enderman.setItemSlot(EquipmentSlot.MAINHAND, cod);
            enderman.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        }
    }


}
