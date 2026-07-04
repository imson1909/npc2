package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class ReturnToHomeGoal extends Goal {
    private final AbstractUnit unit;
    private final double speed;
    private int pathRecalcDelay;
    private static final double HOME_REACHED_DIST_SQR = 9.0;
    private static final double MAX_CHASE_DIST_SQR = 225.0;

    public ReturnToHomeGoal(AbstractUnit unit, double speed) {
        this.unit = unit;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        BlockPos home = unit.getHomePoint();
        if (home == null) return false;
        return unit.isTooFarFromHome() || unit.isTargetLostTimeoutExpired();
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos home = unit.getHomePoint();
        if (home == null) return false;
        return unit.distanceToSqr(Vec3.atCenterOf(home)) > HOME_REACHED_DIST_SQR;
    }

    @Override
    public void start() {
        this.pathRecalcDelay = 0;
        unit.setTarget(null);
    }

    @Override
    public void tick() {
        BlockPos home = unit.getHomePoint();
        if (home == null) return;

        unit.getLookControl().setLookAt(home.getX() + 0.5, home.getY(), home.getZ() + 0.5, 30.0F, 30.0F);

        if (--this.pathRecalcDelay <= 0) {
            this.pathRecalcDelay = 10;
            unit.getNavigation().moveTo(home.getX() + 0.5, home.getY(), home.getZ() + 0.5, speed);
        }
    }

    @Override
    public void stop() {
        unit.getNavigation().stop();
    }
}