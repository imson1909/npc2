package net.nekoyuni.SimpleEnemyMod.client.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.Key;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.client.gui.screens.CommanderMenuScreen;

@EventBusSubscriber(modid = "simpleenemymod", value = Dist.CLIENT)
public class KeyInputHandler {
   @SubscribeEvent
   public static void onKeyInput(Key event) {
      if (KeyBindings.COMMANDER_MENU_KEY.isDown() && Screen.hasShiftDown()) {
         Minecraft.getInstance().setScreen(new CommanderMenuScreen());
      }
   }
}