package net.nekoyuni.SimpleEnemyMod.config.common;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;

public class AiDifficultyConfig {
   public static EnumValue<AiDifficultyConfig.AIDifficulty> DIFFICULTY;

   public static void init(Builder builder) {
      builder.push("ai_difficulty");
      DIFFICULTY = builder.comment(
                      new String[]{
                              "AI Difficulty preset.",
                              "Does NOT affect soldiers accuracy.",
                              "Only affects tactical maneuver timers and movement speeds.",
                              "",
                              "NORMAL: More static soldiers, slower combat pacing.",
                              "ADVANCED: More dynamic and aggressive soldiers."
                      }
              )
              .defineEnum("difficulty", AiDifficultyConfig.AIDifficulty.NORMAL);
      builder.pop();
   }

   public enum AIDifficulty {
      NORMAL,
      ADVANCED;
   }
}