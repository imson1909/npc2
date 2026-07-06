package net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PathfinderMob;

public class SquadData {
   public static final String SQUAD_LEADER_UUID = "SquadLeaderUUID";
   public static final String SQUAD_FORMATION = "SquadFormation";
   public static final String UNIT_INDEX = "UnitIndex";
   public static final String FORMATION_TIMESTAMP = "FormationTimestamp";
   public static final String HANDSHAKE_COMPLETE = "HandshakeComplete";
   private static final String SQUAD_LEADER_ID = "SquadLeaderID";

   public static void setSquadData(PathfinderMob unit, PathfinderMob leader, TerrainScanner.FormationType formation, int unitIndex) {
      CompoundTag data = unit.getPersistentData();
      data.putUUID("SquadLeaderUUID", leader.getUUID());
      data.putString("SquadFormation", formation.name());
      data.putInt("UnitIndex", unitIndex);
      data.putLong("FormationTimestamp", unit.level().getGameTime());
      data.putBoolean("HandshakeComplete", true);
   }

   public static UUID getLeaderUUID(PathfinderMob unit) {
      CompoundTag data = unit.getPersistentData();
      return data.hasUUID("SquadLeaderUUID") ? data.getUUID("SquadLeaderUUID") : null;
   }

   public static boolean hasValidSquadData(PathfinderMob unit) {
      CompoundTag data = unit.getPersistentData();
      return data.hasUUID("SquadLeaderUUID") && data.contains("SquadFormation") && data.getBoolean("HandshakeComplete");
   }

   public static void clearSquadData(PathfinderMob unit) {
      CompoundTag data = unit.getPersistentData();
      data.remove("SquadLeaderID");
      data.remove("SquadLeaderUUID");
      data.remove("SquadFormation");
      data.remove("UnitIndex");
      data.remove("FormationTimestamp");
      data.remove("HandshakeComplete");
   }
}
