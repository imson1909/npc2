package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.SoldierState;
import net.minecraft.core.Vec3i;

public class SeekCoverGoal extends Goal {
   private final AbstractUnit unit;
   private final Mob mob;
   private final double speed;
   private final int searchRadius;
   private int cooldownTicks = 0;
   private boolean abortedByManager = false;
   private boolean isBackwardCover = false;
   @Nullable
   private BlockPos targetCover;

   public SeekCoverGoal(AbstractUnit unit, double speed, int searchRadius) {
      this.unit = unit;
      this.mob = unit;
      this.speed = speed;
      this.searchRadius = Math.max(1, searchRadius);
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public boolean canUse() {
      if (!this.unit.isMovementLockedByManager()) {
         return false;
      }

      if (this.unit.getSoldierState() != SoldierState.SEEK_COVER) {
         return false;
      }

      if (this.cooldownTicks > 0) {
         this.cooldownTicks--;
         return false;
      }

      LivingEntity target = this.mob.getTarget();
      Vec3 dangerPos = null;
      if (target != null) {
         dangerPos = target.getEyePosition();
      } else if (this.unit.getLastKnownTargetPos() != null) {
         dangerPos = this.unit.getLastKnownTargetPos();
      }

      if (dangerPos == null) {
         return false;
      }

      this.targetCover = this.findCoverPosition(dangerPos);
      return this.targetCover != null;
   }

   public void start() {
      if (this.targetCover != null) {
         this.isBackwardCover = this.isCoverBehindUnit();
         if (this.isBackwardCover) {
            this.mob.getNavigation().stop();
         } else {
            this.mob.getNavigation().moveTo(this.targetCover.getX() + 0.5, this.targetCover.getY(), this.targetCover.getZ() + 0.5, this.speed);
         }
      }
   }

   public boolean canContinueToUse() {
      if (!this.unit.isMovementLockedByManager()) {
         this.abortedByManager = true;
         return false;
      } else if (this.unit.getSoldierState() != SoldierState.SEEK_COVER || this.targetCover == null) {
         return false;
      } else {
         return this.isBackwardCover ? !this.mob.blockPosition().closerThan(this.targetCover, 1.5) : !this.mob.getNavigation().isDone();
      }
   }

   public void stop() {
      boolean reachedCover = !this.abortedByManager && this.targetCover != null && this.mob.blockPosition().closerThan(this.targetCover, 2.5);
      if (reachedCover) {
         this.unit.setSoldierState(SoldierState.HOLD_COVER);
         this.unit.setCoverBlock(this.targetCover);
      }

      this.isBackwardCover = false;
      this.unit.releaseMovementLock();
      this.abortedByManager = false;
      this.targetCover = null;
      this.unit.setCoverSearchOrigin(null);
      this.cooldownTicks = 20;
   }

   public void tick() {
      if (this.isBackwardCover) {
         this.tickBackwardStrafe();
      } else {
         if (this.targetCover != null && this.mob.getNavigation().isDone()) {
            this.stop();
         }
      }
   }

   private void tickBackwardStrafe() {
      if (this.targetCover != null) {
         LivingEntity target = this.mob.getTarget();
         if (target != null) {
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
         }

         this.mob.getMoveControl().setWantedPosition(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.speed);
         if (this.mob.blockPosition().closerThan(this.targetCover, 1.5)) {
            this.stop();
         }
      }
   }

   private boolean isCoverBehindUnit() {
      if (this.targetCover == null) {
         return false;
      }

      LivingEntity target = this.mob.getTarget();
      Vec3 lookDir;
      if (target != null) {
         lookDir = new Vec3(target.getX() - this.mob.getX(), 0.0, target.getZ() - this.mob.getZ()).normalize();
      } else {
         float yaw = this.mob.getYRot() * (float) (Math.PI / 180.0);
         lookDir = new Vec3(-Mth.sin(yaw), 0.0, Mth.cos(yaw));
      }

      Vec3 coverDir = new Vec3(this.targetCover.getX() + 0.5 - this.mob.getX(), 0.0, this.targetCover.getZ() + 0.5 - this.mob.getZ())
         .normalize();
      double dot = lookDir.dot(coverDir);
      return dot < -0.5;
   }

   @Nullable
   private BlockPos findCoverPosition(Vec3 targetEyes) {
      Level level = this.mob.level();
      BlockPos overridePos = this.unit.getCoverSearchOrigin();
      BlockPos origin = overridePos != null ? overridePos : this.mob.blockPosition();

      for (int r = 1; r <= this.searchRadius; r++) {
         for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
               if (Math.abs(dx) == r || Math.abs(dz) == r) {
                  BlockPos candidate = origin.offset(dx, 0, dz);
                  BlockState state = level.getBlockState(candidate);
                  if (state.isCollisionShapeFullBlock(level, candidate)) {
                     Vec3 blockCenter = Vec3.atCenterOf(candidate);
                     HitResult hit = level.clip(new ClipContext(targetEyes, blockCenter, Block.COLLIDER, Fluid.NONE, this.mob));
                     if (hit.getType() == Type.BLOCK) {
                        BlockPos standPos = this.findStandableAdjacent(level, candidate, origin);
                        if (standPos != null && this.mob.getNavigation().createPath(standPos, 1) != null) {
                           return standPos;
                        }
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   @Nullable
   private BlockPos findStandableAdjacent(Level level, BlockPos coverBlock, BlockPos origin) {
      Vec3 dirToOrigin = Vec3.atCenterOf(origin).subtract(Vec3.atCenterOf(coverBlock));
      Direction preferred = Direction.getNearest(dirToOrigin.x, dirToOrigin.y, dirToOrigin.z);
      if (preferred.getNormal().equals(Vec3i.ZERO)) {
         BlockPos p = coverBlock.relative(preferred);
         if (this.isStandable(level, p)) {
            return p;
         }
      }

      for (Direction d : Plane.HORIZONTAL) {
         BlockPos p = coverBlock.relative(d);
         if (this.isStandable(level, p)) {
            return p;
         }
      }

      return null;
   }

   private boolean isStandable(Level level, BlockPos pos) {
      BlockState feet = level.getBlockState(pos);
      BlockState head = level.getBlockState(pos.above());
      BlockState floor = level.getBlockState(pos.below());
      return feet.isAir() && head.isAir() && floor.isCollisionShapeFullBlock(level, pos.below());
   }
}
