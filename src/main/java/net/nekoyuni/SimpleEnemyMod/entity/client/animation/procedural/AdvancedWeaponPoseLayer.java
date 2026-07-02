package net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.nbt.GunItemDataAccessor;
import com.tacz.guns.resource.index.CommonGunIndex;
import java.util.Optional;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.util.WeaponPose;

public class AdvancedWeaponPoseLayer extends AbstractProceduralLayer {
   private final ModelPart head;
   private final ModelPart rightArm;
   private final ModelPart leftArm;

   public AdvancedWeaponPoseLayer(ModelPart head, ModelPart rightArm, ModelPart leftArm) {
      super("advanced_weapon_pose");
      this.head = head;
      this.rightArm = rightArm;
      this.leftArm = leftArm;
   }

   @Override
   protected void applyTransformations(ModelPart root, Entity entity, float partialTick) {
      if (entity instanceof LivingEntity living) {
         ItemStack stack = living.getMainHandItem();
         WeaponPose pose = this.determinePose(stack);
         switch (pose) {
            case HEAVY_HIP_FIRE:
               this.applyHipFirePose(root, living);
               break;
            case PISTOL_AIM:
               this.applyPistolPose(root, living);
               break;
            case RIFLE_AIM:
               this.applyRiflePose(root, living);
            case NONE:
         }
      }
   }

   private WeaponPose determinePose(ItemStack stack) {
      if (stack.getItem() instanceof GunItemDataAccessor gunItem) {
         ResourceLocation gunId = gunItem.getGunId(stack);
         if (gunId.getPath().contains("minigun")) {
            return WeaponPose.HEAVY_HIP_FIRE;
         }

         Optional<CommonGunIndex> gunIndexOpt = TimelessAPI.getCommonGunIndex(gunId);
         if (gunIndexOpt.isPresent()) {
            String type = gunIndexOpt.get().getType();
            if ("pistol".equals(type)) {
               return WeaponPose.PISTOL_AIM;
            }
         }

         return WeaponPose.RIFLE_AIM;
      } else {
         return WeaponPose.NONE;
      }
   }

   private void applyRiflePose(ModelPart root, Entity entity) {
      float yawFollow = 0.65F;
      float pitchFollow = 0.85F;
      this.rightArm.yRot = this.rightArm.yRot + this.head.yRot * yawFollow;
      this.rightArm.xRot = this.rightArm.xRot + this.head.xRot * pitchFollow;
      this.leftArm.yRot = this.leftArm.yRot + this.head.yRot * yawFollow;
      this.leftArm.xRot = this.leftArm.xRot + this.head.xRot * pitchFollow;
   }

   private void applyHipFirePose(ModelPart root, Entity entity) {
      float heavyPitchFactor = 0.15F;
      float yawFollow = 0.6F;
      float targetPitch = this.head.xRot * heavyPitchFactor;
      this.rightArm.yRot = this.rightArm.yRot + this.head.yRot * yawFollow;
      this.leftArm.yRot = this.leftArm.yRot + this.head.yRot * yawFollow;
      this.rightArm.xRot += targetPitch;
      this.leftArm.xRot += targetPitch;
      this.rightArm.xRot += 1.9F;
      this.leftArm.xRot += 0.18F;
      this.rightArm.zRot += 0.12F;
      this.rightArm.yRot -= 0.15F;
      this.leftArm.zRot -= 0.25F;
      this.leftArm.yRot += 0.45F;
      this.leftArm.xRot += 0.15F;
   }

   private void applyPistolPose(ModelPart root, Entity entity) {
      float yawFollow = 0.7F;
      float pitchFollow = 0.9F;
      this.leftArm.x = 4.0F;
      this.leftArm.y = -3.0F;
      this.leftArm.z = -2.0F;
      this.rightArm.z = 1.0F;
      this.rightArm.yRot = this.rightArm.yRot + this.head.yRot * yawFollow;
      this.rightArm.xRot = this.rightArm.xRot + this.head.xRot * pitchFollow;
      this.leftArm.yRot = this.leftArm.yRot + this.head.yRot * yawFollow;
      this.leftArm.xRot = this.leftArm.xRot + this.head.xRot * pitchFollow;
      this.rightArm.yRot -= 0.12F;
      this.leftArm.yRot += 0.12F;
      this.rightArm.zRot += 0.04F;
      this.leftArm.zRot -= 0.04F;
   }
}
