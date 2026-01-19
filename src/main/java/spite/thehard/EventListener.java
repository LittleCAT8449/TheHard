package spite.thehard;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import spite.thehard.Events.*;

@Mod(Thehard.MODID)
public class EventListener {
    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event){
        PlayerDebuff.playerDebuff(event);
        LavaFire.PlayerHasLavaFireEvent(event);
        LevelWeatherEvent.onLevelWeatherEvent(event);
        LavaFire.PlayerOnFire(event);
        AddMobSpawn.onPlayerTick(event);
        WaterloggedDoorCollapse.onPlayerTick(event);
    }

    @SubscribeEvent
    public static void onPlayerTickPre(PlayerTickEvent.Pre event){
        PlayerLavaFire.onPlayerLavaFire(event);
        PlayerPutFluid.onPlayerPutFluid(event);
        PlayerTick.onPlayerJoin(event);
        PlayerTick.onPlayerSwim(event);
        PlayerXpEvent.onPlayerLevelUp(event);
        PlayerChangeLevel.onPlayerInNether(event);
    }

    @SubscribeEvent
    public static void onLivingEntitySpawn(FinalizeSpawnEvent event){
        LivingEntitySpawnFix.CreeperSpawnFix(event);
        LivingEntitySpawnFix.SkeletonSpawnFix(event);
        LivingEntitySpawnFix.ZombieSpawnFix(event);
        LivingEntitySpawnFix.SpiderSpawnFix(event);
        GuardianSpawn.onGuardianSpawn(event);
        LivingEntitySpawnFix.EndermanSpawnFix(event);
        DynamicDragonHealthModifier.onDragonSpawn(event);

    }
    @SubscribeEvent
    public static void onCloudEffectSpawn(EntityJoinLevelEvent event){

        CloudEffect.onCloudEffectSpawn(event);
        EntityTarget.oZombiedPiglinJoinLevelEvent(event);
        EntityTarget.onIronGolemJoinLevelEvent(event);
        EntityTarget.onPiglinJoinLevelEvent(event);

    }
    @SubscribeEvent
    public static void onLivingEntityDeathFixEvent(ExplosionEvent.Detonate event){
        LivingEntityExplosionFix.onCreeperDeath(event);
    }
    @SubscribeEvent
    public static void onAttackEvent(AttackEntityEvent event){
        PlayerAttackEvent.onAttack(event);
    }

    @SubscribeEvent
    public static void onLivingEntityHurtEvent(LivingDamageEvent.Pre event){

        PlayerHurt.onPlayerHurtDropItem(event);
        PlayerHurt.onPlayerHurt(event);

    }

    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event){
        PlayerBreakBlock.onPlayerBreakBlock(event);
        PlayerBreakBlock.onPlayerBreakSpawnSilverFish(event);
        PlayerBreakBlock.onBlockSummonTnt(event);
    }

    @SubscribeEvent
    public static void onPlayerUseItem(LivingEntityUseItemEvent.Finish event){
        PlayerUseItem.onPlayerEatItem(event);
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event){
        LevelTick.onNightRainTick(event);
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event){
        PlayerDeathEvent.onPlayerDeath(event);
    }


    @SubscribeEvent
    public static void onEntItyEvent(EntityTickEvent.Post event){
        BoatSunkEvent.onBoatSunk(event);
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event){

        PlayerJump.onPlayerJump(event);

    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event){

    }
    @SubscribeEvent
    public static void onPlayerSleep(CanPlayerSleepEvent event){
        PlayerSleepEvent.onPlayerSleep(event);
    }


}
