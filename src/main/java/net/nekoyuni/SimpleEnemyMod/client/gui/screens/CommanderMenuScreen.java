package net.nekoyuni.SimpleEnemyMod.client.gui.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CommanderMenuScreen extends Screen {
   private static boolean suppressFire = false;

   public CommanderMenuScreen() {
      super(Component.literal("Commander Menu"));
   }

   public static boolean shouldSuppressFire() {
      return suppressFire;
   }

   @Override
   protected void init() {
      this.minecraft.setScreen(null);
   }

   @Override
   public void render(net.minecraft.client.gui.GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      this.minecraft.setScreen(null);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }
}