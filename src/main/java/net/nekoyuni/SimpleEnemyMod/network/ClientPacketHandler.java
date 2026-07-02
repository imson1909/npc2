package net.nekoyuni.SimpleEnemyMod.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.client.system.SuppressionManager;
import net.nekoyuni.SimpleEnemyMod.network.packets.PacketPlayImpactSound;
import net.nekoyuni.SimpleEnemyMod.network.packets.PacketSuppression;
import net.nekoyuni.SimpleEnemyMod.registry.ModSounds;
import net.nekoyuni.SimpleEnemyMod.sound.BulletImpactSoundInstance;

public class ClientPacketHandler {
   private static final Map<String, Long> soundHistory = new ConcurrentHashMap<>();
   private static final long DUPLICATE_THRESHOLD_MS = 1000L;
   private static long lastCleanupTime = 0L;

   public static void handleImpactSound(PacketPlayImpactSound msg) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level != null && mc.player != null) {
         long clientTime = System.currentTimeMillis();
         long latency = clientTime - msg.getTimestamp();
         String soundKey = generateSoundKey(msg.getX(), msg.getY(), msg.getZ(), msg.getVolume(), msg.getPitch());
         if (clientTime - lastCleanupTime > 2000L) {
            cleanupSoundHistory(clientTime);
            lastCleanupTime = clientTime;
         }

         Long lastPlayTime = soundHistory.get(soundKey);
         if (lastPlayTime == null || clientTime - lastPlayTime >= 1000L) {
            if (latency <= 300L) {
               Vec3 impactPos = new Vec3(msg.getX(), msg.getY(), msg.getZ());
               if (!(mc.player.position().distanceTo(impactPos) > 60.0)) {
                  soundHistory.put(soundKey, clientTime);

                  try {
                     BulletImpactSoundInstance soundInstance = new BulletImpactSoundInstance(
                        (SoundEvent)ModSounds.SOUND_BULLET_IMPACT.get(), msg.getSource(), msg.getVolume(), msg.getPitch(), impactPos
                     );
                     mc.getSoundManager().play(soundInstance);
                  } catch (Exception e) {
                     System.out.printf("\u001b[31m[SoundDebug] Error triying to rerproduce the sound: %s\u001b[0m%n", e.getMessage());
                     soundHistory.remove(soundKey);
                  }
               }
            }
         }
      } else {
         System.out.println("\u001b[33m[SoundDebug] Client not ready to reproduce\u001b[0m");
      }
   }

   public static void handleSuppression(PacketSuppression msg) {
      SuppressionManager.addSuppression(msg.getAmount());
   }

   private static String generateSoundKey(double x, double y, double z, float volume, float pitch) {
      return String.format(
         "%d:%d:%d:%d:%d",
         (int)Math.round(x * 10.0),
         (int)Math.round(y * 10.0),
         (int)Math.round(z * 10.0),
         Math.round(volume * 100.0F),
         Math.round(pitch * 100.0F)
      );
   }

   private static void cleanupSoundHistory(long currentTime) {
      soundHistory.entrySet().removeIf(entry -> currentTime - entry.getValue() > 1000L);
   }
}
