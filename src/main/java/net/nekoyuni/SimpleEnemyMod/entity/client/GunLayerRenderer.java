package net.nekoyuni.SimpleEnemyMod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class GunLayerRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   private final ItemInHandRenderer itemRenderer;

   public GunLayerRenderer(RenderLayerParent<T, M> parent, ItemInHandRenderer itemRenderer) {
      super(parent);
      this.itemRenderer = itemRenderer;
   }

   public void render(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      T entity,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      if (!entity.isDeadOrDying()) {
         ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
         if (stack.getItem() instanceof AbstractGunItem gunItem) {
            ResourceLocation gunId = gunItem.getGunId(stack);
            boolean isMinigun = gunId != null && gunId.getPath().contains("minigun");
            if (this.getParentModel() instanceof HierarchicalModel<?> hierarchicalModel) {
               HierarchicalModel<T> hierarchicalModelTyped = (HierarchicalModel<T>)hierarchicalModel;
               ModelPart unit = hierarchicalModelTyped.root().getChild("unit");
               ModelPart rightArm = unit.getChild("rightArm");
               ModelPart rightHandAnchor = rightArm.getChild("rightHandAnchor");

               poseStack.pushPose();

               // FIX: Removed unit.render(), rightArm.render(), rightHandAnchor.render() 
               // These were causing the double model! They re-render the model geometry.
               // Only apply transformations to position the gun correctly:

               // Apply unit transform
               poseStack.translate(unit.x / 16.0F, unit.y / 16.0F, unit.z / 16.0F);
               poseStack.mulPose(Axis.XP.rotation(unit.xRot));
               poseStack.mulPose(Axis.YP.rotation(unit.yRot));
               poseStack.mulPose(Axis.ZP.rotation(unit.zRot));

               // Apply rightArm transform
               poseStack.translate(rightArm.x / 16.0F, rightArm.y / 16.0F, rightArm.z / 16.0F);
               poseStack.mulPose(Axis.XP.rotation(rightArm.xRot));
               poseStack.mulPose(Axis.YP.rotation(rightArm.yRot));
               poseStack.mulPose(Axis.ZP.rotation(rightArm.zRot));

               // Apply rightHandAnchor transform
               poseStack.translate(rightHandAnchor.x / 16.0F, rightHandAnchor.y / 16.0F, rightHandAnchor.z / 16.0F);
               poseStack.mulPose(Axis.XP.rotation(rightHandAnchor.xRot));
               poseStack.mulPose(Axis.YP.rotation(rightHandAnchor.yRot));
               poseStack.mulPose(Axis.ZP.rotation(rightHandAnchor.zRot));

               if (isMinigun) {
                  this.renderMinigun(entity, stack, poseStack, buffer, packedLight);
               } else {
                  boolean inCombat = !(entity instanceof AbstractUnit unit2 && !unit2.isInCombat());
                  if (inCombat) {
                     this.renderStandardGun(entity, stack, poseStack, buffer, packedLight);
                  } else {
                     this.renderStandardGunLowStance(entity, stack, poseStack, buffer, packedLight);
                  }
               }

               poseStack.popPose();
            }
         }
      }
   }

   private void renderStandardGun(T entity, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      poseStack.translate(0.0, 0.1, 0.28);
      poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
      poseStack.mulPose(Axis.XP.rotationDegrees(-95.0F));
      poseStack.scale(1.0F, -1.0F, -1.0F);
      this.itemRenderer.renderItem(entity, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, buffer, packedLight);
   }

   private void renderStandardGunLowStance(T entity, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      poseStack.translate(0.0, 0.35, 0.13);
      poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
      poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
      poseStack.scale(1.0F, -1.0F, -1.0F);
      this.itemRenderer.renderItem(entity, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, buffer, packedLight);
   }

   private void renderMinigun(T entity, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      poseStack.translate(0.01, 0.3, -0.25);
      poseStack.mulPose(Axis.YP.rotationDegrees(-180.0F));
      poseStack.mulPose(Axis.XP.rotationDegrees(-70.0F));
      poseStack.mulPose(Axis.ZP.rotationDegrees(6.0F));
      poseStack.scale(1.0F, -1.0F, -1.0F);
      this.itemRenderer.renderItem(entity, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, buffer, packedLight);
   }
}
