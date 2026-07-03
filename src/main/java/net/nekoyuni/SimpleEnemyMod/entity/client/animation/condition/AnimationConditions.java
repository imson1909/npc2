package net.nekoyuni.SimpleEnemyMod.entity.client.animation.condition;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AnimationConditions {
   public static final IAnimationCondition ALWAYS = (entity, tick) -> true;
   public static final IAnimationCondition NEVER = (entity, tick) -> false;
   public static final IAnimationCondition IS_MOVING = (entity, tick) -> entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
   public static final IAnimationCondition IS_STATIONARY = IS_MOVING.negate();
   public static final IAnimationCondition IS_DEAD_OR_DYING = (entity, tick) -> entity instanceof LivingEntity livingEntity
      ? livingEntity.isDeadOrDying()
      : !entity.isAlive();
   public static final IAnimationCondition IS_ALIVE = IS_DEAD_OR_DYING.negate();
   public static final IAnimationCondition IS_ON_GROUND = (entity, tick) -> entity.onGround();
   public static final IAnimationCondition IS_IN_AIR = IS_ON_GROUND.negate();

   private AnimationConditions() {
   }

   public static IAnimationCondition ticksSince(int minTicks, AnimationConditions.TickProvider tickProvider) {
      return (entity, currentTick) -> {
         int lastTick = tickProvider.getLastTick(entity);
         return currentTick - lastTick >= minTicks;
      };
   }

   public static IAnimationCondition valueChanged(final AnimationConditions.ValueProvider provider) {
      return new IAnimationCondition() {
         private int lastValue = -1;

         @Override
         public boolean test(Entity entity, int tickCount) {
            int currentValue = provider.getValue(entity);
            boolean changed = currentValue != this.lastValue;
            this.lastValue = currentValue;
            return changed;
         }
      };
   }

   @FunctionalInterface
   public interface TickProvider {
      int getLastTick(Entity var1);
   }

   @FunctionalInterface
   public interface ValueProvider {
      int getValue(Entity var1);
   }
}
