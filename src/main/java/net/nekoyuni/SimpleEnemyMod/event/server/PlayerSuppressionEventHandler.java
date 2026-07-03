package net.nekoyuni.SimpleEnemyMod.event.server;

import com.tacz.guns.api.entity.ShootResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.client.system.SuppressionManager;

@EventBusSubscriber(modid = "simpleenemymod")
public class PlayerSuppressionEventHandler {
   @SubscribeEvent
   public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
      if (event.getEntity() instanceof LivingEntity living && !event.getLevel().isClientSide()) {
      }
   }

   public static void onGunShoot(LivingEntity shooter, ShootResult result) {
      if (!shooter.level().isClientSide()) {
         boolean isEnemy = shooter.getType().getDescriptionId().contains("pmc_unit");
         if (isEnemy) {
            SuppressionManager.addSuppression(0.1F);
         }
      }
   }
}