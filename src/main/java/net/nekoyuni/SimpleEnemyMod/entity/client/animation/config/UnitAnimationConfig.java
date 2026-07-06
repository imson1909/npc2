package net.nekoyuni.SimpleEnemyMod.entity.client.animation.config;

import com.mojang.logging.LogUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.ModAnimationsDefinitions;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.AnimationPriority;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.LayeredAnimationManager;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer.IAnimationLayer;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural.AdvancedWeaponPoseLayer;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural.CombatStateLayer;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural.HeadTrackingLayer;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import org.slf4j.Logger;

public class UnitAnimationConfig {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final boolean isDebug = false;

   public static LayeredAnimationManager create(AbstractUnit entity, ModelPart head, ModelPart rightArm, ModelPart leftArm) {
      LayeredAnimationManager manager = AnimationConfig.builder()
         .addDeathLayer("death", entity.deathAnimationState, e -> {
            if (e instanceof AbstractUnit unit) {
               boolean backDeath = (Boolean)unit.getEntityData().get(AbstractUnit.BACK_DEATH);
               return backDeath ? ModAnimationsDefinitions.UNIT_DEATH_BACK : ModAnimationsDefinitions.UNIT_DEATH;
            } else {
               return ModAnimationsDefinitions.UNIT_DEATH;
            }
         })
         .addActionLayer("hurt", entity.hurtAnimationState, ModAnimationsDefinitions.UNIT_HURT_VARIANTS)
         .priority(AnimationPriority.HIGH)
         .speed(2.0F)
         .duration(1.0F)
         .triggerOn(e -> {
            if (e instanceof AbstractUnit unit) {
               int consistentTrigger = (Integer)unit.getEntityData().get(AbstractUnit.DAMAGE_ANIMATION_TICKS);
               // FIX: Also check if hurt animation is already playing to prevent missing trigger due to sync delay
               return (consistentTrigger > 0 || unit.hurtAnimationState.isStarted()) ? 1 : 0;
            } else {
               return 0;
            }
         })
         .build()
         .addLocomotionLayer(
            "locomotion",
            entity.idleAnimationState,
            ModAnimationsDefinitions.UNIT_IDLE,
            entity.walkAnimationState,
            ModAnimationsDefinitions.UNIT_WALK,
            AnimationPriority.MEDIUM
         )
         .addProceduralLayer(new HeadTrackingLayer(head, 30.0F, -25.0F, 45.0F))
         .addProceduralLayer(new CombatStateLayer(rightArm, leftArm))
         .addProceduralLayer(new AdvancedWeaponPoseLayer(head, rightArm, leftArm))
         .build();
      debug("==================================================");
      debug("[UnitAnimationConfig] AnimationManager created with {} animation layers:", manager.getAllAnimationLayers().size());

      for (IAnimationLayer layer : manager.getAllAnimationLayers()) {
         debug("    -> {} (priority: {})", layer.getName(), layer.getPriority());
      }

      debug("[UnitAnimationConfig] Procedural layers: {}", manager.getAllProceduralLayers().size());
      debug("==================================================");
      return manager;
   }

   private static void debug(String message, Object... args) {
      if (isDebug) {
         LOGGER.info(message, args);
      }
   }
}
