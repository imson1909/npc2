package net.nekoyuni.SimpleEnemyMod.entity.client.animation.condition;

import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface IAnimationCondition {
   boolean test(Entity var1, int var2);

   default IAnimationCondition and(IAnimationCondition other) {
      return (entity, tick) -> this.test(entity, tick) && other.test(entity, tick);
   }

   default IAnimationCondition or(IAnimationCondition other) {
      return (entity, tick) -> this.test(entity, tick) || other.test(entity, tick);
   }

   default IAnimationCondition negate() {
      return (entity, tick) -> !this.test(entity, tick);
   }
}
