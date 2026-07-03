package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.nekoyuni.SimpleEnemyMod.compat.curios.CuriosCompat;
import net.nekoyuni.SimpleEnemyMod.compat.curios.SimpleDataGenerators;

@EventBusSubscriber(modid = "simpleenemymod", bus = Bus.MOD)
public class ModDataGenerators {
   @SubscribeEvent
   public static void gatherData(GatherDataEvent event) {
      if (CuriosCompat.LOADED) {
         SimpleDataGenerators.onGatherData(event);
      }
   }
}
