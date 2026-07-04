package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.entity.ai.orders.OrderType;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.SoldierState;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.StrategyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TacticalManagerGoal extends Goal {
   private static final Logger LOGGER = LoggerFactory.getLogger(TacticalManagerGoal.class);
   private static final boolean isDebug = false;
   private final AbstractUnit unit;
   private final float RETREAT_HEALTH_PCT = 0.35F;
   private final double ENGAGE_DIST_FAR = 64.0;
   private final double ENGAGE_DIST_MID = 15.0;
   private LivingEntity lastTarget = null;
   private long lastLogTime = 0L;
   private boolean isForcedRush = false;
   private long forcedRushStartTick = 0L;
   private static final long MAX_RUSH_TIME = 200L;
   private final long PATIENCE_TIMEOUT;
   private final long FLANKING_DURATION;
   private final long COVER_WAIT_TIME;
   private final double RUSH_SPEED;

   public TacticalManagerGoal(AbstractUnit unit) {
      this.unit = unit;
      this.setFlags(EnumSet.noneOf(Flag.class));
      // Use defaults since AIDifficultySettings might not exist
      this.PATIENCE_TIMEOUT = 100L;
      this.FLANKING_DURATION = 200L;
      this.COVER_WAIT_TIME = 40L;
      this.RUSH_SPEED = 1.3;
   }

   public boolean canUse() {
      return true;
   }

   public boolean canContinueToUse() {
      return true;
   }

   public void tick() {
      long time = this.unit.level().getGameTime();

      // NEW: Check if we should return home (target lost timeout or too far)
      if (this.shouldReturnHome()) {
         if (this.unit.getSoldierState() != SoldierState.IDLE) {
            this.unit.setSoldierState(SoldierState.IDLE);
            this.logDecision("RETURNING HOME - Target lost or too far");
         }
         this.lastTarget = null;
         this.unit.setFlankingActive(false);
         return;
      }

      if (this.unit instanceof PmcUnitEntity pmc) {
         OrderType order = pmc.getOrder();
         if (order == OrderType.HOLD_POSITION) {
            if (this.unit.getSoldierState() != SoldierState.ENGAGE && this.unit.getSoldierState() != SoldierState.IDLE) {
               this.unit.setSoldierState(SoldierState.ENGAGE);
            }
            return;
         }
      }

      if (time - this.lastLogTime > 40L) {
         this.lastLogTime = time;
      }

      LivingEntity target = this.unit.getTarget();

      // NEW: Update last seen target time when we have line of sight
      if (target != null && target.isAlive() && this.unit.hasLineOfSight(target)) {
         this.unit.getEntityData().set(AbstractUnit.LAST_SEEN_TARGET_TIME, time);
      }

      if (target != this.lastTarget) {
         this.isForcedRush = false;
         this.unit.releaseMovementLock();
      }

      if (this.isForcedRush && time - this.forcedRushStartTick > 200L) {
         this.isForcedRush = false;
         this.logDecision("RUSH FAILED (Timeout) - Returning to normal logic");
      }

      if (target == null || !target.isAlive() || target.level() != this.unit.level()) {
         this.isForcedRush = false;
         if (this.unit.getSoldierState() != SoldierState.IDLE) {
            this.unit.setSoldierState(SoldierState.IDLE);
            this.logDecision("NO TARGET - Setting IDLE");
         }
         this.lastTarget = null;
         this.unit.setFlankingActive(false);
      } else if (this.lastTarget == null) {
         this.unit.setSoldierState(SoldierState.SEEK_COVER);
         this.lastTarget = target;
         this.logDecision("NEW TARGET DETECTED");
      } else {
         this.lastTarget = target;
         double distance = this.unit.distanceTo(target);
         boolean hasLOS = this.unit.hasLineOfSight(target);
         boolean isLowHealth = this.unit.getHealth() / this.unit.getMaxHealth() < 0.35F;
         SoldierState currentState = this.unit.getSoldierState();
         if (hasLOS) {
            this.unit.setLastKnownTargetPos(target.position());
            this.unit.setLastSeenTargetTick(time);
         }

         if (isLowHealth) {
            if (this.unit.isFlankingActive()) {
               this.unit.setFlankingActive(false);
               this.logDecision("FLANK CANCELLED (Low Health)");
            }

            if (currentState != SoldierState.HOLD_COVER) {
               if (currentState != SoldierState.SEEK_COVER) {
                  this.unit.setSoldierState(SoldierState.SEEK_COVER);
               }
            }
         } else {
            StrategyType idealStrategy = this.calculateIdealStrategy(distance, hasLOS);
            if (idealStrategy != this.unit.getStrategy()) {
               this.unit.setStrategy(idealStrategy);
               this.logDecision("STRATEGY -> " + idealStrategy + " (D:" + String.format("%.1f", distance) + ", LoS:" + hasLOS + ")");
            }

            switch (this.unit.getStrategy()) {
               case MID_RANGE:
               case LONG_RANGE:
                  this.handleMidRangeFlanking(target, time, hasLOS, distance, currentState);
                  break;
               case CLOSE_RANGE:
                  this.handleCloseRange(target, time, hasLOS, distance, currentState);
            }
         }
      }
   }

   // NEW: Check if unit should return home
   private boolean shouldReturnHome() {
      LivingEntity target = this.unit.getTarget();
      boolean hasTarget = target != null && target.isAlive();

      if (hasTarget) {
         // If too far from home while chasing, return
         if (this.unit.isTooFarFromHome()) {
            return true;
         }
         return false;
      }

      // No target - check if we should return home after timeout
      return this.unit.isTargetLostTimeoutExpired();
   }

   private StrategyType calculateIdealStrategy(double distance, boolean hasLOS) {
      if (distance <= 15.0) {
         if (this.isForcedRush) {
            this.isForcedRush = false;
            this.logDecision("RUSH SUCCESSFUL - Entering natural CLOSE_RANGE");
         }

         if (this.unit.isFlankingActive()) {
            this.unit.setFlankingActive(false);
            this.logDecision("FLANK AUTO-CANCELLED (Entered Close Range)");
         }

         return StrategyType.CLOSE_RANGE;
      } else if (this.isForcedRush) {
         return StrategyType.CLOSE_RANGE;
      } else {
         return distance > 64.0 ? StrategyType.LONG_RANGE : StrategyType.MID_RANGE;
      }
   }

   private void handleMidRangeFlanking(LivingEntity target, long time, boolean hasLOS, double distance, SoldierState currentState) {
      long timeSinceLastSeen = time - this.unit.getLastSeenTargetTick();
      if (hasLOS) {
         if (this.unit.isFlankingActive()) {
            this.unit.setFlankingActive(false);
            this.unit.releaseMovementLock();
            this.logDecision("FLANK CANCELLED (LoS Recovered)");
         }

         if (currentState == SoldierState.TACTICAL_MOV || currentState == SoldierState.ENGAGE) {
            this.unit.setSoldierState(SoldierState.SEEK_COVER);
            this.unit.lockMovementForStrategy(this.unit.getStrategy());
            this.logDecision("SEEKING COVER (Strategy: " + this.unit.getStrategy() + ")");
         }
      } else if (timeSinceLastSeen < this.PATIENCE_TIMEOUT) {
         if (!this.unit.isFlankingActive()) {
            if (currentState != SoldierState.HOLD_COVER && currentState != SoldierState.SEEK_COVER) {
               this.unit.setSoldierState(SoldierState.SEEK_COVER);
               this.unit.lockMovementForStrategy(this.unit.getStrategy());
            }

            if (time % 40L == 0L) {
               this.logDecision("WAITING (Patience: " + timeSinceLastSeen + "/" + this.PATIENCE_TIMEOUT + ")");
            }
         }
      } else if (!this.unit.isFlankingActive()) {
         this.unit.setFlankingActive(true);
         this.unit.setFlankingStartTick(time);
         this.unit.releaseMovementLock();
         Vec3 pivotPos = target != null ? target.position() : this.unit.getLastKnownTargetPos();
         if (pivotPos == null) {
            this.unit.setFlankingActive(false);
         } else {
            double dx = this.unit.getX() - pivotPos.x;
            double dz = this.unit.getZ() - pivotPos.z;
            float currentAngle = (float)Math.toDegrees(Math.atan2(dz, dx));
            if (currentAngle < 0.0F) {
               currentAngle += 360.0F;
            }

            this.unit.setFlankingAngle(currentAngle);
            this.unit.setFlankingDirection(this.unit.getRandom().nextBoolean() ? 1 : -1);
            this.unit.setSoldierState(SoldierState.TACTICAL_MOV);
            this.logDecision("STARTING FLANK (Angle: " + String.format("%.1f", currentAngle) + " degrees)");
         }
      } else {
         long flankingElapsed = time - this.unit.getFlankingStartTick();
         if (flankingElapsed > this.FLANKING_DURATION) {
            this.unit.setFlankingActive(false);
            this.unit.releaseMovementLock();
            this.isForcedRush = true;
            this.forcedRushStartTick = time;
            Vec3 rushTarget = this.unit.getLastKnownTargetPos();
            if (rushTarget == null && target != null) {
               rushTarget = target.position();
            }

            if (rushTarget != null) {
               this.unit.getNavigation().moveTo(rushTarget.x, rushTarget.y, rushTarget.z, this.RUSH_SPEED);
               this.unit.setSoldierState(SoldierState.TACTICAL_MOV);
               this.logDecision(
                       "FLANK TIMEOUT -> ACTIVATING FORCED RUSH to "
                               + String.format("(%.1f, %.1f, %.1f)", rushTarget.x, rushTarget.y, rushTarget.z)
               );
            }
         } else {
            if (time % 40L == 0L) {
               this.logDecision("FLANKING [" + currentState + "] Progress: " + flankingElapsed + "/" + this.FLANKING_DURATION);
            }

            switch (currentState) {
               case HOLD_COVER:
                  if (time % this.COVER_WAIT_TIME == 0L) {
                     this.unit.releaseMovementLock();
                     this.unit.setSoldierState(SoldierState.TACTICAL_MOV);
                     this.logDecision("FLANK: Continuing to next arc position");
                  }
               case SEEK_COVER:
               case TACTICAL_MOV:
                  break;
               case ENGAGE:
                  this.unit.setSoldierState(SoldierState.TACTICAL_MOV);
                  this.logDecision("FLANK: Converting ENGAGE to TACTICAL_MOV");
                  break;
               case IDLE:
               case SEARCH:
                  this.unit.setFlankingActive(false);
                  this.unit.releaseMovementLock();
                  this.logDecision("FLANK ABORTED (Unit entered " + currentState + ")");
            }
         }
      }
   }

   private void handleCloseRange(LivingEntity target, long time, boolean hasLOS, double distance, SoldierState currentState) {
      if (hasLOS) {
         if (currentState != SoldierState.ENGAGE) {
            this.unit.setSoldierState(SoldierState.ENGAGE);
            this.logDecision("CLOSE RANGE -> ENGAGE (LoS, D:" + String.format("%.1f", distance) + ")");
         }
      } else {
         long timeSinceLastSeen = time - this.unit.getLastSeenTargetTick();
         if (timeSinceLastSeen > this.PATIENCE_TIMEOUT) {
            if (currentState != SoldierState.TACTICAL_MOV) {
               this.unit.setSoldierState(SoldierState.TACTICAL_MOV);
               this.logDecision("CLOSE RANGE -> TACTICAL_MOV (Lost LoS, patience expired)");
            }

            Vec3 lastKnown = this.unit.getLastKnownTargetPos();
            if (lastKnown != null && this.unit.getNavigation().isDone()) {
               this.unit.getNavigation().moveTo(lastKnown.x, lastKnown.y, lastKnown.z, this.RUSH_SPEED);
            }
         } else if (currentState != SoldierState.SEEK_COVER) {
            this.unit.setSoldierState(SoldierState.SEEK_COVER);
            this.unit.lockMovementForStrategy(StrategyType.CLOSE_RANGE);
            this.logDecision("CLOSE RANGE -> SEEK_COVER (Lost LoS, waiting)");
         }
      }
   }

   private void logDecision(String message) {
      if (isDebug) {
         LOGGER.info("[TacticalManager] Unit {}: {}", this.unit.getId(), message);
      }
   }
}