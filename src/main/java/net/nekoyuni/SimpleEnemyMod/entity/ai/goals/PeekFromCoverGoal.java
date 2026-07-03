package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.SoldierState;

public class PeekFromCoverGoal extends Goal {
   private final AbstractUnit unit;
   private final Mob mob;
   private final double peekSpeed;
   private Vec3 peekPosition;
   private int peekTicks;
   private int cooldownTicks;
   private static final int MAX_PEEK_TICKS = 18;
   private static final int PEEK_COOLDOWN = 25;
   private static final int MAX_SCAN_DISTANCE = 4;
   private static final int LATERAL_RANGE = 4;

   public PeekFromCoverGoal(AbstractUnit unit, double speed) {
      this.unit = unit;
      this.mob = unit;
      this.peekSpeed = speed;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public boolean canUse() {
      if (this.unit.getSoldierState() != SoldierState.HOLD_COVER) {
         return false;
      }

      LivingEntity target = this.mob.getTarget();
      if (target == null || !target.isAlive()) {
         return false;
      }

      if (this.mob.hasLineOfSight(target)) {
         return false;
      }

      if (this.cooldownTicks > 0) {
         this.cooldownTicks--;
         return false;
      }

      BlockPos cover = this.unit.getCoverBlock();
      if (cover == null) {
         return false;
      }

      this.peekPosition = this.findFlexiblePeekSpot(target, cover);
      return this.peekPosition != null;
   }

   public void start() {
      if (this.peekPosition != null) {
         this.peekTicks = 0;
         this.mob.getNavigation().moveTo(this.peekPosition.x + 0.5, this.peekPosition.y, this.peekPosition.z + 0.5, this.peekSpeed);
      }
   }

   public boolean canContinueToUse() {
      return this.unit.getSoldierState() != SoldierState.HOLD_COVER ? false : this.peekTicks < 18;
   }

   public void tick() {
      this.peekTicks++;
      LivingEntity target = this.mob.getTarget();
      if (target != null) {
         this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
         if (this.mob.hasLineOfSight(target)) {
            this.stop();
         } else {
            if (this.mob.getNavigation().isDone() && this.peekTicks > 9) {
               this.stop();
            }
         }
      }
   }

   public void stop() {
      BlockPos cover = this.unit.getCoverBlock();
      if (cover != null) {
         this.mob.getNavigation().moveTo(cover.getX() + 0.5, cover.getY(), cover.getZ() + 0.5, this.peekSpeed);
      }

      this.peekPosition = null;
      this.peekTicks = 0;
      this.cooldownTicks = 25;
   }

   @Nullable
   private Vec3 findFlexiblePeekSpot(LivingEntity target, BlockPos coverBlock) {
      Level level = this.mob.level();
      Vec3 targetEyes = target.getEyePosition();
      Vec3 coverCenter = Vec3.atCenterOf(coverBlock);
      Vec3 toTarget = target.position().subtract(coverCenter).normalize();
      if (Double.isNaN(toTarget.x) || Double.isNaN(toTarget.z)) {
         toTarget = new Vec3(0.0, 0.0, -1.0);
      }

      Vec3 lateral = new Vec3(-toTarget.z, 0.0, toTarget.x).normalize();
      int[] sideOffsets = new int[]{0, 1, -1, 2, -2, 3, -3, 4, -4};

      for (int side : sideOffsets) {
         if (Math.abs(side) <= 4) {
            Vec3 latOffset = lateral.scale(side);

            for (int dist = 1; dist <= 4; dist++) {
               Vec3 candidate = coverCenter.add(latOffset).add(toTarget.scale(dist * 0.5));
               BlockPos foot = BlockPos.containing(candidate);
               if (this.isSafe(level, foot)) {
                  Vec3 eye = Vec3.atCenterOf(foot).add(0.0, this.mob.getEyeHeight(), 0.0);
                  HitResult hit = level.clip(new ClipContext(eye, targetEyes, Block.COLLIDER, Fluid.NONE, this.mob));
                  if (hit.getType() != Type.BLOCK && this.mob.getNavigation().createPath(foot, 1) != null) {
                     return new Vec3(foot.getX(), foot.getY(), foot.getZ());
                  }
               }
            }
         }
      }

      return null;
   }

   private boolean isSafe(Level level, BlockPos pos) {
      BlockPos below = pos.below();
      return level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir() && level.getBlockState(below).isCollisionShapeFullBlock(level, below);
   }
}
