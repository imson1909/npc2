package net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer;

import javax.annotation.Nullable;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.condition.IAnimationCondition;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.AnimationPriority;

public abstract class AbstractAnimationLayer implements IAnimationLayer {
   protected final String name;
   protected final AnimationState animationState;
   protected final AnimationDefinition animationDefinition;
   protected final AnimationPriority priority;
   protected final IAnimationCondition condition;
   private boolean isCurrentPlaying = false;
   private int startTick = -1;

   public AbstractAnimationLayer(
      String name, AnimationState animationState, AnimationDefinition animationDefinition, AnimationPriority priority, @Nullable IAnimationCondition condition
   ) {
      this.name = name;
      this.animationState = animationState;
      this.animationDefinition = animationDefinition;
      this.priority = priority;
      this.condition = condition != null ? condition : (entity, tick) -> true;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public AnimationPriority getPriority() {
      return this.priority;
   }

   @Override
   public boolean canPlay(Entity entity, int tickCount) {
      return this.condition.test(entity, tickCount);
   }

   @Override
   public void play(Entity entity, int tickCount) {
      if (!this.isCurrentPlaying) {
         this.animationState.stop();
         this.animationState.start(tickCount);
         this.isCurrentPlaying = true;
         this.startTick = tickCount;
         this.onStart(entity, tickCount);
      } else {
         this.onUpdate(entity, tickCount);
      }
   }

   @Override
   public boolean isPlaying() {
      return this.isCurrentPlaying && this.animationState.isStarted();
   }

   @Override
   public void stop() {
      this.isCurrentPlaying = false;
      this.startTick = -1;
      if (this.animationState.isStarted()) {
         this.animationState.stop();
      }

      this.onStop();
   }

   protected int getTicksSinceStart(int currentTick) {
      return this.startTick >= 0 ? currentTick - this.startTick : 0;
   }

   protected AnimationState getAnimationState() {
      return this.animationState;
   }

   protected AnimationDefinition getAnimationDefinition() {
      return this.animationDefinition;
   }

   protected void onStart(Entity entity, int tickCount) {
   }

   protected void onUpdate(Entity entity, int tickCount) {
   }

   protected void onStop() {
   }
}
