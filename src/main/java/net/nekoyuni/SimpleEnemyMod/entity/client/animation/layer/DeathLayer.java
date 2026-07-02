package net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer;

import java.util.function.Function;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.condition.IAnimationCondition;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.AnimationPriority;
import org.jetbrains.annotations.Nullable;

public class DeathLayer extends AbstractAnimationLayer {
   private final Function<Entity, AnimationDefinition> variantSelector;
   private boolean hasStarted = false;
   private static final boolean isDebug = false;

   public DeathLayer(String name, AnimationState animationState, AnimationDefinition animationDefinition, @Nullable IAnimationCondition additionalCondition) {
      this(name, animationState, entity -> animationDefinition, additionalCondition);
   }

   public DeathLayer(
      String name, AnimationState animationState, Function<Entity, AnimationDefinition> variantSelector, @Nullable IAnimationCondition additionalCondition
   ) {
      super(name, animationState, null, AnimationPriority.CRITICAL, additionalCondition);
      this.variantSelector = variantSelector;
   }

   @Override
   public boolean canPlay(Entity entity, int tickCount) {
      if (this.hasStarted) {
         return true;
      } else if (entity instanceof LivingEntity livingEntity) {
         boolean isDying = livingEntity.isDeadOrDying();
         return isDying && this.condition != null ? this.condition.test(entity, tickCount) : isDying;
      } else {
         return false;
      }
   }

   @Override
   protected void onStart(Entity entity, int tickCount) {
      if (!this.hasStarted) {
         this.hasStarted = true;
         AnimationDefinition selectedVariant = this.variantSelector.apply(entity);
      }
   }

   @Override
   public void stop() {
   }

   @Override
   public AnimationPriority getPriority() {
      return AnimationPriority.CRITICAL;
   }
}
