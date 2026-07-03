package net.nekoyuni.SimpleEnemyMod.entity.client.pmc_unit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.nekoyuni.SimpleEnemyMod.compat.geckolib.GeckoCompat;
import net.nekoyuni.SimpleEnemyMod.compat.geckolib.GeckoCompatClient;
import net.nekoyuni.SimpleEnemyMod.config.ClientConfig;
import net.nekoyuni.SimpleEnemyMod.entity.client.GunLayerRenderer;
import net.nekoyuni.SimpleEnemyMod.entity.client.util.UniversalArmorLayer;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import org.joml.Quaternionf;

public class PmcUnitRenderer extends MobRenderer<PmcUnitEntity, PmcUnitModel<PmcUnitEntity>> {
   private static ResourceLocation[] PMCUNIT_TEXTURES = new ResourceLocation[]{
           new ResourceLocation("simpleenemymod", "textures/entity/pmc_unit/pmc_unit_default.png")
   };

   public PmcUnitRenderer(Context context) {
      super(context, new PmcUnitModel(context.bakeLayer(PmcUnitModelLayers.PMCUNIT_LAYER)), 0.5F);
      HumanoidModel innerArmor = new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
      HumanoidModel outerArmor = new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
      this.addLayer(new UniversalArmorLayer(this, innerArmor, outerArmor));
      // FIX: Removed duplicate GeckoLib armor layer - UniversalArmorLayer already handles armor
      // If GeckoLib is required for special armor rendering, use config flag instead of always loading
      this.addLayer(new GunLayerRenderer(this, context.getItemInHandRenderer()));
   }

   public ResourceLocation getTextureLocation(PmcUnitEntity entity) {
      int variant = entity.getVariant();
      return variant >= 0 && variant < PMCUNIT_TEXTURES.length ? PMCUNIT_TEXTURES[variant] : PMCUNIT_TEXTURES[0];
   }

   public boolean shouldRender(PmcUnitEntity entity, Frustum frustum, double camX, double camY, double camZ) {
      int configDist = (Integer)ClientConfig.RENDER_DISTANCE.get();
      double maxDistance = configDist * configDist;
      double dx = entity.getX() - camX;
      double dy = entity.getY() - camY;
      double dz = entity.getZ() - camZ;
      double distanceSq = dx * dx + dy * dy + dz * dz;
      return distanceSq <= maxDistance ? true : super.shouldRender(entity, frustum, camX, camY, camZ);
   }

   protected void setupRotations(PmcUnitEntity pEntity, PoseStack pPoseStack, float pAgeInTicks, float pBodyYRot, float pPartialTicks) {
      if (pEntity.deathAnimationState.isStarted()) {
         float rootMotionX = ((PmcUnitModel)this.model).root().x / 16.0F;
         float rootMotionY = ((PmcUnitModel)this.model).root().y / 16.0F;
         float rootMotionZ = ((PmcUnitModel)this.model).root().z / 16.0F;
         float rootRotX = ((PmcUnitModel)this.model).root().xRot;
         float rootRotY = ((PmcUnitModel)this.model).root().yRot;
         float rootRotZ = ((PmcUnitModel)this.model).root().zRot;
         pPoseStack.translate(rootMotionX, 0.0F, 0.0F);
         pPoseStack.mulPose(new Quaternionf().rotationXYZ(rootRotX, rootRotY, rootRotZ));
         ((PmcUnitModel)this.model).root().setPos(0.0F, 0.0F, 0.0F);
         ((PmcUnitModel)this.model).root().setRotation(0.0F, 0.0F, 0.0F);
         float bodyRotation = Mth.lerp(pPartialTicks, pEntity.yBodyRotO, pEntity.yBodyRot);
         pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyRotation));
         return; // FIX: Added return to prevent super.setupRotations from overriding death animation
      } else {
         super.setupRotations(pEntity, pPoseStack, pAgeInTicks, pBodyYRot, pPartialTicks);
      }
   }

   public void render(PmcUnitEntity p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
      super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
   }

   private static void reloadTextures(ResourceManager rm) {
      List<ResourceLocation> found = new ArrayList<>();
      rm.listResources("textures/entity/pmc_unit", path -> path.getPath().endsWith(".png")).keySet().forEach(found::add);
      found.sort(Comparator.comparing(rl -> rl.toString()));
      if (!found.isEmpty()) {
         PMCUNIT_TEXTURES = found.toArray(new ResourceLocation[0]);
      }
   }
}
