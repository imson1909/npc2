package net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class AiGunSpreadHelper {

   public static Vec3 applySpread(Vec3 direction, float spread, RandomSource random) {
      if (spread <= 0) return direction;

      // Create a random offset perpendicular to the direction
      Vec3 up = new Vec3(0, 1, 0);
      Vec3 right = direction.cross(up).normalize();
      if (right.lengthSqr() < 0.001) {
         right = new Vec3(1, 0, 0).cross(direction).normalize();
      }
      Vec3 newUp = direction.cross(right).normalize();

      // Random angles with bias toward center (Gaussian-like distribution)
      float angle1 = (random.nextFloat() - 0.5f) * 2 * spread;
      float angle2 = (random.nextFloat() - 0.5f) * 2 * spread;

      // Apply spread
      Vec3 result = direction.add(right.scale(angle1)).add(newUp.scale(angle2));
      return result.normalize();
   }

   public static Vec3 applySpreadWithDistance(Vec3 direction, float baseSpread, double distance,
                                              RandomSource random) {
      float distanceFactor = (float) Math.min(1.0, distance / 100.0);
      float adjustedSpread = baseSpread * (1.0f + distanceFactor);
      return applySpread(direction, adjustedSpread, random);
   }
}