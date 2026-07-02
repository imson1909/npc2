package net.nekoyuni.SimpleEnemyMod.entity.ai.roles;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.MoveToAttackRangeGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.PeekFromCoverGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.RangedGunAttackGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SeekCoverGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SmartSquadDoorGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.TacticalManagerGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.TacticalManeuverGoal;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public abstract class AbstractFriendlyGoals implements IRoleGoals {
   @Override
   public void addGoals(PathfinderMob entity) {
      entity.goalSelector.addGoal(0, new TacticalManagerGoal((AbstractUnit)entity));
      entity.goalSelector.addGoal(0, new FloatGoal(entity));
      entity.goalSelector.addGoal(1, new SmartSquadDoorGoal(entity, 6.0));
      entity.goalSelector.addGoal(2, new SeekCoverGoal((AbstractUnit)entity, 1.1, 13));
      entity.goalSelector.addGoal(3, new MoveToAttackRangeGoal(entity, 96.0, 88.0, 1.2));
      entity.goalSelector.addGoal(4, new PeekFromCoverGoal((AbstractUnit)entity, 1.1));
      entity.goalSelector.addGoal(5, new TacticalManeuverGoal((AbstractUnit)entity));
      entity.goalSelector
         .addGoal(
            6,
            new RangedGunAttackGoal(
               entity,
               ((Double)AiShootingConfig.MAX_SHOOT_DISTANCE.get()).floatValue(),
               ((Double)AiShootingConfig.BASE_SPREAD.get()).floatValue(),
               ((Double)AiShootingConfig.SPREAD_INCREASE.get()).floatValue(),
               (Integer)AiShootingConfig.MIN_BURST.get(),
               (Integer)AiShootingConfig.MAX_BURST.get(),
               (Integer)AiShootingConfig.MIN_BURST_COOLDOWN.get(),
               (Integer)AiShootingConfig.MAX_BURST_COOLDOWN.get()
            )
         );
      entity.goalSelector.addGoal(8, new LookAtPlayerGoal(entity, Player.class, 8.0F));
      entity.goalSelector.addGoal(9, new RandomLookAroundGoal(entity));
      this.addSpecificGoals(entity);
   }

   protected abstract void addSpecificGoals(PathfinderMob var1);
}
