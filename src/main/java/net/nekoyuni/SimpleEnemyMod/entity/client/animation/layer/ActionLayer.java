package net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer;

import javax.annotation.Nullable;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.condition.IAnimationCondition;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.AnimationPriority;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.IAnimatedEntity;

public class ActionLayer extends AbstractAnimationLayer {
   private final AnimationDefinition[] animationVariants;
   private int currentVariantIndex = 0;
   private final float configDurationSeconds;
   private int durationTicks;
   private int ticksRemaining = 0;
   private int lastTickSeen = -1;
   private int lastTriggerValue = 0;
   private boolean waitingForReset = false;
   private final ActionLayer.TriggerDetector triggerDetector;
   private final boolean isDebug = false;

   public ActionLayer(
      String name,
      AnimationState animationState,
      AnimationDefinition animationDefinition,
      AnimationPriority priority,
      float durationSeconds,
      ActionLayer.TriggerDetector triggerDetector,
      @Nullable IAnimationCondition additionalCondition
   ) {
      this(name, animationState, new AnimationDefinition[]{animationDefinition}, priority, durationSeconds, triggerDetector, additionalCondition);
   }

   public ActionLayer(
      String name,
      AnimationState animationState,
      AnimationDefinition[] animationVariants,
      AnimationPriority priority,
      float durationSeconds,
      ActionLayer.TriggerDetector triggerDetector,
      @Nullable IAnimationCondition additionalCondition
   ) {
      super(name, animationState, animationVariants[0], priority, additionalCondition);
      this.animationVariants = animationVariants;
      this.configDurationSeconds = durationSeconds;
      this.triggerDetector = triggerDetector;
   }

   @Override
   public boolean canPlay(Entity entity, int tickCount) {
      int currentTriggerValue = this.triggerDetector.getCurrentValue(entity);
      if (currentTriggerValue == 0) {
         this.waitingForReset = false;
         this.lastTriggerValue = 0;
         return false;
      }

      if (this.waitingForReset) {
         if (currentTriggerValue <= this.lastTriggerValue) {
            this.lastTriggerValue = currentTriggerValue;
            return false;
         }

         this.waitingForReset = false;
      }

      boolean isNewTrigger = currentTriggerValue > this.lastTriggerValue;
      this.lastTriggerValue = currentTriggerValue;
      return isNewTrigger ? true : this.isPlaying() && this.ticksRemaining > 0;
   }

   @Override
   protected void onStart(Entity entity, int tickCount) {
      if (this.animationVariants.length > 1) {
         this.currentVariantIndex = entity.level().getRandom().nextInt(this.animationVariants.length);
         if (entity instanceof IAnimatedEntity animatedEntity) {
            animatedEntity.onAnimationVariantSelected(this.getName(), this.currentVariantIndex);
         }
      } else {
         this.currentVariantIndex = 0;
      }

      AnimationDefinition selectedVariant = this.animationVariants[this.currentVariantIndex];
      int animDurationTicks = (int)(selectedVariant.lengthInSeconds() * 20.0F);
      int configDurationTicks = (int)(this.configDurationSeconds * 20.0F);
      this.durationTicks = Math.max(animDurationTicks, configDurationTicks);
      if (this.durationTicks <= 0) {
         this.durationTicks = 1;
      }

      this.ticksRemaining = this.durationTicks;
   }

   @Override
   protected void onUpdate(Entity entity, int tickCount) {
      if (tickCount != this.lastTickSeen) {
         if (this.ticksRemaining > 0) {
            this.ticksRemaining--;
         }

         this.lastTickSeen = tickCount;
      }
   }

   @Override
   protected void onStop() {
      this.ticksRemaining = 0;
      this.waitingForReset = true;
      this.lastTickSeen = -1;
   }

   @Override
   public boolean isPlaying() {
      return super.isPlaying() && this.ticksRemaining > 0;
   }

   public AnimationDefinition getCurrentVariant() {
      return this.animationVariants[this.currentVariantIndex];
   }

   @FunctionalInterface
   public interface TriggerDetector {
      int getCurrentValue(Entity var1);
   }
}
