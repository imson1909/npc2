package net.nekoyuni.SimpleEnemyMod.event.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.client.system.ClientGlowManager;
import org.joml.Vector3f;

@EventBusSubscriber(modid = "simpleenemymod", value = Dist.CLIENT)
public class ClientGlowRenderHandler {
   private static final int PARTICLE_COUNT = 12;
   private static final double RADIUS = 0.6;
   private static final int TICK_INTERVAL = 5;
   private static int tickCounter = 0;

   @SubscribeEvent
   public static void onClientTick(ClientTickEvent event) {
      if (event.phase == Phase.END) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.level != null) {
            tickCounter++;
            if (tickCounter % 5 == 0) {
               for (int id : ClientGlowManager.getAll()) {
                  Entity entity = mc.level.getEntity(id);
                  if (entity != null) {
                     spawnCircle(mc.level, entity);
                  }
               }
            }
         }
      }
   }

   private static void spawnCircle(Level level, Entity entity) {
      for (int i = 0; i < 12; i++) {
         double angle = (Math.PI / 6) * i;
         double offsetX = Math.cos(angle) * 0.6;
         double offsetZ = Math.sin(angle) * 0.6;
         level.addParticle(
            new DustParticleOptions(new Vector3f(0.0F, 1.0F, 0.3F), 1.0F),
            entity.getX() + offsetX,
            entity.getY() + 0.05,
            entity.getZ() + offsetZ,
            0.0,
            0.0,
            0.0
         );
      }
   }

   @SubscribeEvent
   public static void onRenderLiving(Pre<?, ?> event) {
      if (ClientGlowManager.getAll().contains(event.getEntity().getId())) {
         System.out.println("Glow event triggered for: " + event.getEntity().getId());
         event.getEntity().setGlowingTag(true);
      }
   }
}
