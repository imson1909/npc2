package net.nekoyuni.SimpleEnemyMod.procedural.events.system;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.nekoyuni.SimpleEnemyMod.config.common.EventSpawnConfig;

public class EventCooldownManager {
   private static final int DEFAULT_COOLDOWN_TICKS = 600;
   private final Map<UUID, Integer> playerCooldowns = new HashMap<>();

   public void tick(Iterable<ServerPlayer> onlinePlayers) {
      for (ServerPlayer player : onlinePlayers) {
         UUID uuid = player.getUUID();
         int current = this.playerCooldowns.getOrDefault(uuid, 0);
         if (current > 0) {
            this.playerCooldowns.put(uuid, current - 1);
         }
      }
   }

   public boolean isOnCooldown(ServerPlayer player) {
      return this.playerCooldowns.getOrDefault(player.getUUID(), 0) > 0;
   }

   public void triggerCooldown(ServerPlayer player) {
      this.playerCooldowns.put(player.getUUID(), this.getCooldownTicks());
   }

   public void resetCooldown(ServerPlayer player) {
      this.playerCooldowns.remove(player.getUUID());
   }

   public void onPlayerDisconnect(ServerPlayer player) {
      this.playerCooldowns.remove(player.getUUID());
   }

   public int getRemainingTicks(ServerPlayer player) {
      return this.playerCooldowns.getOrDefault(player.getUUID(), 0);
   }

   private int getCooldownTicks() {
      try {
         return (Integer)EventSpawnConfig.EVENT_GLOBAL_COOLDOWN_TICKS.get();
      } catch (Exception e) {
         return 600;
      }
   }
}
