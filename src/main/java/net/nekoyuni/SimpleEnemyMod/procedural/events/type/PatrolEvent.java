package net.nekoyuni.SimpleEnemyMod.procedural.events.type;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biome;
import net.nekoyuni.SimpleEnemyMod.config.common.EventSpawnConfig;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.procedural.events.system.DynamicEvent;
import net.nekoyuni.SimpleEnemyMod.registry.ModEntities;
import net.nekoyuni.SimpleEnemyMod.spawn.utils.SpawnHelper;

public class PatrolEvent extends DynamicEvent {
   public PatrolEvent() {
      super("military_patrol");
   }

   @Override
   public double getBaseChance() {
      return (Double)EventSpawnConfig.PATROL_BASE_CHANCE.get();
   }

   @Override
   public double getFailureMultiplier() {
      return (Double)EventSpawnConfig.PATROL_FAILURE_MULTIPLIER.get();
   }

   @Override
   public int getMinDistance() {
      return 60;
   }

   @Override
   public int getMaxDistance() {
      return 110;
   }

   @Override
   public double getBiomeModifier(ServerLevel level, BlockPos pos) {
      Holder<Biome> biomeHolder = level.getBiome(pos);
      if (biomeHolder.is(BiomeTags.IS_BEACH) || biomeHolder.is(BiomeTags.IS_RIVER)) {
         return 2.0;
      } else {
         return !biomeHolder.is(BiomeTags.IS_MOUNTAIN) && !biomeHolder.is(BiomeTags.IS_HILL) ? 1.0 : 0.0;
      }
   }

   @Override
   public boolean execute(ServerLevel level, ServerPlayer player, BlockPos pos) {
      if (!SpawnHelper.isValidSpawn(level, pos)) {
         return false;
      }

      int min = (Integer)EventSpawnConfig.PATROL_MIN_SIZE.get();
      int max = (Integer)EventSpawnConfig.PATROL_MAX_SIZE.get();
      int squadSize = min + level.random.nextInt(Math.max(1, max - min + 1));

      for (int i = 0; i < squadSize; i++) {
         this.spawnSoldier(level, pos, false);
      }

      this.spawnSoldier(level, pos, true);
      return true;
   }

   private void spawnSoldier(ServerLevel level, BlockPos basePos, boolean isLeader) {
      double offsetX = (level.random.nextDouble() - 0.5) * 4.0;
      double offsetZ = (level.random.nextDouble() - 0.5) * 4.0;
      BlockPos spawnPos = basePos.offset((int)offsetX, 0, (int)offsetZ);
      spawnPos = new BlockPos(spawnPos.getX(), basePos.getY(), spawnPos.getZ());
      AbstractUnit unit = new PmcUnitEntity((EntityType<? extends Monster>)ModEntities.PMCUNIT.get(), level);

      if (isLeader) {
         unit.setRole(UnitRole.HOSTILE);
      } else {
         unit.setRole(UnitRole.HOSTILE);
      }

      unit.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
      unit.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.EVENT, null, null);
      level.addFreshEntity(unit);
   }
}
