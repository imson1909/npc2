package net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public abstract class AbstractProceduralLayer implements IProceduralLayer {
   protected final String name;
   protected boolean enabled = true;

   protected AbstractProceduralLayer(String name) {
      this.name = name;
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public void apply(ModelPart root, Entity entity, float partialTick) {
      if (this.enabled) {
         this.applyTransformations(root, entity, partialTick);
      }
   }

   protected abstract void applyTransformations(ModelPart var1, Entity var2, float var3);

   public String getName() {
      return this.name;
   }
}
