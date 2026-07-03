package net.nekoyuni.SimpleEnemyMod.entity.ai.roles;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SquadUnitHandshakeFollowGoal;

public class HostileSquadUnitGoals implements IRoleGoals {
    @Override
    public void addGoals(PathfinderMob entity) {
        entity.goalSelector.addGoal(2, new SquadUnitHandshakeFollowGoal(entity, 1.1, 15));
        entity.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(entity, 1.0));

        entity.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(entity, Player.class, true));
        entity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(entity, Monster.class, true));
    }
}