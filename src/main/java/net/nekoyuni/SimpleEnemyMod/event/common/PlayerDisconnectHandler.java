package net.nekoyuni.SimpleEnemyMod.event.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.nekoyuni.SimpleEnemyMod.procedural.events.DynamicEventManager;

@EventBusSubscriber(modid = "simpleenemymod", bus = Bus.FORGE)
public class PlayerDisconnectHandler {
   @SubscribeEvent
   public static void onPlayerDisconnect(PlayerLoggedOutEvent event) {
      if (event.getEntity() instanceof ServerPlayer serverPlayer) {
         DynamicEventManager.onPlayerDisconnect(serverPlayer);
      }
   }
}
