package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.phys.AABB;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils.SquadData;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils.TerrainScanner;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class SquadLeaderHandshakeGoal extends Goal {
   private final PathfinderMob leader;
   private final int scanRange;
   private final int squadSearchRange;
   private TerrainScanner.FormationType currentFormation;
   private TerrainScanner.FormationType lastFormation;
   private final Map<Integer, Integer> assignedUnits = new HashMap<>();

   public SquadLeaderHandshakeGoal(PathfinderMob leader, int scanRange, int squadSearchRange) {
      this.leader = leader;
      this.scanRange = scanRange;
      this.squadSearchRange = squadSearchRange;
      this.currentFormation = TerrainScanner.FormationType.WEDGE;
      this.lastFormation = null;
      this.setFlags(EnumSet.of(Flag.TARGET));
   }

   public boolean canUse() {
      if (this.leader instanceof AbstractUnit unit && unit.isTooFarFromHome()) {
         return false;
      }
      return !this.leader.isUsingItem() && this.leader.getTarget() == null;
   }

   public boolean canContinueToUse() {
      if (this.leader instanceof AbstractUnit unit && unit.isTooFarFromHome()) {
         return false;
      }
      return !this.leader.isUsingItem() && this.leader.getTarget() == null;
   }

   public void start() {
      this.lastFormation = null;
      this.performImmediateHandshake();
   }

   public void tick() {
      int offset = this.leader.getId();
      if ((this.leader.tickCount + offset) % 20 == 0) {
         this.performHandshakeWithNearbyUnits();
         this.updateNearbyAssignedUnits();
      }
      if ((this.leader.tickCount + offset) % 60 == 0) {
         this.performTerrainScan();
         if (this.currentFormation != this.lastFormation) {
            this.updateAllAssignedUnits();
            this.lastFormation = this.currentFormation;
         }
      }
   }

   private void updateNearbyAssignedUnits() {
      AABB searchBox = new AABB(this.leader.blockPosition()).inflate(this.squadSearchRange);
      for (PathfinderMob unit : this.leader.level().getEntitiesOfClass(PathfinderMob.class, searchBox, entity -> {
         UUID storedLeaderUUID = SquadData.getLeaderUUID(entity);
         return this.assignedUnits.containsKey(entity.getId()) && storedLeaderUUID != null && storedLeaderUUID.equals(this.leader.getUUID());
      })) {
         String currentUnitFormation = unit.getPersistentData().getString("SquadFormation");
         if (!currentUnitFormation.equals(this.currentFormation.name())) {
            int unitIndex = this.assignedUnits.get(unit.getId());
            SquadData.setSquadData(unit, this.leader, this.currentFormation, unitIndex);
         }
      }
   }

   private void performImmediateHandshake() {
      for (PathfinderMob unit : this.findNearbyUnassignedUnits()) {
         this.performHandshakeWithUnit(unit);
      }
   }

   private void performHandshakeWithNearbyUnits() {
      for (PathfinderMob unit : this.findNearbyUnassignedUnits()) {
         if (this.canAcceptUnit(unit)) {
            this.performHandshakeWithUnit(unit);
         }
      }
      this.cleanupDistantUnits();
   }

   private void performHandshakeWithUnit(PathfinderMob unit) {
      int unitIndex = this.assignedUnits.size();
      SquadData.setSquadData(unit, this.leader, this.currentFormation, unitIndex);
      this.assignedUnits.put(unit.getId(), unitIndex);
   }

   private List<PathfinderMob> findNearbyUnassignedUnits() {
      AABB searchBox = new AABB(this.leader.blockPosition()).inflate(this.squadSearchRange);
      return this.leader.level().getEntitiesOfClass(
              PathfinderMob.class,
              searchBox,
              entity -> entity.getClass() == this.leader.getClass() && !SquadData.hasValidSquadData(entity)
      );
   }

   private boolean canAcceptUnit(PathfinderMob unit) {
      return this.assignedUnits.size() < 8;
   }

   private void updateAllAssignedUnits() {
      for (PathfinderMob unit : this.getAssignedUnits()) {
         int unitIndex = this.assignedUnits.get(unit.getId());
         SquadData.setSquadData(unit, this.leader, this.currentFormation, unitIndex);
      }
   }

   private List<PathfinderMob> getAssignedUnits() {
      AABB searchBox = new AABB(this.leader.blockPosition()).inflate(this.squadSearchRange * 2);
      return this.leader.level().getEntitiesOfClass(PathfinderMob.class, searchBox, entity -> {
         UUID storedLeaderUUID = SquadData.getLeaderUUID(entity);
         return this.assignedUnits.containsKey(entity.getId()) && storedLeaderUUID != null && storedLeaderUUID.equals(this.leader.getUUID());
      });
   }

   private void cleanupDistantUnits() {
      List<Integer> toRemove = new ArrayList<>();
      for (Integer unitId : this.assignedUnits.keySet()) {
         Entity entity = this.leader.level().getEntity(unitId);
         if (entity == null || !entity.isAlive() || this.leader.distanceTo(entity) > this.squadSearchRange * this.squadSearchRange * 4) {
            toRemove.add(unitId);
            if (entity instanceof PathfinderMob mob) {
               SquadData.clearSquadData(mob);
            }
         }
      }
      toRemove.forEach(this.assignedUnits::remove);
      if (!toRemove.isEmpty()) {
         this.reorganizeUnitIndices();
      }
   }

   private void reorganizeUnitIndices() {
      List<PathfinderMob> currentUnits = this.getAssignedUnits();
      this.assignedUnits.clear();
      for (int i = 0; i < currentUnits.size(); i++) {
         PathfinderMob unit = currentUnits.get(i);
         this.assignedUnits.put(unit.getId(), i);
         SquadData.setSquadData(unit, this.leader, this.currentFormation, i);
      }
   }

   private void performTerrainScan() {
      BlockPos leaderPos = this.leader.blockPosition();
      TerrainScanner.FormationType newFormation = TerrainScanner.getFormationType(this.leader.level(), leaderPos, this.scanRange);
      this.currentFormation = newFormation;
   }

   public TerrainScanner.FormationType getCurrentFormation() {
      return this.currentFormation;
   }

   public int getSquadSize() {
      return this.assignedUnits.size();
   }
}