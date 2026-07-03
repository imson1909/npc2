package net.nekoyuni.SimpleEnemyMod.entity.ai.roles;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SquadUnitHandshakeFollowGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.ReturnToHomeGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.FactionAlertGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.HomePatrolGoal;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class HostileSquadUnitGoals implements IRoleGoals {
    @Override
    public void addGoals(PathfinderMob entity) {
        AbstractUnit unit = (AbstractUnit) entity;

        // Priority 0: Return home if target lost or too far
        entity.goalSelector.addGoal(0, new ReturnToHomeGoal(unit, 1.1));

        // Priority 1: Alert faction allies when target spotted
        entity.goalSelector.addGoal(1, new FactionAlertGoal(unit, 64.0));

        entity.goalSelector.addGoal(2, new SquadUnitHandshakeFollowGoal(entity, 1.1, 15));
        // Priority 7: Patrol near home when idle (smaller radius for units)
        entity.goalSelector.addGoal(7, new HomePatrolGoal(unit, 0.6, 12.0));
        entity.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(entity, 1.0));

        entity.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(entity, Player.class, true));
        entity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(entity, Monster.class, true));
    }
}