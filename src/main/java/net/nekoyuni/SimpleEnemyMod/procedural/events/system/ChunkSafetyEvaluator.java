package net.nekoyuni.SimpleEnemyMod.procedural.events.system;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ChunkSafetyEvaluator {
   private static final int SAFETY_THRESHOLD = 6;
   private static final int SCAN_RADIUS = 24;
   private static final int SCAN_STEP_XZ = 3;
   private static final int SCAN_STEP_Y = 2;
   private static final int SCAN_HALF_Y = 8;
   private static final int MIN_DIST_FROM_ANY_PLAYER = 32;

   public static boolean isPlayerBase(ServerLevel level, BlockPos targetPos) {
      for (ServerPlayer player : level.players()) {
         double distSq = player.blockPosition().distSqr(targetPos);
         if (distSq < 1024.0) {
            return true;
         }
      }

      return hasBaseBlocks(level, targetPos);
   }

   private static boolean hasBaseBlocks(ServerLevel level, BlockPos targetPos) {
      int score = 0;

      for (int x = -24; x <= 24; x += 3) {
         for (int z = -24; z <= 24; z += 3) {
            for (int y = -8; y <= 8; y += 2) {
               BlockState state = level.getBlockState(targetPos.offset(x, y, z));
               score += scoreBlock(state);
               if (score >= 6) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private static int scoreBlock(BlockState state) {
      if (state.is(Blocks.STONE)
         || state.is(Blocks.GRASS_BLOCK)
         || state.is(Blocks.DIRT)
         || state.is(Blocks.COARSE_DIRT)
         || state.is(Blocks.PODZOL)
         || state.is(Blocks.GRANITE)
         || state.is(Blocks.DIORITE)
         || state.is(Blocks.ANDESITE)
         || state.is(Blocks.DEEPSLATE)
         || state.is(Blocks.COBBLED_DEEPSLATE)
         || state.is(Blocks.CALCITE)
         || state.is(Blocks.TUFF)
         || state.is(Blocks.DRIPSTONE_BLOCK)
         || state.is(Blocks.MYCELIUM)
         || state.is(Blocks.MOSS_BLOCK)
         || state.is(Blocks.FARMLAND)) {
         return 5;
      } else if (state.is(Blocks.LAVA)) {
         return 3;
      } else if (state.is(Blocks.FIRE)) {
         return 3;
      } else if (state.is(Blocks.CACTUS) || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.CAMPFIRE)) {
         return 2;
      } else if (state.is(Blocks.SOUL_FIRE) || state.is(Blocks.SOUL_CAMPFIRE) || state.is(Blocks.TORCH)) {
         return 2;
      } else if (state.is(Blocks.BAMBOO) || state.is(Blocks.VINE) || state.is(Blocks.CAVE_VINES)) {
         return 2;
      } else if (state.is(Blocks.OAK_LEAVES)
         || state.is(Blocks.AZALEA)
         || state.is(Blocks.FLOWERING_AZALEA)
         || state.is(Blocks.MOSS_CARPET)
         || state.is(Blocks.PINK_PETALS)
         || state.is(Blocks.CHERRY_LEAVES)
         || state.is(Blocks.SPORE_BLOSSOM)
         || state.is(Blocks.PITCHER_PLANT)
         || state.is(Blocks.BIG_DRIPLEAF)) {
         return 2;
      } else if (state.is(Blocks.WATER)) {
         return 1;
      } else {
         return !state.is(Blocks.KELP) && !state.is(Blocks.SEAGRASS) && !state.is(Blocks.TALL_SEAGRASS) && !state.is(Blocks.BUBBLE_COLUMN)
            ? 0
            : 1;
      }
   }
}
