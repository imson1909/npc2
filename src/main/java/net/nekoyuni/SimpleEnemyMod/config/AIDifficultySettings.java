package net.nekoyuni.SimpleEnemyMod.config;

import net.nekoyuni.SimpleEnemyMod.config.common.AiDifficultyConfig;

public class AIDifficultySettings {
   public final long patienceTimeout;
   public final long flankingDuration;
   public final long coverWaitTime;
   public final double flankSpeed;
   public final double rushSpeed;
   public final double midRangeSpeed;

   private AIDifficultySettings(long patience, long flanking, long cover, double flank, double rush, double midRange) {
      this.patienceTimeout = patience;
      this.flankingDuration = flanking;
      this.coverWaitTime = cover;
      this.flankSpeed = flank;
      this.rushSpeed = rush;
      this.midRangeSpeed = midRange;
   }

   public static AIDifficultySettings fromConfig() {
      return switch ((AiDifficultyConfig.AIDifficulty)AiDifficultyConfig.DIFFICULTY.get()) {
         case NORMAL -> new AIDifficultySettings(80L, 1200L, 40L, 1.15, 1.3, 1.15);
         case ADVANCED -> new AIDifficultySettings(40L, 1200L, 15L, 1.35, 1.45, 1.2);
      };
   }
}