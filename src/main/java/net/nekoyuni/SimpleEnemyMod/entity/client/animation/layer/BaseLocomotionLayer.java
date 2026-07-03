package net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer;

import javax.annotation.Nullable;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.condition.IAnimationCondition;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.AnimationPriority;

public class BaseLocomotionLayer extends AbstractAnimationLayer {
   private final AnimationState idleState;
   private final AnimationState walkState;
   private final AnimationDefinition idleDefinition;
   private final AnimationDefinition walkDefinition;
   private int idleAnimationTimeout = 0;
   private final IAnimationCondition walkCondition;
   private BaseLocomotionLayer.LocomotionState currentState = BaseLocomotionLayer.LocomotionState.IDLE;
   private final boolean isDebug = false;

   public BaseLocomotionLayer(
      String name,
      AnimationState idleState,
      AnimationDefinition idleDefinition,
      AnimationState walkState,
      AnimationDefinition walkDefinition,
      AnimationPriority priority,
      @Nullable IAnimationCondition additionalCondition
   ) {
      super(name, idleState, idleDefinition, priority, additionalCondition);
      this.idleState = idleState;
      this.idleDefinition = idleDefinition;
      this.walkState = walkState;
      this.walkDefinition = walkDefinition;
      this.walkCondition = (entity, tick) -> entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
   }

   @Override
   public void play(Entity entity, int tickCount) {
      boolean shouldWalk = this.walkCondition.test(entity, tickCount);
      if (shouldWalk) {
         this.transitionToWalk(tickCount);
      } else {
         this.transitionToIdle(entity, tickCount);
      }
   }

   private void transitionToWalk(int tickCount) {
      if (this.currentState != BaseLocomotionLayer.LocomotionState.WALKING) {
         if (this.idleState.isStarted()) {
            this.idleState.stop();
         }

         this.walkState.stop();
         this.walkState.start(tickCount);
         this.currentState = BaseLocomotionLayer.LocomotionState.WALKING;
      }
   }

   private void transitionToIdle(Entity entity, int tickCount) {
      if (this.currentState != BaseLocomotionLayer.LocomotionState.IDLE) {
         if (this.walkState.isStarted()) {
            this.walkState.stop();
         }

         this.idleAnimationTimeout = 0;
         this.currentState = BaseLocomotionLayer.LocomotionState.IDLE;
      }

      if (this.idleAnimationTimeout <= 0) {
         if (!this.idleState.isStarted()) {
            this.idleState.stop();
            this.idleState.start(tickCount);
         }

         this.idleAnimationTimeout = entity.level().getRandom().nextInt(40) + 80;
      } else {
         this.idleAnimationTimeout--;
      }
   }

   @Override
   public void stop() {
      if (this.idleState.isStarted()) {
         this.idleState.stop();
      }

      if (this.walkState.isStarted()) {
         this.walkState.stop();
      }

      this.currentState = BaseLocomotionLayer.LocomotionState.IDLE;
      this.idleAnimationTimeout = 0;
   }

   @Override
   protected void onStart(Entity entity, int tickCount) {
      boolean shouldWalk = entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
      if (shouldWalk) {
         this.currentState = BaseLocomotionLayer.LocomotionState.WALKING;
         this.walkState.stop();
         this.walkState.start(tickCount);
      } else {
         this.currentState = BaseLocomotionLayer.LocomotionState.IDLE;
         this.idleAnimationTimeout = 0;
         this.idleState.stop();
         this.idleState.start(tickCount);
      }
   }

   @Override
   protected void onStop() {
      if (this.idleState.isStarted()) {
         this.idleState.stop();
      }

      if (this.walkState.isStarted()) {
         this.walkState.stop();
      }
   }

   @Override
   public boolean isPlaying() {
      return this.idleState.isStarted() || this.walkState.isStarted();
   }

   public boolean isWalking() {
      return this.currentState == BaseLocomotionLayer.LocomotionState.WALKING;
   }

   public boolean isIdle() {
      return this.currentState == BaseLocomotionLayer.LocomotionState.IDLE;
   }

   private enum LocomotionState {
      IDLE,
      WALKING;
   }
}
