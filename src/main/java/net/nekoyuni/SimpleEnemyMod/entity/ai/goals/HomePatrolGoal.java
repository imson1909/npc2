package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class HomePatrolGoal extends WaterAvoidingRandomStrollGoal {
    private final AbstractUnit unit;
    private final double homeRadius;

    public HomePatrolGoal(AbstractUnit unit, double speed, double homeRadius) {
        super(unit, speed);
        this.unit = unit;
        this.homeRadius = homeRadius;
    }

    @Override
    public boolean canUse() {
        if (unit.getTarget() != null && unit.getTarget().isAlive()) {
            return false;
        }
        BlockPos home = unit.getHomePoint();
        if (home == null) return super.canUse();
        if (unit.distanceToSqr(Vec3.atCenterOf(home)) > homeRadius * homeRadius) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (unit.getTarget() != null && unit.getTarget().isAlive()) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        BlockPos home = unit.getHomePoint();
        if (home == null) return super.getPosition();

        for (int i = 0; i < 15; i++) {
            double angle = unit.getRandom().nextDouble() * Math.PI * 2;
            double dist = unit.getRandom().nextDouble() * homeRadius * 0.8;
            int x = home.getX() + (int)(Math.cos(angle) * dist);
            int z = home.getZ() + (int)(Math.sin(angle) * dist);
            int y = unit.level().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

            Vec3 pos = new Vec3(x + 0.5, y, z + 0.5);
            if (unit.distanceToSqr(pos) >= 4.0 && unit.distanceToSqr(pos) <= homeRadius * homeRadius) {
                return pos;
            }
        }
        return super.getPosition();
    }
}