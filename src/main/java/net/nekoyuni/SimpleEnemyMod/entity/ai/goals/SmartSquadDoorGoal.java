package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import java.util.List;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartSquadDoorGoal extends OpenDoorGoal {
   private static final Logger LOGGER = LoggerFactory.getLogger(SmartSquadDoorGoal.class);
   private final double checkRadius;
   private int cooldown = 0;
   private static final int COOLDOWN_TIME = 5;
   private static final boolean isDebug = false;

   public SmartSquadDoorGoal(PathfinderMob mob, double checkRadius) {
      super(mob, false);
      this.checkRadius = checkRadius;
   }

   public boolean canUse() {
      if (this.cooldown > 0) {
         this.cooldown--;
         return false;
      } else {
         return super.canUse();
      }
   }

   public void stop() {
      super.stop();
      if (this.isAllyBehind()) {
         debug("[DoorGoal] Ally behind detected. Leaving door OPEN.");
         this.setOpen(true);
      } else {
         debug("[DoorGoal] No ally behind. CLOSING door.");
         this.setOpen(false);
      }

      this.cooldown = 5;
   }

   private boolean isAllyBehind() {
      AABB searchBox = this.mob.getBoundingBox().inflate(this.checkRadius, 2.0, this.checkRadius);
      List<? extends Mob> allies = this.mob.level().getEntitiesOfClass(this.mob.getClass(), searchBox, entity -> entity != this.mob && entity.isAlive());
      if (allies.isEmpty()) {
         return false;
      }

      Vec3 myLook = this.mob.getForward();
      Vec3 myPos = this.mob.position();

      for (Mob ally : allies) {
         Vec3 allyDir = ally.position().subtract(myPos).normalize();
         double dot = myLook.dot(allyDir);
         if (dot < -0.2) {
            return true;
         }
      }

      return false;
   }

   private static void debug(String message, Object... args) {
   }
}
