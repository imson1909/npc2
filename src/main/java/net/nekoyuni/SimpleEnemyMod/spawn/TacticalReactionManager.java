package net.nekoyuni.SimpleEnemyMod.spawn;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class TacticalReactionManager {
   private static final Queue<Runnable> pendingReactions = new ConcurrentLinkedQueue<>();
   private static final int MAX_REACTIONS_PER_TICK = 5;

   public static void queueReaction(Runnable reaction) {
      pendingReactions.add(reaction);
   }

   @SubscribeEvent
   public static void onServerTick(ServerTickEvent event) {
      if (event.phase == Phase.END) {
         processQueue();
      }
   }

   private static void processQueue() {
      for (int processedCount = 0; !pendingReactions.isEmpty() && processedCount < 5; processedCount++) {
         Runnable task = pendingReactions.poll();
         if (task != null) {
            try {
               task.run();
            } catch (Exception var3) {
            }
         }
      }
   }
}
