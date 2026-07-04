package net.nekoyuni.SimpleEnemyMod.client.system;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "simpleenemymod", value = Dist.CLIENT)
public class SuppressionManager {
   public static float suppressionLevel = 0.0F;
   private static final float DECAY_RATE = 0.0025F;

   public static void addSuppression(float amount) {
      suppressionLevel += amount;
      if (suppressionLevel > 1.0F) {
         suppressionLevel = 1.0F;
      }
   }

   @SubscribeEvent
   public static void onClientTick(ClientTickEvent event) {
      if (event.phase == Phase.END && suppressionLevel > 0.0F) {
         suppressionLevel -= DECAY_RATE;
         if (suppressionLevel < 0.0F) {
            suppressionLevel = 0.0F;
         }
      }
   }
}