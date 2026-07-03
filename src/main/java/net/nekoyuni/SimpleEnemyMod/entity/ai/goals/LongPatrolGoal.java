package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class LongPatrolGoal extends WaterAvoidingRandomStrollGoal {
   private final int patrolRadius;
   private final int verticalSearchRange;
   private final float minDistanceSqr;

   public LongPatrolGoal(PathfinderMob mob, double speedModifier, int patrolRadius) {
      super(mob, speedModifier);
      this.patrolRadius = patrolRadius;
      this.verticalSearchRange = 7;
      float minDist = patrolRadius * 0.4F;
      this.minDistanceSqr = minDist * minDist;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   @Override
   public boolean canContinueToUse() {
      if (this.mob.isInWater()) {
         return false;
      }
      return super.canContinueToUse();
   }

   @Nullable
   @Override
   protected Vec3 getPosition() {
      Vec3 target = LandRandomPos.getPos(this.mob, this.patrolRadius, this.verticalSearchRange);
      if (target != null && target.distanceToSqr(this.mob.position()) >= this.minDistanceSqr) {
         return target;
      }
      return null;
   }
}