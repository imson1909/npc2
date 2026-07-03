package net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HeadTrackingLayer extends AbstractProceduralLayer {
   private final ModelPart head;
   private float maxYawDegrees = 30.0F;
   private float minPitchDegrees = -25.0F;
   private float maxPitchDegrees = 45.0F;

   public HeadTrackingLayer(ModelPart head) {
      super("head_traking");
      this.head = head;
   }

   public HeadTrackingLayer(ModelPart head, float maxYaw, float minPitch, float maxPitch) {
      this(head);
      this.maxYawDegrees = maxYaw;
      this.minPitchDegrees = minPitch;
      this.maxPitchDegrees = maxPitch;
   }

   @Override
   protected void applyTransformations(ModelPart root, Entity entity, float partialTick) {
      if (entity instanceof LivingEntity livingEntity) {
         float netHeadYaw = livingEntity.yHeadRot - livingEntity.yBodyRot;
         float headPitch = livingEntity.getXRot();
         netHeadYaw = Mth.clamp(netHeadYaw, -this.maxYawDegrees, this.maxYawDegrees);
         headPitch = Mth.clamp(headPitch, this.minPitchDegrees, this.maxPitchDegrees);
         this.head.yRot += netHeadYaw * (float) (Math.PI / 180.0);
         this.head.xRot += headPitch * (float) (Math.PI / 180.0);
      }
   }
}
