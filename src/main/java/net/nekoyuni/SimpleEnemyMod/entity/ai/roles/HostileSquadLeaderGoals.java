package net.nekoyuni.SimpleEnemyMod.entity.ai.roles;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.*;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class HostileSquadLeaderGoals implements IRoleGoals {
    @Override
    public void addGoals(PathfinderMob entity) {
        AbstractUnit unit = (AbstractUnit) entity;
        entity.goalSelector.addGoal(0, new ReturnToHomeGoal(unit, 1.2));
        entity.goalSelector.addGoal(0, new FloatGoal(entity));
        entity.goalSelector.addGoal(1, new FactionAlertGoal(unit, 64.0));
        entity.goalSelector.addGoal(2, new SmartSquadDoorGoal(entity, 6.0));
        entity.goalSelector.addGoal(3, new SeekCoverGoal(unit, 1.1, 13));
        entity.goalSelector.addGoal(4, new MoveToAttackRangeGoal(entity, 96.0, 88.0, 1.2));
        entity.goalSelector.addGoal(5, new PeekFromCoverGoal(unit, 1.1));
        entity.goalSelector.addGoal(6, new TacticalManeuverGoal(unit));
        entity.goalSelector.addGoal(7, new RangedGunAttackGoal(
                entity,
                ((Double)AiShootingConfig.MAX_SHOOT_DISTANCE.get()).floatValue(),
                ((Double)AiShootingConfig.BASE_SPREAD.get()).floatValue(),
                ((Double)AiShootingConfig.SPREAD_INCREASE.get()).floatValue(),
                (Integer)AiShootingConfig.MIN_BURST.get(),
                (Integer)AiShootingConfig.MAX_BURST.get(),
                (Integer)AiShootingConfig.MIN_BURST_COOLDOWN.get(),
                (Integer)AiShootingConfig.MAX_BURST_COOLDOWN.get()
        ));
        entity.goalSelector.addGoal(8, new HomePatrolGoal(unit, 0.8, 15));
        entity.goalSelector.addGoal(9, new LookAtPlayerGoal(entity, Player.class, 8.0F));
        entity.goalSelector.addGoal(10, new RandomLookAroundGoal(entity));

        Predicate<LivingEntity> notUnit = e -> !(e instanceof AbstractUnit);
        entity.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(entity, Player.class, true, notUnit));
        entity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(entity, Monster.class, true, notUnit));
    }
}