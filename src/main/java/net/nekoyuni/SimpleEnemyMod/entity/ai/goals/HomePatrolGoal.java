package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class HomePatrolGoal extends Goal {
    private final PathfinderMob mob;
    private final double speedModifier;
    private final int patrolRadius;
    private final int verticalSearchRange;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int walkTimer;
    private int waitTimer;
    private int lookAroundTimer;
    private static final int WALK_MIN = 40;
    private static final int WALK_MAX = 100;
    private static final int WAIT_MIN = 60;
    private static final int WAIT_MAX = 160;
    private static final int LOOK_AROUND_DURATION = 30;

    public HomePatrolGoal(PathfinderMob mob, double speedModifier, int patrolRadius) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.patrolRadius = patrolRadius;
        this.verticalSearchRange = 7;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) return false;
        if (this.mob.isInWater()) return false;
        if (this.mob instanceof AbstractUnit unit) {
            if (unit.isTargetLostTimeoutExpired()) return false;
            if (unit.getHomePoint() == null) return false;
        }
        return this.waitTimer <= 0 && this.lookAroundTimer <= 0 && this.walkTimer <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) return false;
        if (this.mob.isInWater()) return false;
        return this.walkTimer > 0 || this.waitTimer > 0 || this.lookAroundTimer > 0;
    }

    @Override
    public void start() {
        Vec3 pos = this.getPosition();
        if (pos == null) {
            this.walkTimer = 0;
            this.waitTimer = WAIT_MIN;
            return;
        }
        this.targetX = pos.x;
        this.targetY = pos.y;
        this.targetZ = pos.z;
        this.walkTimer = WALK_MIN + this.mob.getRandom().nextInt(WALK_MAX - WALK_MIN);
        this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speedModifier);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        this.walkTimer = 0;
        this.waitTimer = 0;
        this.lookAroundTimer = 0;
    }

    @Override
    public void tick() {
        if (this.walkTimer > 0) {
            this.walkTimer--;
            if (this.walkTimer <= 0 || this.mob.getNavigation().isDone()) {
                this.mob.getNavigation().stop();
                this.walkTimer = 0;
                this.waitTimer = WAIT_MIN + this.mob.getRandom().nextInt(WAIT_MAX - WAIT_MIN);
            }
            return;
        }

        if (this.waitTimer > 0) {
            this.waitTimer--;
            this.mob.getNavigation().stop();
            if (this.waitTimer <= 0) {
                this.lookAroundTimer = LOOK_AROUND_DURATION;
            }
            return;
        }

        if (this.lookAroundTimer > 0) {
            this.lookAroundTimer--;
            this.mob.getNavigation().stop();
            if (this.mob.getRandom().nextFloat() < 0.15F) {
                float yaw = this.mob.getYRot() + (this.mob.getRandom().nextFloat() - 0.5F) * 120.0F;
                this.mob.setYRot(yaw);
                this.mob.yHeadRot = yaw;
                this.mob.yBodyRot = yaw;
            }
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        BlockPos home = null;
        if (this.mob instanceof AbstractUnit unit) home = unit.getHomePoint();
        if (home == null) home = this.mob.blockPosition();
        Vec3 homeCenter = Vec3.atCenterOf(home);
        Vec3 target = LandRandomPos.getPos(this.mob, this.patrolRadius, this.verticalSearchRange);
        if (target != null && target.distanceToSqr(homeCenter) <= (double)(this.patrolRadius * this.patrolRadius)) {
            return target;
        }
        Vec3 towardsHome = homeCenter.subtract(this.mob.position()).normalize().scale(this.patrolRadius * 0.5);
        return this.mob.position().add(towardsHome);
    }
}