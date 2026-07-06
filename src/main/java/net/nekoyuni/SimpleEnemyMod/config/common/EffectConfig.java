package net.nekoyuni.SimpleEnemyMod.config.common;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class EffectConfig {
   public static BooleanValue ENABLE_SUPPRESSION;

   public static void init(Builder builder) {
      builder.push("Visual Effects");
      ENABLE_SUPPRESSION = builder.comment("Activate or deactivate the visual suppression effect").define("enableSuppression", true);
      builder.pop();
   }
}