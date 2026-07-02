package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils.SquadData;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils.TerrainScanner;

public class SquadUnitHandshakeFollowGoal extends Goal {
   private final PathfinderMob unit;
   private final double movementSpeed;
   private final int maxLeaderDistance;
   private PathfinderMob squadLeader;
   private TerrainScanner.FormationType currentFormation;
   private Vec3 targetPosition;
   private int unitIndex;
   private int validationTimer;
   private final int validationInterval = 40;
   private int pathRecalcDelay = 0;
   private Vec3 lastTargetPos = Vec3.ZERO;

   public SquadUnitHandshakeFollowGoal(PathfinderMob unit, double movementSpeed, int maxLeaderDistance) {
      this.unit = unit;
      this.movementSpeed = movementSpeed;
      this.maxLeaderDistance = maxLeaderDistance;
      this.setFlags(EnumSet.of(Flag.MOVE));
   }

   public boolean canUse() {
      return !this.unit.isUsingItem() && this.unit.getTarget() == null && SquadData.hasValidSquadData(this.unit) && this.findLeaderFromNBT() != null;
   }

   public boolean canContinueToUse() {
      return this.unit.isUsingItem() || this.unit.getTarget() != null ? false : this.squadLeader != null && this.squadLeader.isAlive();
   }

   public void start() {
      this.validationTimer = 0;
      this.loadSquadDataFromNBT();
   }

   public void tick() {
      this.validationTimer++;
      if (this.validationTimer >= 40) {
         if (!this.validateAndUpdateSquadData()) {
            return;
         }
         this.validationTimer = 0;
      }
      if (this.squadLeader != null && this.currentFormation != null) {
         this.calculateTargetPosition();
         this.moveToFormationPosition();
      }
   }

   public void stop() {
      this.targetPosition = null;
   }

   private PathfinderMob findLeaderFromNBT() {
      if (!SquadData.hasValidSquadData(this.unit)) {
         return null;
      }
      UUID leaderUUID = SquadData.getLeaderUUID(this.unit);
      if (leaderUUID == null) {
         return null;
      }
      if (this.unit.level() instanceof ServerLevel serverLevel
              && serverLevel.getEntity(leaderUUID) instanceof PathfinderMob mob
              && mob.getClass() == this.unit.getClass()) {
         return mob;
      }
      return null;
   }

   private void loadSquadDataFromNBT() {
      this.squadLeader = this.findLeaderFromNBT();
      if (this.squadLeader != null) {
         CompoundTag data = this.unit.getPersistentData();
         String formationName = data.getString("SquadFormation");
         try {
            this.currentFormation = TerrainScanner.FormationType.valueOf(formationName);
         } catch (IllegalArgumentException e) {
            this.currentFormation = TerrainScanner.FormationType.WEDGE;
         }
         this.unitIndex = data.getInt("UnitIndex");
      }
   }

   private boolean validateAndUpdateSquadData() {
      if (!SquadData.hasValidSquadData(this.unit)) {
         return false;
      }
      PathfinderMob newLeader = this.findLeaderFromNBT();
      if (newLeader == null) {
         SquadData.clearSquadData(this.unit);
         return false;
      }
      this.squadLeader = newLeader;
      this.loadSquadDataFromNBT();
      return true;
   }

   private void calculateTargetPosition() {
      if (this.squadLeader != null && this.currentFormation != null) {
         Vec3 leaderPos = this.squadLeader.position();
         Vec3 leaderLook = this.squadLeader.getForward();
         switch (this.currentFormation) {
            case COLUMN:
               this.targetPosition = this.calculateColumnPosition(leaderPos, leaderLook);
               break;
            case WEDGE:
               this.targetPosition = this.calculateWedgePosition(leaderPos, leaderLook);
               break;
            default:
               this.targetPosition = leaderPos;
         }
      }
   }

   private Vec3 calculateColumnPosition(Vec3 leaderPos, Vec3 leaderLook) {
      double spacing = 2.0;
      double behindDistance = 2.0 + this.unitIndex * spacing;
      Vec3 behind = leaderLook.scale(-behindDistance);
      return leaderPos.add(behind);
   }

   private Vec3 calculateWedgePosition(Vec3 leaderPos, Vec3 leaderLook) {
      double spacing = 2.5;
      double behindDistance = 1.5 + Math.abs(this.unitIndex) * 0.8;
      Vec3 right = new Vec3(-leaderLook.z, 0.0, leaderLook.x).normalize();
      double side = this.unitIndex % 2 == 0 ? 1.0 : -1.0;
      double sideDistance = spacing * (this.unitIndex / 2 + 1);
      Vec3 behind = leaderLook.scale(-behindDistance);
      Vec3 sideways = right.scale(side * sideDistance);
      return leaderPos.add(behind).add(sideways);
   }

   private void moveToFormationPosition() {
      if (this.targetPosition != null) {
         double distanceToTarget = this.unit.position().distanceTo(this.targetPosition);
         if (distanceToTarget <= 2.2) {
            if (!this.unit.getNavigation().isDone()) {
               this.unit.getNavigation().stop();
            }
         } else {
            boolean targetMovedSignificantly = this.lastTargetPos.distanceToSqr(this.targetPosition) > 1.0;
            if (this.pathRecalcDelay > 0 && !targetMovedSignificantly) {
               this.pathRecalcDelay--;
            } else {
               this.pathRecalcDelay = 10 + this.unit.getRandom().nextInt(10);
               this.lastTargetPos = this.targetPosition;
               this.unit.getNavigation().moveTo(this.targetPosition.x, this.targetPosition.y, this.targetPosition.z, this.movementSpeed);
            }
         }
      }
   }

   public Vec3 getTargetPosition() {
      return this.targetPosition;
   }

   public TerrainScanner.FormationType getCurrentFormation() {
      return this.currentFormation;
   }

   public PathfinderMob getSquadLeader() {
      return this.squadLeader;
   }
}