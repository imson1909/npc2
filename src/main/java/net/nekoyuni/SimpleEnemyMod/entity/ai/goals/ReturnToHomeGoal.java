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
    private static final double HOME_REACHED_DIST_SQR = 9.0;
    private static final double MAX_CHASE_DIST_SQR = 2500.0;
    private int stuckTicks = 0;
    private BlockPos lastPos = BlockPos.ZERO;

    public ReturnToHomeGoal(AbstractUnit unit, double speed) {
        this.unit = unit;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos home = unit.getHomePoint();
        if (home == null) return false;

        LivingEntity target = unit.getTarget();
        boolean hasTarget = target != null && target.isAlive();

        if (hasTarget) {
            if (unit.distanceToSqr(Vec3.atCenterOf(home)) > MAX_CHASE_DIST_SQR) {
                return true;
            }
            if (unit.isTargetLostTimeoutExpired()) {
                return true;
            }
            return false;
        }

        return unit.isTargetLostTimeoutExpired() || unit.isTooFarFromHome();
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos home = unit.getHomePoint();
        if (home == null) return false;
        return unit.distanceToSqr(Vec3.atCenterOf(home)) > HOME_REACHED_DIST_SQR;
    }

    @Override
    public void start() {
        BlockPos home = unit.getHomePoint();
        if (home != null) {
            unit.getNavigation().moveTo(home.getX() + 0.5, home.getY(), home.getZ() + 0.5, speed);
        }
        stuckTicks = 0;
        lastPos = unit.blockPosition();
    }

    @Override
    public void tick() {
        BlockPos home = unit.getHomePoint();
        if (home == null) return;

        if (unit.tickCount % 20 == 0) {
            if (unit.blockPosition().equals(lastPos)) {
                stuckTicks++;
                if (stuckTicks > 3) {
                    unit.getNavigation().moveTo(home.getX() + 0.5, home.getY(), home.getZ() + 0.5, speed * 1.2);
                    stuckTicks = 0;
                }
            } else {
                stuckTicks = 0;
            }
            lastPos = unit.blockPosition();
        }
    }

    @Override
    public void stop() {
        unit.getNavigation().stop();
        unit.setSoldierState(net.nekoyuni.SimpleEnemyMod.entity.unit.util.SoldierState.IDLE);
    }
}