package net.nekoyuni.SimpleEnemyMod.event.common;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.nekoyuni.SimpleEnemyMod.registry.ModCommands;

@EventBusSubscriber(modid = "simpleenemymod", bus = Bus.FORGE)
public class CommandRegistrationHandler {
   @SubscribeEvent
   public static void onRegisterCommands(RegisterCommandsEvent event) {
      ModCommands.register(event.getDispatcher());
   }
}
