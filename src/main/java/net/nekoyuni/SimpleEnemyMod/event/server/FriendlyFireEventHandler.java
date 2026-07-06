package net.nekoyuni.SimpleEnemyMod.event.server;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;

@EventBusSubscriber(modid = "simpleenemymod")
public class FriendlyFireEventHandler {
   @SubscribeEvent
   public static void onLivingAttack(LivingAttackEvent event) {
      LivingEntity victim = event.getEntity();
      LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity ? (LivingEntity)event.getSource().getEntity() : null;
      if (attacker != null && victim instanceof PmcUnitEntity && attacker instanceof PmcUnitEntity) {
         event.setCanceled(true);
      }
   }
}
