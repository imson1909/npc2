package net.nekoyuni.SimpleEnemyMod.compat.geckolib.internal;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GeckoArmorAdjuster {
   private static final float SCALE_HEAD = 1.0F;
   private static final float SCALE_BODY = 1.0F;
   private static final float SCALE_ARMS = 1.04F;
   private static final float SCALE_LEGS = 1.15F;
   private static final float SCALE_BOOTS = 1.2F;
   private static final float OFF_HEAD_X = 0.0F;
   private static final float OFF_HEAD_Y = 6.0F;
   private static final float OFF_HEAD_Z = -1.75F;
   private static final float OFF_BODY_X = 0.0F;
   private static final float OFF_BODY_Y = 5.9F;
   private static final float OFF_BODY_Z = -1.85F;
   private static final float OFF_ARM_RIGHT_X = -0.2F;
   private static final float OFF_ARM_RIGHT_Y = 5.9F;
   private static final float OFF_ARM_RIGHT_Z = -1.65F;
   private static final float OFF_ARM_LEFT_X = 0.2F;
   private static final float OFF_ARM_LEFT_Y = 5.9F;
   private static final float OFF_ARM_LEFT_Z = -1.65F;
   private static final float OFF_LEG_RIGHT_X = -0.25F;
   private static final float OFF_LEG_RIGHT_Y = 5.5F;
   private static final float OFF_LEG_RIGHT_Z = -0.45F;
   private static final float OFF_LEG_LEFT_X = 0.1F;
   private static final float OFF_LEG_LEFT_Y = 5.98F;
   private static final float OFF_LEG_LEFT_Z = -0.5F;
   private static final float OFF_BOOT_RIGHT_X = 0.25F;
   private static final float OFF_BOOT_RIGHT_Y = 7.0F;
   private static final float OFF_BOOT_RIGHT_Z = -0.25F;
   private static final float OFF_BOOT_LEFT_X = -0.25F;
   private static final float OFF_BOOT_LEFT_Y = 7.0F;
   private static final float OFF_BOOT_LEFT_Z = -0.65F;

   public static void applyAdjustments(GeoArmorRenderer<?> renderer, HumanoidModel<?> baseModel, EquipmentSlot slot) {
      if (renderer != null && baseModel != null) {
         GeoBone geoHead = renderer.getHeadBone();
         GeoBone geoBody = renderer.getBodyBone();
         GeoBone geoRightArm = renderer.getRightArmBone();
         GeoBone geoLeftArm = renderer.getLeftArmBone();
         GeoBone geoRightLeg = renderer.getRightLegBone();
         GeoBone geoLeftLeg = renderer.getLeftLegBone();
         GeoBone geoRightBoot = renderer.getRightBootBone();
         GeoBone geoLeftBoot = renderer.getLeftBootBone();
         ModelPart vanHead = baseModel.head;
         ModelPart vanBody = baseModel.body;
         ModelPart vanRightArm = baseModel.rightArm;
         ModelPart vanLeftArm = baseModel.leftArm;
         ModelPart vanRightLeg = baseModel.rightLeg;
         ModelPart vanLeftLeg = baseModel.leftLeg;
         switch (slot) {
            case HEAD:
               applyScale(geoHead, 1.0F);
               applyOffset(vanHead, 0.0F, 6.0F, -1.75F);
               break;
            case CHEST:
               applyScale(geoBody, 1.0F);
               applyOffset(vanBody, 0.0F, 5.9F, -1.85F);
               applyScale(geoRightArm, 1.04F);
               applyScale(geoLeftArm, 1.04F);
               applyOffset(vanRightArm, -0.2F, 5.9F, -1.65F);
               applyOffset(vanLeftArm, 0.2F, 5.9F, -1.65F);
               break;
            case LEGS:
               applyScale(geoBody, 1.0F);
               applyOffset(vanBody, 0.0F, 5.9F, -1.85F);
               applyScale(geoRightLeg, 1.15F);
               applyScale(geoLeftLeg, 1.15F);
               applyOffset(vanRightLeg, -0.25F, 5.5F, -0.45F);
               applyOffset(vanLeftLeg, 0.1F, 5.98F, -0.5F);
               break;
            case FEET:
               if (geoRightBoot != null) {
                  applyScale(geoRightBoot, 1.2F);
               } else {
                  applyScale(geoRightLeg, 1.2F);
               }

               if (geoLeftBoot != null) {
                  applyScale(geoLeftBoot, 1.2F);
               } else {
                  applyScale(geoLeftLeg, 1.2F);
               }

               applyOffset(vanRightLeg, 0.25F, 7.0F, -0.25F);
               applyOffset(vanLeftLeg, -0.25F, 7.0F, -0.65F);
         }
      }
   }

   private static void applyScale(GeoBone bone, float scale) {
      if (bone != null) {
         bone.updateScale(scale, scale, scale);
      }
   }

   private static void applyOffset(ModelPart part, float x, float y, float z) {
      if (part != null) {
         part.x += x;
         part.y += y;
         part.z += z;
      }
   }
}
