package net.nekoyuni.SimpleEnemyMod.entity.client.pmc_unit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.Entity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.ModAnimationsDefinitions;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.config.UnitAnimationConfig;
import net.nekoyuni.SimpleEnemyMod.entity.client.util.IArmorBoneProvider;
import net.nekoyuni.SimpleEnemyMod.entity.client.util.UnitModelDefinitions;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class PmcUnitModel<T extends Entity> extends HierarchicalModel<T> implements IArmorBoneProvider {
   private final ModelPart fakeRoot;
   private final ModelPart unit;
   private final ModelPart head;
   private final ModelPart body;
   private final ModelPart rightArm;
   private final ModelPart rightHandAnchor;
   private final ModelPart leftArm;
   private final ModelPart rightLeg;
   private final ModelPart leftLeg;

   public PmcUnitModel(ModelPart root) {
      this.fakeRoot = root.getChild("fakeRoot");
      this.unit = this.fakeRoot.getChild("unit");
      this.head = this.unit.getChild("head");
      this.body = this.unit.getChild("body");
      this.rightArm = this.unit.getChild("rightArm");
      this.rightHandAnchor = this.rightArm.getChild("rightHandAnchor");
      this.leftArm = this.unit.getChild("leftArm");
      this.rightLeg = this.unit.getChild("rightLeg");
      this.leftLeg = this.unit.getChild("leftLeg");
   }

   @Override
   public void translateToBody(PoseStack poseStack) {
      // FIX: Apply the "unit" part's transform so armor follows the body
      // The unit part is the parent of all body parts (head, body, arms, legs).
      // Without this, armor is rendered at the entity origin instead of following
      // the unit's rotation/position (e.g., death animation, procedural layers).
      poseStack.translate(this.unit.x / 16.0F, this.unit.y / 16.0F, this.unit.z / 16.0F);
      poseStack.mulPose(Axis.XP.rotation(this.unit.xRot));
      poseStack.mulPose(Axis.YP.rotation(this.unit.yRot));
      poseStack.mulPose(Axis.ZP.rotation(this.unit.zRot));
   }

   public static LayerDefinition createBodyLayer() {
      return UnitModelDefinitions.createBaseUnitBodyLayer();
   }

   @Override
   public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks) {
      this.root().getAllParts().forEach(ModelPart::resetPose);
      if (entity instanceof AbstractUnit unitEntity) {
         float actualAgeInTicks = unitEntity.tickCount + ageInTicks;

         if (unitEntity.deathAnimationState.isStarted()) {
            boolean playBack = (Boolean) unitEntity.getEntityData().get(AbstractUnit.BACK_DEATH);
            if (playBack) {
               this.animate(unitEntity.deathAnimationState, ModAnimationsDefinitions.UNIT_DEATH_BACK, actualAgeInTicks, 1.0F);
            } else {
               this.animate(unitEntity.deathAnimationState, ModAnimationsDefinitions.UNIT_DEATH, actualAgeInTicks, 1.0F);
            }
         } else if (!unitEntity.hurtAnimationState.isStarted()) {
            if (unitEntity.walkAnimationState.isStarted()) {
               this.animate(unitEntity.walkAnimationState, ModAnimationsDefinitions.UNIT_WALK, actualAgeInTicks, 1.0F);
            } else if (unitEntity.idleAnimationState.isStarted()) {
               this.animate(unitEntity.idleAnimationState, ModAnimationsDefinitions.UNIT_IDLE, actualAgeInTicks, 1.0F);
            }
         } else {
            int variantIndex = unitEntity.currentHurtVariant;
            int safeIndex = variantIndex >= 0 && variantIndex < ModAnimationsDefinitions.UNIT_HURT_VARIANTS.length ? variantIndex : 0;
            this.animate(unitEntity.hurtAnimationState, ModAnimationsDefinitions.UNIT_HURT_VARIANTS[safeIndex], actualAgeInTicks, 2.0F);
         }
      }
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
      this.head.xRot = headPitch * ((float)Math.PI / 180F);

      if (entity instanceof AbstractUnit unitEntity) {
         if (unitEntity.getAnimationManager() == null) {
            unitEntity.setAnimationManager(UnitAnimationConfig.create(unitEntity, this.head, this.rightArm, this.leftArm));
         }
         float actualAgeInTicks = unitEntity.tickCount + ageInTicks;
         unitEntity.getAnimationManager().applyProceduralLayers(this.root(), unitEntity, actualAgeInTicks);
      }
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.fakeRoot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }

   @Override
   public ModelPart root() {
      return this.fakeRoot;
   }

   @Override
   public ModelPart getUnit() {
      return this.unit;
   }

   @Override
   public ModelPart getHead() {
      return this.head;
   }

   @Override
   public ModelPart getBody() {
      return this.body;
   }

   @Override
   public ModelPart getRightArm() {
      return this.rightArm;
   }

   @Override
   public ModelPart getLeftArm() {
      return this.leftArm;
   }

   @Override
   public ModelPart getRightLeg() {
      return this.rightLeg;
   }

   @Override
   public ModelPart getLeftLeg() {
      return this.leftLeg;
   }
}
