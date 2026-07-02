package net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class ArmAimingLayer extends AbstractProceduralLayer {
   private final ModelPart head;
   private final ModelPart rightArm;
   private final ModelPart leftArm;
   private float yawFollowFactor = 0.65F;
   private float pitchFollowFactor = 0.85F;

   public ArmAimingLayer(ModelPart head, ModelPart rightArm, ModelPart leftArm) {
      super("arm_aiming");
      this.head = head;
      this.rightArm = rightArm;
      this.leftArm = leftArm;
   }

   public ArmAimingLayer(ModelPart head, ModelPart rightArm, ModelPart leftArm, float yawFactor, float pitchFactor) {
      this(head, rightArm, leftArm);
      this.yawFollowFactor = yawFactor;
      this.pitchFollowFactor = pitchFactor;
   }

   @Override
   protected void applyTransformations(ModelPart root, Entity entity, float partialTick) {
      this.rightArm.yRot = this.rightArm.yRot + this.head.yRot * this.yawFollowFactor;
      this.rightArm.xRot = this.rightArm.xRot + this.head.xRot * this.pitchFollowFactor;
      this.leftArm.yRot = this.leftArm.yRot + this.head.yRot * this.yawFollowFactor;
      this.leftArm.xRot = this.leftArm.xRot + this.head.xRot * this.pitchFollowFactor;
   }

   public void setYawFollowFactor(float factor) {
      this.yawFollowFactor = factor;
   }

   public void setHeadPitchFactor(float factor) {
      this.pitchFollowFactor = factor;
   }
}
