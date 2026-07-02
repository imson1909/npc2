package net.nekoyuni.SimpleEnemyMod.entity.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.nekoyuni.SimpleEnemyMod.compat.geckolib.GeckoCompat;
import net.nekoyuni.SimpleEnemyMod.compat.geckolib.GeckoCompatClient;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class UniversalArmorLayer<T extends AbstractUnit, M extends EntityModel<T> & IArmorBoneProvider> extends RenderLayer<T, M> {
   private final HumanoidModel<LivingEntity> innerModel;
   private final HumanoidModel<LivingEntity> outerModel;
   private static final float GLOBAL_Y_OFFSET = -0.85F;
   private static final float SCALE_HEAD = 1.28F;
   private static final float SCALE_BODY = 1.98F;
   private static final float SCALE_ARMS = 1.9F;
   private static final float SCALE_LEGS = 2.1F;
   private static final float SCALE_BOOTS = 2.15F;
   private static final float OFF_ARM_RIGHT_X = -3.95F;
   private static final float OFF_ARM_RIGHT_Y = 0.0F;
   private static final float OFF_ARM_RIGHT_Z = 3.0F;
   private static final float OFF_ARM_LEFT_X = 3.0F;
   private static final float OFF_ARM_LEFT_Y = 0.0F;
   private static final float OFF_ARM_LEFT_Z = -4.5F;
   private static final float OFF_LEG_RIGHT_X = -2.0F;
   private static final float OFF_LEG_RIGHT_Y = 9.0F;
   private static final float OFF_LEG_RIGHT_Z = 3.5F;
   private static final float OFF_LEG_LEFT_X = 2.0F;
   private static final float OFF_LEG_LEFT_Y = 8.0F;
   private static final float OFF_LEG_LEFT_Z = -0.4F;
   private static final float OFF_BOOT_RIGHT_X = -2.0F;
   private static final float OFF_BOOT_RIGHT_Y = 11.0F;
   private static final float OFF_BOOT_RIGHT_Z = 3.8F;
   private static final float OFF_BOOT_LEFT_X = 2.0F;
   private static final float OFF_BOOT_LEFT_Y = 11.0F;
   private static final float OFF_BOOT_LEFT_Z = -1.2F;

   public UniversalArmorLayer(RenderLayerParent<T, M> parent, HumanoidModel<?> inner, HumanoidModel<?> outer) {
      super(parent);
      this.innerModel = (HumanoidModel<LivingEntity>)inner;
      this.outerModel = (HumanoidModel<LivingEntity>)outer;
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
      this.renderArmorPiece(
         poseStack, buffer, entity, EquipmentSlot.FEET, packedLight, this.outerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch
      );
      this.renderArmorPiece(
         poseStack, buffer, entity, EquipmentSlot.LEGS, packedLight, this.innerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch
      );
      this.renderArmorPiece(
         poseStack, buffer, entity, EquipmentSlot.CHEST, packedLight, this.outerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch
      );
      this.renderArmorPiece(
         poseStack, buffer, entity, EquipmentSlot.HEAD, packedLight, this.outerModel, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch
      );
   }

   private void renderArmorPiece(
      PoseStack poseStack,
      MultiBufferSource buffer,
      T entity,
      EquipmentSlot slot,
      int packedLight,
      HumanoidModel<LivingEntity> defaultModel,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      ItemStack itemStack = entity.getItemBySlot(slot);
      if (!itemStack.isEmpty()) {
         if (!GeckoCompat.LOADED || !GeckoCompatClient.isGeckoArmor(itemStack)) {
            if (itemStack.getItem() instanceof ArmorItem armorItem) {
               HumanoidModel<LivingEntity> armorModel = this.getArmorModel(entity, itemStack, slot, defaultModel);
               if (armorModel != null) {
                  armorModel.prepareMobModel(entity, limbSwing, limbSwingAmount, ageInTicks);
                  this.syncArmorToEntity((M)this.getParentModel(), armorModel, slot);
                  this.applyManualScale(armorModel, slot);
                  this.applyManualPosition(armorModel, slot);
                  boolean hasGlint = itemStack.hasFoil();
                  boolean isDyeable = itemStack.getItem() instanceof DyeableArmorItem;
                  ResourceLocation texture = this.getArmorTexture(entity, itemStack, slot, armorItem);
                  if (texture != null) {
                     poseStack.pushPose();
                     ((IArmorBoneProvider)this.getParentModel()).translateToBody(poseStack);
                     poseStack.translate(0.0F, -0.85F, 0.0F);
                     this.renderArmorModel(poseStack, buffer, packedLight, hasGlint, armorModel, texture, isDyeable, itemStack);
                     if (isDyeable) {
                        ResourceLocation overlayTexture = this.getArmorOverlayTexture(entity, itemStack, slot, armorItem);
                        if (overlayTexture != null) {
                           this.renderArmorModel(poseStack, buffer, packedLight, hasGlint, armorModel, overlayTexture, false, itemStack);
                        }
                     }

                     poseStack.popPose();
                  }
               }
            }
         }
      }
   }

   private void applyManualScale(HumanoidModel<LivingEntity> armorModel, EquipmentSlot slot) {
      switch (slot) {
         case HEAD:
            this.setPartScale(armorModel.head, 1.28F);
            this.setPartScale(armorModel.hat, 1.28F);
            break;
         case CHEST:
            this.setPartScale(armorModel.body, 1.98F);
            this.setPartScale(armorModel.rightArm, 1.9F);
            this.setPartScale(armorModel.leftArm, 1.9F);
            break;
         case LEGS:
            this.setPartScale(armorModel.body, 1.98F);
            this.setPartScale(armorModel.rightLeg, 2.1F);
            this.setPartScale(armorModel.leftLeg, 2.1F);
            break;
         case FEET:
            this.setPartScale(armorModel.rightLeg, 2.15F);
            this.setPartScale(armorModel.leftLeg, 2.15F);
      }
   }

   private void applyManualPosition(HumanoidModel<LivingEntity> armorModel, EquipmentSlot slot) {
      switch (slot) {
         case CHEST:
            this.addPartOffset(armorModel.rightArm, -3.95F, 0.0F, 3.0F);
            this.addPartOffset(armorModel.leftArm, 3.0F, 0.0F, -4.5F);
            break;
         case LEGS:
            this.addPartOffset(armorModel.rightLeg, -2.0F, 9.0F, 3.5F);
            this.addPartOffset(armorModel.leftLeg, 2.0F, 8.0F, -0.4F);
            break;
         case FEET:
            this.addPartOffset(armorModel.rightLeg, -2.0F, 11.0F, 3.8F);
            this.addPartOffset(armorModel.leftLeg, 2.0F, 11.0F, -1.2F);
      }
   }

   private void setPartScale(ModelPart part, float scale) {
      part.xScale = scale;
      part.yScale = scale;
      part.zScale = scale;
   }

   private void addPartOffset(ModelPart part, float x, float y, float z) {
      part.x += x;
      part.y += y;
      part.z += z;
   }

   private void syncArmorToEntity(M entityModel, HumanoidModel<LivingEntity> armorModel, EquipmentSlot slot) {
      armorModel.setAllVisible(false);
      switch (slot) {
         case HEAD:
            armorModel.head.visible = true;
            armorModel.hat.visible = true;
            this.copyModelPart(entityModel.getHead(), armorModel.head);
            this.copyModelPart(entityModel.getHead(), armorModel.hat);
            break;
         case CHEST:
            armorModel.body.visible = true;
            armorModel.rightArm.visible = true;
            armorModel.leftArm.visible = true;
            this.copyModelPart(entityModel.getBody(), armorModel.body);
            this.copyModelPart(entityModel.getRightArm(), armorModel.rightArm);
            this.copyModelPart(entityModel.getLeftArm(), armorModel.leftArm);
            break;
         case LEGS:
            armorModel.body.visible = true;
            armorModel.rightLeg.visible = true;
            armorModel.leftLeg.visible = true;
            this.copyModelPart(entityModel.getBody(), armorModel.body);
            this.copyModelPart(entityModel.getRightLeg(), armorModel.rightLeg);
            this.copyModelPart(entityModel.getLeftLeg(), armorModel.leftLeg);
            break;
         case FEET:
            armorModel.rightLeg.visible = true;
            armorModel.leftLeg.visible = true;
            this.copyModelPart(entityModel.getRightLeg(), armorModel.rightLeg);
            this.copyModelPart(entityModel.getLeftLeg(), armorModel.leftLeg);
      }
   }

   private void copyModelPart(ModelPart source, ModelPart target) {
      target.xRot = source.xRot;
      target.yRot = source.yRot;
      target.zRot = source.zRot;
      target.x = source.x;
      target.y = source.y;
      target.z = source.z;
   }

   private HumanoidModel<LivingEntity> getArmorModel(T entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> defaultModel) {
      Model model = ForgeHooksClient.getArmorModel(entity, stack, slot, defaultModel);
      return model instanceof HumanoidModel ? (HumanoidModel)model : null;
   }

   private ResourceLocation getArmorTexture(T entity, ItemStack stack, EquipmentSlot slot, ArmorItem armorItem) {
      String baseTexture = armorItem.getArmorTexture(stack, entity, slot, null);
      String texture = ForgeHooksClient.getArmorTexture(entity, stack, baseTexture, slot, null);
      if (texture != null) {
         return ResourceLocation.tryParse(texture);
      }

      String material = armorItem.getMaterial().getName();
      String domain = "minecraft";
      String path = material;
      if (material.contains(":")) {
         String[] split = material.split(":");
         domain = split[0];
         path = split[1];
      }

      String layer = slot == EquipmentSlot.LEGS ? "layer_2" : "layer_1";
      return new ResourceLocation(domain, "textures/models/armor/" + path + "_" + layer + ".png");
   }

   private ResourceLocation getArmorOverlayTexture(T entity, ItemStack stack, EquipmentSlot slot, ArmorItem armorItem) {
      String texture = ForgeHooksClient.getArmorTexture(entity, stack, armorItem.getArmorTexture(stack, entity, slot, "overlay"), slot, "overlay");
      return texture != null ? ResourceLocation.tryParse(texture) : null;
   }

   private void renderArmorModel(
           PoseStack poseStack, MultiBufferSource buffer,
           int packedLight,
           boolean hasGlint,
           HumanoidModel<LivingEntity> model,
           ResourceLocation texture,
           boolean isDyeable,
           ItemStack stack
   ) {
      VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, RenderType.armorCutoutNoCull(texture), false, hasGlint);
      float r = 1.0F;
      float g = 1.0F;
      float b = 1.0F;
      if (isDyeable && stack.getItem() instanceof DyeableArmorItem dyeable) {
         int color = dyeable.getColor(stack);
         r = (color >> 16 & 0xFF) / 255.0F;
         g = (color >> 8 & 0xFF) / 255.0F;
         b = (color & 0xFF) / 255.0F;
      }

      model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_WHITE_U, r, g, b, 1.0F);
   }
}
