package net.nekoyuni.SimpleEnemyMod.entity.client.animation.config;

import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.condition.IAnimationCondition;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.AnimationPriority;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.LayeredAnimationManager;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer.ActionLayer;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer.BaseLocomotionLayer;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer.DeathLayer;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural.IProceduralLayer;

public class AnimationConfig {
   public static AnimationConfig.Builder builder() {
      return new AnimationConfig.Builder();
   }

   public static class ActionLayerBuilder {
      private final AnimationConfig.Builder parentBuilder;
      private final String name;
      private final AnimationState animationState;
      private final AnimationDefinition[] animationVariants;
      private AnimationPriority priority = AnimationPriority.MEDIUM;
      private float durationSeconds = 1.0F;
      private float speedFactor = 1.0F;
      private ActionLayer.TriggerDetector triggerDetector = null;
      private IAnimationCondition condition = null;

      private ActionLayerBuilder(AnimationConfig.Builder parent, String name, AnimationState state, AnimationDefinition[] variants) {
         this.parentBuilder = parent;
         this.name = name;
         this.animationState = state;
         this.animationVariants = variants;
      }

      public AnimationConfig.ActionLayerBuilder speed(float factor) {
         this.speedFactor = factor;
         return this;
      }

      public AnimationConfig.ActionLayerBuilder priority(AnimationPriority priority) {
         this.priority = priority;
         return this;
      }

      public AnimationConfig.ActionLayerBuilder duration(float seconds) {
         this.durationSeconds = seconds;
         return this;
      }

      public AnimationConfig.ActionLayerBuilder triggerOn(ActionLayer.TriggerDetector detector) {
         this.triggerDetector = detector;
         return this;
      }

      public AnimationConfig.ActionLayerBuilder condition(IAnimationCondition condition) {
         this.condition = condition;
         return this;
      }

      public AnimationConfig.Builder build() {
         if (this.triggerDetector == null) {
            throw new IllegalStateException("ActionLayer '" + this.name + "' needs a trigger detector");
         }

         ActionLayer layer = new ActionLayer(
            this.name, this.animationState, this.animationVariants, this.priority, this.durationSeconds, this.triggerDetector, this.condition
         );
         this.parentBuilder.managerBuilder.addAnimationLayer(layer);
         return this.parentBuilder;
      }
   }

   public static class Builder {
      private final LayeredAnimationManager.Builder managerBuilder = LayeredAnimationManager.builder();

      public AnimationConfig.Builder addLocomotionLayer(
         String name,
         AnimationState idleState,
         AnimationDefinition idleDefinition,
         AnimationState walkState,
         AnimationDefinition walkDefinition,
         AnimationPriority priority
      ) {
         return this.addLocomotionLayer(name, idleState, idleDefinition, walkState, walkDefinition, priority, null);
      }

      public AnimationConfig.Builder addLocomotionLayer(
         String name,
         AnimationState idleState,
         AnimationDefinition idleDefinition,
         AnimationState walkState,
         AnimationDefinition walkDefinition,
         AnimationPriority priority,
         @Nullable IAnimationCondition condition
      ) {
         BaseLocomotionLayer layer = new BaseLocomotionLayer(name, idleState, idleDefinition, walkState, walkDefinition, priority, condition);
         this.managerBuilder.addAnimationLayer(layer);
         return this;
      }

      public AnimationConfig.ActionLayerBuilder addActionLayer(String name, AnimationState animationState, AnimationDefinition animationDefinition) {
         return new AnimationConfig.ActionLayerBuilder(this, name, animationState, new AnimationDefinition[]{animationDefinition});
      }

      public AnimationConfig.ActionLayerBuilder addActionLayer(String name, AnimationState animationState, AnimationDefinition[] animationVariants) {
         return new AnimationConfig.ActionLayerBuilder(this, name, animationState, animationVariants);
      }

      public AnimationConfig.Builder addDeathLayer(String name, AnimationState animationState, AnimationDefinition animationDefinition) {
         return this.addDeathLayer(name, animationState, animationDefinition, null);
      }

      public AnimationConfig.Builder addDeathLayer(
         String name, AnimationState animationState, AnimationDefinition animationDefinition, @Nullable IAnimationCondition condition
      ) {
         DeathLayer layer = new DeathLayer(name, animationState, animationDefinition, condition);
         this.managerBuilder.addAnimationLayer(layer);
         return this;
      }

      public AnimationConfig.Builder addDeathLayer(String name, AnimationState animationState, Function<Entity, AnimationDefinition> variantSelector) {
         return this.addDeathLayer(name, animationState, variantSelector, null);
      }

      public AnimationConfig.Builder addDeathLayer(
         String name, AnimationState animationState, Function<Entity, AnimationDefinition> variantSelector, @Nullable IAnimationCondition condition
      ) {
         DeathLayer layer = new DeathLayer(name, animationState, variantSelector, condition);
         this.managerBuilder.addAnimationLayer(layer);
         return this;
      }

      public AnimationConfig.Builder addProceduralLayer(IProceduralLayer layer) {
         this.managerBuilder.addProceduralLayer(layer);
         return this;
      }

      public LayeredAnimationManager build() {
         return this.managerBuilder.build();
      }
   }
}
