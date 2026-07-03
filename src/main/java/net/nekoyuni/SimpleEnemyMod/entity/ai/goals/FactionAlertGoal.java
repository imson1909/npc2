package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.FactionType;

public class FactionAlertGoal extends Goal {
    private final AbstractUnit unit;
    private final double alertRadius;
    private int cooldown = 0;
    private static final int COOLDOWN_TICKS = 10;

    public FactionAlertGoal(AbstractUnit unit, double alertRadius) {
        this.unit = unit;
        this.alertRadius = alertRadius;
        this.setFlags(EnumSet.noneOf(Flag.class));
    }

    @Override
    public boolean canUse() {
        if (cooldown-- > 0) return false;
        LivingEntity target = unit.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = unit.getTarget();
        if (target == null || !target.isAlive()) return;

        FactionType myFaction = unit.getFaction();
        if (myFaction == null) return;

        AABB searchBox = unit.getBoundingBox().inflate(alertRadius);
        List<AbstractUnit> allies = unit.level().getEntitiesOfClass(
                AbstractUnit.class,
                searchBox,
                e -> e != unit && e.getFaction() == myFaction && e.isAlive()
        );

        for (AbstractUnit ally : allies) {
            if (ally.getTarget() == null || !ally.getTarget().isAlive()) {
                ally.setTarget(target);
                ally.getEntityData().set(AbstractUnit.LAST_SEEN_TARGET_TIME, unit.level().getGameTime());
            }
        }

        cooldown = COOLDOWN_TICKS;
    }
}