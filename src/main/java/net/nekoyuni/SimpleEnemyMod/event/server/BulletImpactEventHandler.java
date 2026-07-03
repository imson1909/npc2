package net.nekoyuni.SimpleEnemyMod.event.server;

import com.tacz.guns.api.event.server.AmmoHitBlockEvent;
import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.spawn.TacticalReactionManager;

@EventBusSubscriber
public class BulletImpactEventHandler {
   private static final double SUPPRESSION_RADIUS = 7.0;

   @SubscribeEvent
   public static void onBulletHitBlock(AmmoHitBlockEvent event) {
      EntityKineticBullet bullet = event.getAmmo();
      if (bullet.getOwner() instanceof LivingEntity shooter) {
         double x = event.getHitResult().getLocation().x;
         double y = event.getHitResult().getLocation().y;
         double z = event.getHitResult().getLocation().z;
         AABB impactBox = new AABB(x - 7.0, y - 7.0, z - 7.0, x + 7.0, y + 7.0, z + 7.0);

         for (PathfinderMob npc : event.getLevel().getEntitiesOfClass(PathfinderMob.class, impactBox)) {
            if (npc.isAlive()
               && npc != shooter
               && npc.getClass() != shooter.getClass()
               && npc instanceof AbstractUnit
               && (!(npc instanceof PmcUnitEntity) || !(shooter instanceof Player))
               && npc.getTarget() == null) {
               TacticalReactionManager.queueReaction(() -> {
                  if (npc.isAlive()) {
                     npc.setTarget(shooter);
                     npc.setGlowingTag(false);
                  }
               });
            }
         }
      }
   }
}
