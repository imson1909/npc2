package net.nekoyuni.SimpleEnemyMod.event.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.procedural.events.DynamicEventManager;

@EventBusSubscriber(modid = "simpleenemymod")
public class SpawnEventHandler {
   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         MinecraftServer server = event.getServer();
         ServerLevel overworld = server.getLevel(Level.OVERWORLD);
         if (overworld != null) {
            DynamicEventManager.tick(overworld);
         }
      }
   }
}
