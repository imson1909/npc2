package net.nekoyuni.SimpleEnemyMod.event.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.nekoyuni.SimpleEnemyMod.config.common.GeneralConfig;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.registry.ModEntities;

@EventBusSubscriber(modid = "simpleenemymod", bus = Bus.FORGE)
public class VillageGarrisonHandler {
   private static final List<VillageGarrisonHandler.PendingGarrison> PENDING_GARRISONS = new ArrayList<>();

   @SubscribeEvent
   public static void onVillagerSpawn(EntityJoinLevelEvent event) {
      if (GeneralConfig.enableVillageGarrison) {
         if (!event.getLevel().isClientSide && event.getEntity() instanceof Villager villager) {
            CompoundTag persistentData = villager.getPersistentData();
            if (!persistentData.getBoolean("sem_garrison_checked")) {
               persistentData.putBoolean("sem_garrison_checked", true);
               PENDING_GARRISONS.add(new VillageGarrisonHandler.PendingGarrison((ServerLevel)event.getLevel(), villager.blockPosition(), 100));
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLevelTick(LevelTickEvent event) {
      if (event.phase == Phase.END && !event.level.isClientSide) {
         ServerLevel currentLevel = (ServerLevel)event.level;
         Iterator<VillageGarrisonHandler.PendingGarrison> iterator = PENDING_GARRISONS.iterator();

         while (iterator.hasNext()) {
            VillageGarrisonHandler.PendingGarrison task = iterator.next();
            if (task.level == currentLevel) {
               task.delayTicks--;
               if (task.delayTicks <= 0) {
                  iterator.remove();
                  BlockPos farCorner = task.pos.offset(40, 0, 40);
                  if (currentLevel.hasChunkAt(farCorner) && currentLevel.hasChunkAt(task.pos)) {
                     List<AbstractUnit> guardsNearby = currentLevel.getEntitiesOfClass(AbstractUnit.class, new AABB(task.pos).inflate(40.0));
                     if (guardsNearby.isEmpty()) {
                        int squadSize = 2 + currentLevel.random.nextInt(3);

                        for (int i = 0; i < squadSize; i++) {
                           spawnGuard(currentLevel, task.pos);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void spawnGuard(ServerLevel level, BlockPos basePos) {
      double dx = (level.random.nextDouble() - 0.5) * 8.0;
      double dz = (level.random.nextDouble() - 0.5) * 8.0;
      BlockPos spawnPos = basePos.offset((int)dx, 0, (int)dz);
      if (level.hasChunkAt(spawnPos)) {
         for (int maxIntentos = 20; level.isEmptyBlock(spawnPos.below()) && spawnPos.getY() > level.getMinBuildHeight() && maxIntentos > 0; maxIntentos--) {
            spawnPos = spawnPos.below();
         }

         AbstractUnit unit = new PmcUnitEntity((EntityType<? extends Monster>)ModEntities.PMCUNIT.get(), level);
         unit.setRole(UnitRole.DEFAULT);
         unit.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
         unit.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.EVENT, null, null);
         level.addFreshEntity(unit);
      }
   }

   private static class PendingGarrison {
      final ServerLevel level;
      final BlockPos pos;
      int delayTicks;

      PendingGarrison(ServerLevel level, BlockPos pos, int delayTicks) {
         this.level = level;
         this.pos = pos;
         this.delayTicks = delayTicks;
      }
   }
}