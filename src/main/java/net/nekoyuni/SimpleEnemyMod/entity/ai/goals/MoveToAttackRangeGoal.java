package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.SoldierState;

public class MoveToAttackRangeGoal extends Goal {
   private final Mob mob;
   private final double detectionRange;
   private final double attackRange;
   private final double speed;
   private LivingEntity target;
   private int cooldownTicks = 0;
   private static final int COOLDOWN_DURATION = 20;
   private static final double RANGE_BUFFER = 2.0;

   public MoveToAttackRangeGoal(Mob mob, double detectionRange, double attackRange, double speed) {
      this.mob = mob;
      this.detectionRange = detectionRange;
      this.attackRange = attackRange;
      this.speed = speed;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public boolean canUse() {
      if (this.cooldownTicks > 0) {
         this.cooldownTicks--;
         return false;
      }

      this.target = this.mob.getTarget();
      if (this.target != null && this.target.isAlive()) {
         if (this.mob instanceof AbstractUnit unit) {
            SoldierState state = unit.getSoldierState();
            if (state == SoldierState.SEEK_COVER || state == SoldierState.HOLD_COVER || state == SoldierState.TACTICAL_MOV) {
               return false;
            }
         }

         double distanceToTarget = this.mob.distanceTo(this.target);
         return distanceToTarget > this.attackRange + 2.0;
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      if (this.target != null && this.target.isAlive()) {
         if (this.mob.getTarget() != this.target) {
            return false;
         }

         if (this.mob instanceof AbstractUnit unit) {
            SoldierState state = unit.getSoldierState();
            if (state == SoldierState.SEEK_COVER || state == SoldierState.HOLD_COVER || state == SoldierState.TACTICAL_MOV) {
               return false;
            }
         }

         double distanceToTarget = this.mob.distanceTo(this.target);
         return distanceToTarget > this.attackRange;
      } else {
         return false;
      }
   }

   public void start() {
      PathNavigation navigation = this.mob.getNavigation();
      navigation.moveTo(this.target, this.speed);
   }

   public void stop() {
      this.mob.getNavigation().stop();
      this.target = null;
      this.cooldownTicks = 20;
   }

   public void tick() {
      if (this.target != null && this.target.isAlive()) {
         double distanceToTarget = this.mob.distanceTo(this.target);
         if (distanceToTarget <= this.attackRange) {
            PathNavigation navigation = this.mob.getNavigation();
            navigation.stop();
            this.stop();
         } else {
            PathNavigation navigation = this.mob.getNavigation();
            if (!navigation.isInProgress() || navigation.getPath() == null) {
               navigation.moveTo(this.target, this.speed);
            }

            if (this.mob.tickCount % 10 == 0 && navigation.getPath() != null) {
               double distanceToNavTarget = this.mob.distanceTo(this.target);
               if (distanceToNavTarget > 5.0) {
                  navigation.moveTo(this.target, this.speed);
               }
            }
         }
      }
   }

   public boolean requiresUpdateEveryTick() {
      return true;
   }
}
