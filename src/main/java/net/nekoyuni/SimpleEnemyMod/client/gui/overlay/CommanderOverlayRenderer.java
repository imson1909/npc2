package net.nekoyuni.SimpleEnemyMod.client.gui.overlay;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.client.util.CommanderRayTrace;

@EventBusSubscriber(Dist.CLIENT)
public class CommanderOverlayRenderer {
   public static boolean isSelectingPosition = false;
   public static Set<Integer> selectedUnitsSnapshot = new HashSet<>();
   public static boolean isSelectingTarget = false;

   @SubscribeEvent
   public static void onRenderWorldLast(RenderLevelStageEvent event) {
      if (event.getStage() == Stage.AFTER_TRANSLUCENT_BLOCKS) {
         if (isSelectingPosition || isSelectingTarget) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player != null) {
               if (isSelectingPosition) {
                  BlockHitResult result = CommanderRayTrace.rayTrace(player, 45.0);
                  if (CommanderRayTrace.isValidMoveTarget(result)) {
                     Vec3 hitPos = result.getLocation();
                     if (player.tickCount % 2 == 0) {
                        player.level().addParticle(ParticleTypes.POOF, hitPos.x, hitPos.y + 0.1, hitPos.z, 0.0, 0.1, 0.0);
                     }
                  }
               }

               if (isSelectingTarget) {
                  Entity target = CommanderRayTrace.rayTraceEntity(player, 64.0);
                  if (target == null) {
                     return;
                  }

                  if (player.tickCount % 2 == 0) {
                     return;
                  }

                  double targetX = target.getX();
                  double targetY = target.getY() + target.getBbHeight() / 2.0F;
                  double targetZ = target.getZ();
                  player.level().addParticle(ParticleTypes.CRIT, targetX, targetY, targetZ, 0.0, 0.1, 0.0);
               }
            }
         }
      }
   }
}