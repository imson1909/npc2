package net.nekoyuni.SimpleEnemyMod.event.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.MouseButton.Pre;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.client.gui.overlay.CommanderOverlayRenderer;
import net.nekoyuni.SimpleEnemyMod.client.util.CommanderRayTrace;
import net.nekoyuni.SimpleEnemyMod.entity.ai.orders.OrderType;
import net.nekoyuni.SimpleEnemyMod.network.ModNetworking;
import net.nekoyuni.SimpleEnemyMod.network.packets.PacketIssueOrder;

@EventBusSubscriber(modid = "simpleenemymod", value = Dist.CLIENT)
public class ClientClickEventHandler {
   @SubscribeEvent
   public static void onMouseInput(Pre event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null) {
         if (CommanderOverlayRenderer.isSelectingPosition) {
            handleMoveSelection(event, mc);
         } else if (CommanderOverlayRenderer.isSelectingTarget) {
            handleAttackSelection(event, mc);
         }
      }
   }

   private static void handleMoveSelection(Pre event, Minecraft mc) {
      if (event.getButton() == 0 && event.getAction() == 1) {
         BlockHitResult result = CommanderRayTrace.rayTrace(mc.player, 45.0);
         if (CommanderRayTrace.isValidMoveTarget(result)) {
            sendMoveToOrder(result.getLocation());
            CommanderOverlayRenderer.isSelectingPosition = false;
            event.setCanceled(true);
            mc.player.displayClientMessage(Component.literal("§aMove Order Sent!"), true);
         }
      }

      if (event.getButton() == 1 && event.getAction() == 1) {
         CommanderOverlayRenderer.isSelectingPosition = false;
         event.setCanceled(true);
      }
   }

   private static void handleAttackSelection(Pre event, Minecraft mc) {
      if (event.getButton() == 0 && event.getAction() == 1) {
         Entity target = CommanderRayTrace.rayTraceEntity(mc.player, 50.0);
         if (target != null) {
            sendAttackOrder(target.getId());
            CommanderOverlayRenderer.isSelectingTarget = false;
            event.setCanceled(true);
            mc.player.displayClientMessage(Component.literal("§aTarget Designated: " + target.getName().getString()), true);
         }
      }

      if (event.getButton() == 1 && event.getAction() == 1) {
         CommanderOverlayRenderer.isSelectingTarget = false;
         event.setCanceled(true);
      }
   }

   private static void sendMoveToOrder(Vec3 pos) {
      Set<Integer> targets = CommanderOverlayRenderer.selectedUnitsSnapshot;
      if (targets != null && !targets.isEmpty()) {
         List<Integer> sortedIds = new ArrayList<>(targets);
         sortedIds.sort((id1, id2) -> Integer.compare(id2, id1));

         for (int i = 0; i < sortedIds.size(); i++) {
            int newIndex = i;
            ModNetworking.sendToServer(new PacketIssueOrder(sortedIds.get(i), OrderType.MOVE_TO_POSITION, pos, newIndex, -1));
         }

         CommanderOverlayRenderer.selectedUnitsSnapshot.clear();
      }
   }

   private static void sendAttackOrder(int targetId) {
      Set<Integer> targets = CommanderOverlayRenderer.selectedUnitsSnapshot;
      if (targets != null && !targets.isEmpty()) {
         List<Integer> sortedIds = new ArrayList<>(targets);
         sortedIds.sort((id1, id2) -> Integer.compare(id2, id1));

         for (int i = 0; i < sortedIds.size(); i++) {
            int newIndex = i;
            ModNetworking.sendToServer(new PacketIssueOrder(sortedIds.get(i), OrderType.ATTACK_THAT_TARGET, Vec3.ZERO, newIndex, targetId));
         }

         CommanderOverlayRenderer.selectedUnitsSnapshot.clear();
      }
   }
}
