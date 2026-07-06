package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.FactionType;

public class FactionAwareTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final double verticalRange;
    private final AbstractUnit unit;

    public FactionAwareTargetGoal(AbstractUnit unit, Class<T> targetClass, boolean mustSee, double followRange) {
        super(unit, targetClass, 10, mustSee, false, createFactionPredicate(unit, targetClass));
        this.unit = unit;
        this.verticalRange = 32.0;
    }

    @Nullable
    private static <T extends LivingEntity> Predicate<LivingEntity> createFactionPredicate(AbstractUnit unit, Class<T> targetClass) {
        if (targetClass == Player.class) {
            return target -> {
                if (!(target instanceof Player)) return false;
                if (target instanceof AbstractUnit) return false;
                if (target.getClass() == unit.getClass()) return false;
                return true;
            };
        }
        if (Monster.class.isAssignableFrom(targetClass)) {
            return target -> {
                if (target instanceof AbstractUnit otherUnit) {
                    return otherUnit.getFaction() != unit.getFaction();
                }
                return target instanceof Monster;
            };
        }
        return null;
    }

    @Override
    protected AABB getTargetSearchArea(double followRange) {
        return this.mob.getBoundingBox().inflate(followRange, this.verticalRange, followRange);
    }

    @Override
    public void start() {
        super.start();
        if (this.target != null && this.target.isAlive()) {
            alertFaction();
        }
    }

    @Override
    public void tick() {
        super.tick();
        // Periodically re-alert faction if we still have a valid target
        if (this.target != null && this.target.isAlive() && this.unit.tickCount % 20 == 0) {
            alertFaction();
        }
    }

    private void alertFaction() {
        if (this.unit.level().isClientSide()) return;
        if (this.target == null || !this.target.isAlive()) return;

        FactionType myFaction = this.unit.getFaction();
        AABB alertBox = this.unit.getBoundingBox().inflate(64.0, 32.0, 64.0);
        for (Mob ally : this.unit.level().getEntitiesOfClass(Mob.class, alertBox)) {
            if (ally instanceof AbstractUnit allyUnit && allyUnit.getFaction() == myFaction) {
                if (allyUnit.getTarget() == null || !allyUnit.getTarget().isAlive()) {
                    allyUnit.setTarget(this.target);
                }
            }
        }
    }
}