package net.nekoyuni.SimpleEnemyMod.compat.geckolib.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import net.nekoyuni.SimpleEnemyMod.compat.curios.CuriosCompat;
import net.nekoyuni.SimpleEnemyMod.compat.curios.CuriosHelper;
import net.nekoyuni.SimpleEnemyMod.entity.client.pmc_unit.PmcUnitModel;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GeckoArmorLayerImpl<T extends AbstractUnit, M extends EntityModel<T>> extends RenderLayer<T, M> {
   private final Map<Item, HumanoidModel<?>> rendererCache = new HashMap<>();
   private final HumanoidModel<?> dummyHumanoidModel;

   private GeckoArmorLayerImpl(RenderLayerParent<T, M> parent, HumanoidModel<?> dummyModel) {
      super(parent);
      this.dummyHumanoidModel = dummyModel;
   }

   public static <T extends AbstractUnit, M extends EntityModel<T>> RenderLayer<T, M> create(RenderLayerParent<T, M> parent, HumanoidModel<?> dummyModel) {
      return new GeckoArmorLayerImpl<>(parent, dummyModel);
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
      for (EquipmentSlot slot : EquipmentSlot.values()) {
         if (slot.isArmor()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!stack.isEmpty() && GeckoHooksImpl.isGeckoArmor(stack)) {
               this.renderArmorPiece(
                  poseStack, buffer, packedLight, entity, stack, slot, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch
               );
            }
         }
      }

      if (CuriosCompat.LOADED) {
         CuriosHelper.renderCuriosSlots(
            entity,
            (stackx, mappedSlot) -> this.renderArmorPiece(
               poseStack, buffer, packedLight, entity, stackx, mappedSlot, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch
            )
         );
      }
   }

   private void renderArmorPiece(
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      T entity,
      ItemStack stack,
      EquipmentSlot slot,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
   ) {
      Item itemKey = stack.getItem();
      HumanoidModel<?> armorModel = this.rendererCache.computeIfAbsent(itemKey, k -> {
         IClientItemExtensions extensions = IClientItemExtensions.of(stack);
         HumanoidModel<?> parentHumanoid = this.getParentModel() instanceof HumanoidModel<?> humanoidx ? humanoidx : this.dummyHumanoidModel;
         return extensions.getHumanoidArmorModel(entity, stack, slot, parentHumanoid);
      });
      if (armorModel != null) {
         boolean isGeckoArmor = armorModel instanceof GeoArmorRenderer;
         if (isGeckoArmor) {
            GeoArmorRenderer<?> geoRenderer = (GeoArmorRenderer<?>)armorModel;
            HumanoidModel<?> baseModelToUse;
            if (this.getParentModel() instanceof HumanoidModel<?> humanoid) {
               baseModelToUse = humanoid;
            } else {
               baseModelToUse = this.dummyHumanoidModel;
               if (this.getParentModel() instanceof PmcUnitModel<?> unitModel) {
                  this.syncModelParts(unitModel, this.dummyHumanoidModel);
               }
            }

            geoRenderer.prepForRender(entity, stack, slot, baseModelToUse);
            GeckoArmorAdjuster.applyAdjustments(geoRenderer, baseModelToUse, slot);
         } else {
            HumanoidModel<T> castedModel = (HumanoidModel<T>)armorModel;
            this.setPartVisibility(armorModel, slot);
            castedModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            castedModel.prepareMobModel(entity, limbSwing, limbSwingAmount, ageInTicks);
         }

         poseStack.pushPose();
         armorModel.renderToBuffer(
                 poseStack,
                 buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity))),
                 packedLight,
                 OverlayTexture.NO_OVERLAY,
                 1.0F,
                 1.0F,
                 1.0F,
                 1.0F
         );
      }
   }

   private void syncModelParts(PmcUnitModel<?> source, HumanoidModel<?> target) {
      ModelPart unitParent = source.getUnit();
      float parentRotX = unitParent.xRot;
      float parentRotY = unitParent.yRot;
      float parentRotZ = unitParent.zRot;
      float parentPosX = unitParent.x;
      float parentPosY = unitParent.y;
      float parentPosZ = unitParent.z;
      this.copyPartWithGlobalOffset(source.getHead(), target.head, parentPosX, parentPosY, parentPosZ, parentRotX, parentRotY, parentRotZ, true);
      this.copyPartWithGlobalOffset(source.getBody(), target.body, parentPosX, parentPosY, parentPosZ, parentRotX, parentRotY, parentRotZ, true);
      this.copyPartWithGlobalOffset(source.getRightArm(), target.rightArm, parentPosX, parentPosY, parentPosZ, parentRotX, parentRotY, parentRotZ, true);
      this.copyPartWithGlobalOffset(source.getLeftArm(), target.leftArm, parentPosX, parentPosY, parentPosZ, parentRotX, parentRotY, parentRotZ, true);
      this.copyPartWithGlobalOffset(source.getRightLeg(), target.rightLeg, parentPosX, parentPosY, parentPosZ, parentRotX, parentRotY, parentRotZ, true);
      this.copyPartWithGlobalOffset(source.getLeftLeg(), target.leftLeg, parentPosX, parentPosY, parentPosZ, parentRotX, parentRotY, parentRotZ, true);
      target.crouching = false;
      target.riding = false;
      target.young = false;
   }

   private void copyPartWithGlobalOffset(
      ModelPart source, ModelPart target, float pX, float pY, float pZ, float rX, float rY, float rZ, boolean preserveVisibility
   ) {
      boolean originalVisibility = target.visible;
      target.setPos(0.0F, 0.0F, 0.0F);
      target.setRotation(0.0F, 0.0F, 0.0F);
      target.xScale = 1.0F;
      target.yScale = 1.0F;
      target.zScale = 1.0F;
      target.copyFrom(source);
      if (preserveVisibility) {
         target.visible = originalVisibility;
      }

      target.x += pX;
      target.y += pY;
      target.z += pZ;
      target.xRot += rX;
      target.yRot += rY;
      target.zRot += rZ;
   }

   public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
      String domain = ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace();
      String path = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
      String defaultPath = domain
         + ":textures/models/armor/"
         + path
         + "_layer_"
         + (slot == EquipmentSlot.LEGS ? 2 : 1)
         + (type == null ? "" : "_" + type)
         + ".png";
      String texture = ForgeHooksClient.getArmorTexture(entity, stack, defaultPath, slot, type);
      return ResourceLocation.tryParse(texture);
   }

   private void setPartVisibility(HumanoidModel<?> model, EquipmentSlot slot) {
      model.setAllVisible(false);
      switch (slot) {
         case HEAD:
            model.head.visible = true;
            model.hat.visible = true;
            break;
         case CHEST:
            model.body.visible = true;
            model.rightArm.visible = true;
            model.leftArm.visible = true;
            break;
         case LEGS:
            model.body.visible = true;
            model.rightLeg.visible = true;
            model.leftLeg.visible = true;
            break;
         case FEET:
            model.rightLeg.visible = true;
            model.leftLeg.visible = true;
      }
   }
}
