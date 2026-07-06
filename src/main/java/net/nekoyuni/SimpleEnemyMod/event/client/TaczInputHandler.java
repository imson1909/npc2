package net.nekoyuni.SimpleEnemyMod.event.client;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nekoyuni.SimpleEnemyMod.client.gui.screens.CommanderMenuScreen;

@Mod.EventBusSubscriber(modid = "simpleenemymod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TaczInputHandler {

   @SubscribeEvent
   public static void onClientTick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
      if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
         return;
      }

      net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
      if (mc.player == null || mc.screen == null) {
         return;
      }

      if (mc.screen instanceof CommanderMenuScreen) {
         boolean justClosedMenu = CommanderMenuScreen.shouldSuppressFire();
         if (justClosedMenu) {
            // Suppress fire for a few ticks after closing menu
         }
      }
   }
}