package net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils;

import net.minecraft.world.phys.Vec3;

public class FormationUtils {
   public static Vec3 getTargetPosition(Vec3 anchorPos, Vec3 anchorLook, int formation, int index) {
      if (formation == 0) {
         return calculateColumnPosition(anchorPos, anchorLook, index);
      } else {
         return formation == 1 ? calculateWedgePosition(anchorPos, anchorLook, index) : anchorPos;
      }
   }

   private static Vec3 calculateColumnPosition(Vec3 anchorPos, Vec3 anchorLook, int index) {
      Vec3 back = anchorLook.normalize().scale(-2.0 * (double)(index + 1));
      return anchorPos.add(back);
   }

   private static Vec3 calculateWedgePosition(Vec3 anchorPos, Vec3 anchorLook, int index) {
      Vec3 right = anchorLook.yRot(-1.5707964F).normalize();
      int row = (index + 1) / 2;
      int side = (index + 1) % 2 == 0 ? 1 : -1;
      Vec3 forward = anchorLook.normalize().scale(-1.5 * (double)row);
      Vec3 lateral = right.scale(2.0 * (double)row * (double)side);
      return anchorPos.add(forward).add(lateral);
   }
}