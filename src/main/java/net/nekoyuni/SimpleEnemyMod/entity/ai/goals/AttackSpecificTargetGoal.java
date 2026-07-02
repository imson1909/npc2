package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.nekoyuni.SimpleEnemyMod.entity.ai.orders.OrderType;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;

public class AttackSpecificTargetGoal extends Goal {
   private final PmcUnitEntity mob;

   public AttackSpecificTargetGoal(PmcUnitEntity mob) {
      this.mob = mob;
      this.setFlags(EnumSet.of(Flag.TARGET));
   }

   public boolean canUse() {
      return this.mob.getOrder() == OrderType.ATTACK_THAT_TARGET && this.mob.getAttackTargetId() != -1;
   }

   public void start() {
      this.forceUpdateTarget();
   }

   public void tick() {
      if (this.mob.tickCount % 5 == 0) {
         this.forceUpdateTarget();
      }
   }

   private void forceUpdateTarget() {
      Entity target = this.mob.level().getEntity(this.mob.getAttackTargetId());
      if (target instanceof LivingEntity living && target.isAlive()) {
         this.mob.setTarget(living);
      } else {
         this.stop();
      }
   }

   public void stop() {
      this.mob.setOrder(OrderType.FREE_FIRE);
      this.mob.setAttackTargetId(-1);
      this.mob.setTarget(null);
   }
}
