package net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainScanner {
   public static boolean isWall(Level world, BlockPos center, int range) {
      for (int x = -range; x <= range; x += 2) {
         for (int z = -range; z <= range; z += 2) {
            BlockPos scanPos = center.offset(x, 0, z);
            if (hasWallStructure(world, scanPos)) {
               return true;
            }
         }
      }

      return false;
   }

   public static TerrainScanner.FormationType getFormationType(Level world, BlockPos leaderPos, int scanRange) {
      return isWall(world, leaderPos, scanRange) ? TerrainScanner.FormationType.COLUMN : TerrainScanner.FormationType.WEDGE;
   }

   private static boolean hasWallStructure(Level world, BlockPos pos) {
      int solidBlocks = 0;
      int requiredBlocks = 6;

      for (int width = 0; width < 3; width++) {
         for (int height = 0; height < 2; height++) {
            BlockPos checkPos = pos.offset(width, height, 0);
            if (isSolidBlock(world, checkPos)) {
               solidBlocks++;
            }
         }
      }

      if (solidBlocks < requiredBlocks) {
         solidBlocks = 0;

         for (int depth = 0; depth < 3; depth++) {
            for (int height = 0; height < 2; height++) {
               BlockPos checkPos = pos.offset(0, height, depth);
               if (isSolidBlock(world, checkPos)) {
                  solidBlocks++;
               }
            }
         }
      }

      return solidBlocks >= requiredBlocks;
   }

   private static boolean isSolidBlock(Level world, BlockPos pos) {
      BlockState state = world.getBlockState(pos);
      return !state.isAir() && state.isCollisionShapeFullBlock(world, pos) && state.getFluidState().isEmpty();
   }

   public static int getScanInterval() {
      return 100;
   }

   public enum FormationType {
      COLUMN,
      WEDGE;
   }
}
