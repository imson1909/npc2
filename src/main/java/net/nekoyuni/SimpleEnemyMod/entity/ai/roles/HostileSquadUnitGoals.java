package net.nekoyuni.SimpleEnemyMod.entity.ai.roles;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.*;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class HostileSquadUnitGoals implements IRoleGoals {
    @Override
    public void addGoals(PathfinderMob entity) {
        AbstractUnit unit = (AbstractUnit) entity;
        entity.goalSelector.addGoal(0, new ReturnToHomeGoal(unit, 1.1));
        entity.goalSelector.addGoal(1, new FactionAlertGoal(unit, 64.0));
        entity.goalSelector.addGoal(7, new HomePatrolGoal(unit, 0.6, 12));
        entity.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(entity, 1.0));

        Predicate<LivingEntity> notUnit = e -> !(e instanceof AbstractUnit);
        entity.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(entity, Player.class, true, notUnit));
        entity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(entity, Monster.class, true, notUnit));
    }
}