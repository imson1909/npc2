package net.nekoyuni.SimpleEnemyMod.entity.client.animation.core;

import net.minecraft.world.entity.Entity;

public class AnimationUpdateContext {
   private final Entity entity;
   private final int tickCount;
   private Boolean isMoving;
   private Boolean isDead;

   public AnimationUpdateContext(Entity entity, int tickCount) {
      this.entity = entity;
      this.tickCount = tickCount;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public int getTickCount() {
      return this.tickCount;
   }

   public boolean isMoving() {
      if (this.isMoving == null) {
         this.isMoving = this.entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
      }

      return this.isMoving;
   }

   public boolean isDead() {
      if (this.isDead == null) {
         this.isDead = !this.entity.isAlive();
      }

      return this.isDead;
   }
}
