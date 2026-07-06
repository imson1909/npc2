package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.config.AIDifficultySettings;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.SoldierState;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.StrategyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TacticalManeuverGoal extends Goal {
   private static final Logger LOGGER = LoggerFactory.getLogger(TacticalManeuverGoal.class);
   private static final boolean isDebug = false;
   private final AbstractUnit unit;
   private static final int MOVE_COOLDOWN = 10;
   private int lastSeenTick = 0;
   private static final int ATTENTION_SPAN = 40;
   private int pauseTicks = 0;
   private Vec3 lastFlankTarget = Vec3.ZERO;
   private int stuckCounter = 0;
   private static final int MAX_STUCK_CHECKS = 3;
   private final double FLANK_SPEED;
   private final double MID_RANGE_SPEED;
   private int pathRecalcCooldown = 0;
   private static final int PATH_RECALC_INTERVAL = 20;
   private int flankCalcCooldown = 0;
   private static final int FLANK_CALC_INTERVAL = 15;
   private Vec3 cachedFlankPos = null;
   private BlockPos cachedSafePos = null;
   private Vec3 lastCachedOrigin = null;
   private static final double CACHE_INVALIDATION_DIST = 4.0;

   public TacticalManeuverGoal(AbstractUnit unit) {
      this.unit = unit;
      this.setFlags(EnumSet.of(Flag.MOVE));
      AIDifficultySettings settings = AIDifficultySettings.fromConfig();
      this.FLANK_SPEED = settings.flankSpeed;
      this.MID_RANGE_SPEED = settings.midRangeSpeed;
   }

   public boolean canUse() {
      if (this.unit.isTooFarFromHome()) {
         return false;
      }
      SoldierState state = this.unit.getSoldierState();
      return state == SoldierState.ENGAGE || state == SoldierState.TACTICAL_MOV;
   }

   public boolean canContinueToUse() {
      if (this.unit.isTooFarFromHome()) {
         return false;
      }
      if (this.unit.isMovementLockedByManager()) {
         return false;
      }

      SoldierState state = this.unit.getSoldierState();
      return state == SoldierState.ENGAGE || state == SoldierState.TACTICAL_MOV;
   }

   public void tick() {
      LivingEntity target = this.unit.getTarget();
      if (target != null) {
         if (!this.unit.isMovementLockedByManager()) {
            int staggerOffset = this.unit.getId() % 5;
            long currentTime = this.unit.level().getGameTime();
            boolean isMyTurn = currentTime % 5L == staggerOffset;
            if (!isMyTurn) {
               if (this.unit.hasLineOfSight(target)) {
                  this.unit.getLookControl().setLookAt(target, 30.0F, 30.0F);
               }
            } else {
               SoldierState state = this.unit.getSoldierState();
               boolean hasLOS = this.unit.hasLineOfSight(target);
               if (this.pauseTicks > 0) {
                  this.pauseTicks--;
                  this.unit.getLookControl().setLookAt(target, 30.0F, 30.0F);
                  this.unit.getNavigation().stop();
               } else {
                  if (hasLOS) {
                     this.lastSeenTick = (int)currentTime;
                  }

                  boolean shouldLockOnTarget = hasLOS || currentTime - this.lastSeenTick < 40L;
                  if (shouldLockOnTarget) {
                     this.unit.getLookControl().setLookAt(target, 30.0F, 30.0F);
                  }

                  if (state == SoldierState.TACTICAL_MOV && this.unit.isFlankingActive() && this.unit.getStrategy() != StrategyType.CLOSE_RANGE) {
                     this.handleFlankingMovement(target);
                  } else if (state == SoldierState.TACTICAL_MOV) {
                     this.unit.getNavigation().moveTo(target, 1.25);
                  } else if (this.unit.level().getGameTime() - this.unit.getLastMicroMoveTick() >= 10L) {
                     if (this.unit.getStrategy() != StrategyType.CLOSE_RANGE && this.unit.getRandom().nextFloat() < 0.02) {
                        this.pauseTicks = 20 + this.unit.getRandom().nextInt(20);
                     } else {
                        StrategyType strategy = this.unit.getStrategy();
                        if (strategy == null) {
                           strategy = StrategyType.MID_RANGE;
                        }

                        Vec3 movement = null;
                        double speed = 1.0;
                        switch (strategy) {
                           case LONG_RANGE:
                              movement = this.suppressionHold(target);
                              speed = 1.15;
                              break;
                           case MID_RANGE:
                              if (hasLOS) {
                                 movement = this.basicStrafe(target);
                                 speed = this.MID_RANGE_SPEED;
                              } else {
                                 movement = null;
                                 speed = this.MID_RANGE_SPEED;
                              }
                              break;
                           case CLOSE_RANGE:
                              movement = this.aggressiveCQB(target);
                              speed = 1.2;
                        }

                        if (movement != null) {
                           if (strategy != StrategyType.LONG_RANGE) {
                              movement = this.applyWallBias(movement);
                           }

                           this.unit.getNavigation().moveTo(movement.x, movement.y, movement.z, speed);
                           this.unit.setLastMicroMoveTick(this.unit.level().getGameTime());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void handleFlankingMovement(LivingEntity target) {
      if (this.flankCalcCooldown > 0 && this.cachedFlankPos != null) {
         this.flankCalcCooldown--;
      } else {
         this.cachedFlankPos = this.calculateCircularFlankPosition(target);
         this.flankCalcCooldown = 15;
      }

      Vec3 flankPos = this.cachedFlankPos;
      double distToFlankPos = this.unit.position().distanceTo(flankPos);
      double distToLastTarget = this.unit.position().distanceTo(this.lastFlankTarget);
      if (!this.lastFlankTarget.equals(Vec3.ZERO) && flankPos.distanceTo(this.lastFlankTarget) < 3.0) {
         this.stuckCounter++;
         if (this.stuckCounter >= 3) {
            float currentAngle = this.unit.getFlankingAngle();
            currentAngle += 60.0F * this.unit.getFlankingDirection();
            if (currentAngle >= 360.0F) {
               currentAngle -= 360.0F;
            }

            if (currentAngle < 0.0F) {
               currentAngle += 360.0F;
            }

            this.unit.setFlankingAngle(currentAngle);
            this.cachedFlankPos = null;
            this.flankCalcCooldown = 0;
            this.stuckCounter = 0;
            return;
         }
      } else {
         this.stuckCounter = 0;
      }

      this.lastFlankTarget = flankPos;
      boolean destinationChanged = this.cachedFlankPos != null && flankPos.distanceTo(this.cachedFlankPos) > 2.0;
      if (this.pathRecalcCooldown > 0 && !destinationChanged) {
         this.pathRecalcCooldown--;
      } else {
         this.unit.getNavigation().moveTo(flankPos.x, flankPos.y, flankPos.z, this.FLANK_SPEED);
         this.pathRecalcCooldown = 20;
      }

      debug("[ManeuverGoal] Flanking - Unit: " + this.unit.getId() + " | Dist: " + String.format("%.1f", distToFlankPos) + " | Stuck: " + this.stuckCounter);
      if (distToFlankPos < 5.0 || this.unit.getNavigation().isDone()) {
         this.unit.setSoldierState(SoldierState.SEEK_COVER);
         this.cachedFlankPos = null;
      }
   }

   private Vec3 aggressiveCQB(LivingEntity target) {
      Vec3 dir = this.directionTo(target);
      Vec3 lateral = new Vec3(-dir.z, 0.0, dir.x);
      if (this.unit.getRandom().nextBoolean()) {
         lateral = lateral.scale(1.0);
      } else {
         lateral = lateral.scale(-1.0);
      }

      Vec3 moveDir = lateral.scale(3.0).add(dir.scale(1.5)).normalize();
      return this.unit.position().add(moveDir.scale(3.0));
   }

   private Vec3 basicStrafe(LivingEntity target) {
      Vec3 dir = this.directionTo(target);
      Vec3 lateral = new Vec3(-dir.z, 0.0, dir.x);
      if (this.unit.getRandom().nextBoolean()) {
         lateral = lateral.scale(1.0);
      } else {
         lateral = lateral.scale(-1.0);
      }

      Vec3 moveDir = lateral.add(dir.scale(-0.2)).normalize();
      return this.unit.position().add(moveDir.scale(5.0));
   }

   private Vec3 suppressionHold(LivingEntity target) {
      Vec3 dir = this.directionTo(target);
      Vec3 lateral = new Vec3(-dir.z, 0.0, dir.x);
      if (this.unit.getRandom().nextBoolean()) {
         lateral = lateral.scale(1.0);
      } else {
         lateral = lateral.scale(-1.0);
      }

      return this.unit.position().add(lateral.normalize().scale(1.5));
   }

   private Vec3 applyWallBias(Vec3 originalMovePos) {
      Level level = this.unit.level();
      BlockPos currentPos = this.unit.blockPosition();
      Vec3 leftDir = this.unit.getViewVector(1.0F).yRot((float)Math.toRadians(90.0));
      Vec3 rightDir = this.unit.getViewVector(1.0F).yRot((float)Math.toRadians(-90.0));
      boolean wallOnLeft = !level.getBlockState(currentPos.relative(this.getDirectionFromVec(leftDir), 2)).isAir();
      boolean wallOnRight = !level.getBlockState(currentPos.relative(this.getDirectionFromVec(rightDir), 2)).isAir();
      if (wallOnLeft && !wallOnRight) {
         return originalMovePos.add(leftDir.scale(1.5));
      } else {
         return wallOnRight && !wallOnLeft ? originalMovePos.add(rightDir.scale(1.5)) : originalMovePos;
      }
   }

   private Direction getDirectionFromVec(Vec3 vec) {
      return Direction.getNearest(vec.x, vec.y, vec.z);
   }

   private Vec3 directionTo(LivingEntity target) {
      return new Vec3(target.getX() - this.unit.getX(), 0.0, target.getZ() - this.unit.getZ()).normalize();
   }

   private Vec3 calculateCircularFlankPosition(LivingEntity target) {
      Vec3 lastKnownPos = this.unit.getLastKnownTargetPos();
      if (lastKnownPos == null) {
         return this.basicStrafe(target);
      }

      double currentDistance = this.unit.position().distanceTo(lastKnownPos);
      double flankRadius;
      if (currentDistance > 55.0) {
         flankRadius = 55.0;
      } else if (currentDistance > 20.0) {
         flankRadius = currentDistance;
      } else {
         flankRadius = 17.0;
      }

      flankRadius = Math.max(flankRadius, 15.0);
      flankRadius = Math.min(flankRadius, 55.0);
      float angleIncrement;
      if (flankRadius > 45.0) {
         angleIncrement = 25.0F;
      } else if (flankRadius > 30.0) {
         angleIncrement = 40.0F;
      } else {
         angleIncrement = 50.0F;
      }

      float currentAngle = this.unit.getFlankingAngle();
      int direction = this.unit.getFlankingDirection();
      currentAngle += angleIncrement * direction;
      if (currentAngle >= 360.0F) {
         currentAngle -= 360.0F;
      }

      if (currentAngle < 0.0F) {
         currentAngle += 360.0F;
      }

      this.unit.setFlankingAngle(currentAngle);
      double radians = Math.toRadians(currentAngle);
      double offsetX = Math.cos(radians) * flankRadius;
      double offsetZ = Math.sin(radians) * flankRadius;
      Vec3 targetPos = new Vec3(lastKnownPos.x + offsetX, lastKnownPos.y, lastKnownPos.z + offsetZ);
      BlockPos targetBlock = BlockPos.containing(targetPos);
      BlockPos safePos = this.findNearestSafePosition(targetBlock, 5);
      if (safePos != null) {
         this.unit.setCoverSearchOrigin(safePos);
         return Vec3.atCenterOf(safePos);
      } else {
         return targetPos;
      }
   }

   @Nullable
   private BlockPos findNearestSafePosition(BlockPos target, int searchRadius) {
      Vec3 targetVec = Vec3.atCenterOf(target);
      if (this.cachedSafePos != null && this.lastCachedOrigin != null && targetVec.distanceTo(this.lastCachedOrigin) < 4.0) {
         return this.cachedSafePos;
      }

      Level level = this.unit.level();

      for (int r = 0; r <= searchRadius; r++) {
         for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
               if (Math.abs(dx) == r || Math.abs(dz) == r || r <= 0) {
                  BlockPos candidate = target.offset(dx, 0, dz);
                  if (level.getBlockState(candidate).isAir()
                          && level.getBlockState(candidate.above()).isAir()
                          && level.getBlockState(candidate.below()).isCollisionShapeFullBlock(level, candidate.below())) {
                     this.cachedSafePos = candidate;
                     this.lastCachedOrigin = targetVec;
                     return candidate;
                  }
               }
            }
         }
      }

      this.cachedSafePos = null;
      this.lastCachedOrigin = targetVec;
      return null;
   }

   public void stop() {
      this.unit.getNavigation().stop();
      this.cachedFlankPos = null;
      this.cachedSafePos = null;
      this.lastCachedOrigin = null;
      this.pathRecalcCooldown = 0;
      this.flankCalcCooldown = 0;
   }

   private static void debug(String message, Object... args) {
   }
}