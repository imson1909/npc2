package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils.FormationUtils;
import net.nekoyuni.SimpleEnemyMod.entity.ai.orders.ICommandableMob;
import net.nekoyuni.SimpleEnemyMod.entity.ai.orders.OrderType;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class CommanderOrderGoal extends Goal {
   private final PathfinderMob mob;
   private final ICommandableMob commandable;
   private final double speedModifier;
   private final float stopDistance;
   private final float startDistance;
   private int timeToRecalcPath;
   private Vec3 frozenFormationDirection = null;
   private Vec3 lastOwnerPosition = Vec3.ZERO;
   private static final int FORMATION_COOLDOWN = 100;
   private int ticksSinceLastCombat = 100;

   public CommanderOrderGoal(PathfinderMob mob, double speedModifier, float startDist, float stopDist) {
      this.mob = mob;
      if (!(mob instanceof ICommandableMob)) {
         throw new IllegalArgumentException("CommanderOrderGoal requires an ICommandableMob");
      }

      this.commandable = (ICommandableMob)mob;
      this.speedModifier = speedModifier;
      this.startDistance = startDist;
      this.stopDistance = stopDist;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public void resetCombatCooldown() {
      this.ticksSinceLastCombat = 100;
   }

   public boolean canUse() {
      if (this.mob instanceof AbstractUnit unit && unit.isTooFarFromHome()) {
         return false;
      }
      if (this.commandable.getOwnerUUID() == null) {
         return false;
      }

      LivingEntity owner = this.mob.level().getPlayerByUUID(this.commandable.getOwnerUUID());
      return owner == null ? false : this.mob.getTarget() == null || !this.mob.getTarget().isAlive();
   }

   public boolean canContinueToUse() {
      if (this.mob.getTarget() != null) {
         this.ticksSinceLastCombat = 0;
         return false;
      } else {
         return this.canUse();
      }
   }

   public void start() {
      super.start();
      this.frozenFormationDirection = null;
      this.lastOwnerPosition = Vec3.ZERO;
   }

   public void tick() {
      if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) {
         this.ticksSinceLastCombat = 0;
      } else {
         this.ticksSinceLastCombat++;
         OrderType order = this.commandable.getOrder();
         boolean hasActiveOrder = order == OrderType.HOLD_POSITION
                 || order == OrderType.MOVE_TO_POSITION
                 || order == OrderType.FOLLOW_COMMANDER
                 || order == OrderType.FORM_WEDGE
                 || order == OrderType.FORM_COLUMN;
         if (hasActiveOrder || this.ticksSinceLastCombat >= 100) {
            switch (order) {
               case HOLD_POSITION:
                  this.performHoldPosition();
                  break;
               case FOLLOW_COMMANDER:
               case FORM_WEDGE:
               case FORM_COLUMN:
                  this.performFollowOwner(order);
                  break;
               case MOVE_TO_POSITION:
                  this.performMoveToPosition();
            }
         }
      }
   }

   private void performHoldPosition() {
      if (!this.mob.getNavigation().isDone()) {
         this.mob.getNavigation().stop();
      }
   }

   private void performFollowOwner(OrderType currentOrder) {
      LivingEntity owner = this.mob.level().getPlayerByUUID(this.commandable.getOwnerUUID());
      if (owner != null) {
         int myIndex = this.mob instanceof PmcUnitEntity pmc ? pmc.getFormationIndex() : 0;
         Vec3 idealTarget;
         if (currentOrder == OrderType.FORM_COLUMN && myIndex > 0) {
            PmcUnitEntity predecessor = this.findPredecessor(owner, myIndex - 1);
            if (predecessor != null) {
               float yRot = predecessor.yBodyRot;
               double rad = Math.toRadians(yRot);
               Vec3 offset = new Vec3(-Math.sin(rad), 0.0, Math.cos(rad)).normalize().scale(-1.5);
               idealTarget = predecessor.position().add(offset);
            } else {
               idealTarget = this.calculateClassicFormationTarget(owner, currentOrder, myIndex);
            }
         } else {
            idealTarget = this.calculateClassicFormationTarget(owner, currentOrder, myIndex);
         }

         double distSqr = this.mob.distanceToSqr(idealTarget);
         double stopThreshold;
         double startThreshold;
         if (currentOrder == OrderType.FORM_COLUMN) {
            stopThreshold = 1.44;
            startThreshold = 3.24;
         } else if (currentOrder == OrderType.FORM_WEDGE) {
            stopThreshold = 4.0;
            startThreshold = 20.25;
         } else {
            stopThreshold = this.stopDistance * this.stopDistance;
            startThreshold = this.startDistance * this.startDistance;
         }

         if (distSqr < stopThreshold) {
            if (!this.mob.getNavigation().isDone()) {
               this.mob.getNavigation().stop();
            }
         } else if (distSqr > startThreshold && --this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            Vec3 finalMoveTarget = idealTarget;
            if (myIndex == 0 && !this.isPositionSafe(idealTarget)) {
               finalMoveTarget = owner.position();
            }

            this.mob.getNavigation().moveTo(finalMoveTarget.x, finalMoveTarget.y, finalMoveTarget.z, this.speedModifier);
         }
      }
   }

   private Vec3 calculateClassicFormationTarget(LivingEntity owner, OrderType currentOrder, int myIndex) {
      Vec3 currentOwnerPos = owner.position();
      double positionChangeSqr = this.lastOwnerPosition.distanceToSqr(currentOwnerPos);
      boolean ownerMoved = positionChangeSqr > 0.25;
      float yRot = owner.yBodyRot;
      double f = yRot * (Math.PI / 180.0);
      Vec3 currentBodyDirection = new Vec3(-Math.sin(f), 0.0, Math.cos(f)).normalize();
      Vec3 formationDirection;
      if (currentOrder == OrderType.FOLLOW_COMMANDER) {
         formationDirection = currentBodyDirection;
         this.frozenFormationDirection = null;
      } else if (ownerMoved) {
         this.frozenFormationDirection = currentBodyDirection;
         formationDirection = currentBodyDirection;
         this.lastOwnerPosition = currentOwnerPos;
      } else {
         if (this.frozenFormationDirection == null) {
            this.frozenFormationDirection = currentBodyDirection;
            this.lastOwnerPosition = currentOwnerPos;
         }

         formationDirection = this.frozenFormationDirection;
      }

      return currentOrder == OrderType.FOLLOW_COMMANDER
              ? owner.position()
              : FormationUtils.getTargetPosition(owner.position(), formationDirection, currentOrder, myIndex);
   }

   private PmcUnitEntity findPredecessor(LivingEntity owner, int targetIndex) {
      List<PmcUnitEntity> allies = this.mob
              .level()
              .getEntitiesOfClass(
                      PmcUnitEntity.class, this.mob.getBoundingBox().inflate(20.0), e -> e != this.mob && e.isOwnedBy((Player)owner) && e.getFormationIndex() == targetIndex
              );
      return !allies.isEmpty() ? allies.get(0) : null;
   }

   private void performMoveToPosition() {
      int myIndex = 0;
      if (this.mob instanceof PmcUnitEntity pmc) {
         myIndex = pmc.getFormationIndex();
      }

      if (myIndex == 0) {
         Vec3 finalTarget = this.commandable.getMoveToTarget();
         if (finalTarget == Vec3.ZERO) {
            return;
         }

         double distSqr = this.mob.distanceToSqr(finalTarget);
         if (distSqr < 2.5) {
            this.mob.getNavigation().stop();
         } else if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 20;
            this.mob.getNavigation().moveTo(finalTarget.x, finalTarget.y, finalTarget.z, this.speedModifier);
         }
      } else {
         PmcUnitEntity pointMan = this.findPointMan();
         if (pointMan != null) {
            OrderType moveFormation = OrderType.FORM_COLUMN;
            Vec3 targetPos = FormationUtils.getTargetPosition(pointMan.position(), pointMan.getForward(), moveFormation, myIndex);
            if (--this.timeToRecalcPath <= 0) {
               this.timeToRecalcPath = 10;
               this.mob.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
            }
         } else {
            Vec3 finalTarget = this.commandable.getMoveToTarget();
            if (--this.timeToRecalcPath <= 0) {
               this.timeToRecalcPath = 20;
               this.mob.getNavigation().moveTo(finalTarget.x, finalTarget.y, finalTarget.z, this.speedModifier);
            }
         }
      }
   }

   private PmcUnitEntity findPointMan() {
      for (PmcUnitEntity ally : this.mob.level().getEntitiesOfClass(PmcUnitEntity.class, this.mob.getBoundingBox().inflate(30.0))) {
         if (ally.getFormationIndex() == 0 && ally.isOwnedBy(this.mob.level().getPlayerByUUID(this.commandable.getOwnerUUID()))) {
            return ally;
         }
      }

      return null;
   }

   private boolean isPositionSafe(Vec3 pos) {
      BlockPos blockPos = BlockPos.containing(pos);
      boolean feetBlocked = this.isBlocked(blockPos);
      boolean headBlocked = this.isBlocked(blockPos.above());
      return !feetBlocked && !headBlocked;
   }

   private boolean isBlocked(BlockPos pos) {
      BlockState state = this.mob.level().getBlockState(pos);
      return state.isViewBlocking(this.mob.level(), pos) || state.isSolid();
   }
}