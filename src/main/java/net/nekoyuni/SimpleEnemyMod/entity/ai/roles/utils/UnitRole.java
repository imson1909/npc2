package net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils;

import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.IRoleGoals;

public enum UnitRole {
   HOSTILE((entity) -> {
      entity.goalSelector.addGoal(0, new net.minecraft.world.entity.ai.goal.FloatGoal(entity));
      entity.goalSelector.addGoal(1, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SmartSquadDoorGoal(entity, 6.0));
      entity.goalSelector.addGoal(2, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SeekCoverGoal((net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit)entity, 1.1, 13));
      entity.goalSelector.addGoal(3, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.MoveToAttackRangeGoal(entity, 96.0, 88.0, 1.2));
      entity.goalSelector.addGoal(4, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.PeekFromCoverGoal((net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit)entity, 1.1));
      entity.goalSelector.addGoal(5, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.TacticalManeuverGoal((net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit)entity));
      entity.goalSelector.addGoal(6, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.RangedGunAttackGoal(
              entity,
              ((Double)net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig.MAX_SHOOT_DISTANCE.get()).floatValue(),
              ((Double)net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig.BASE_SPREAD.get()).floatValue(),
              ((Double)net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig.SPREAD_INCREASE.get()).floatValue(),
              (Integer)net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig.MIN_BURST.get(),
              (Integer)net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig.MAX_BURST.get(),
              (Integer)net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig.MIN_BURST_COOLDOWN.get(),
              (Integer)net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig.MAX_BURST_COOLDOWN.get()
      ));
      entity.goalSelector.addGoal(7, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.LongPatrolGoal(entity, 1.1, 30));
      entity.goalSelector.addGoal(8, new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(entity, net.minecraft.world.entity.player.Player.class, 8.0F));
      entity.goalSelector.addGoal(9, new net.minecraft.world.entity.ai.goal.RandomLookAroundGoal(entity));

      entity.targetSelector.addGoal(1, new net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<>(entity, net.minecraft.world.entity.player.Player.class, true));
      entity.targetSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal<>(entity, net.minecraft.world.entity.monster.Monster.class, true));
   });

   private final IRoleGoals goals;

   UnitRole(IRoleGoals goals) {
      this.goals = goals;
   }

   public IRoleGoals getGoals() {
      return this.goals;
   }
}