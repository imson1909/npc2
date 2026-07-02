package net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils;

import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.DefaultSquadLeaderGoals;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.DefaultSquadUnitGoals;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.DefaultUnitGoals;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.FriendlySquadLeaderGoals;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.FriendlySquadUnitGoals;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.FriendlyUnitGoals;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.IRoleGoals;

public enum UnitRole {
   DEFAULT(new DefaultUnitGoals()),
   SQUAD_LEADER(new DefaultSquadLeaderGoals()),
   SQUAD_UNIT(new DefaultSquadUnitGoals()),
   FRIENDLY_DEFAULT(new FriendlyUnitGoals()),
   FRIENDLY_SQUAD_LEADER(new FriendlySquadLeaderGoals()),
   FRIENDLY_SQUAD_UNIT(new FriendlySquadUnitGoals());

   private final IRoleGoals goals;

   UnitRole(IRoleGoals goals) {
      this.goals = goals;
   }

   public IRoleGoals getGoals() {
      return this.goals;
   }
}
