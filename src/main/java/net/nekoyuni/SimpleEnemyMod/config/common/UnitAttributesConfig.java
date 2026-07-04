package net.nekoyuni.SimpleEnemyMod.config.common;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class UnitAttributesConfig {
   public static DoubleValue UNIT_HEALTH;
   public static DoubleValue UNIT_SPEED;
   public static DoubleValue UNIT_DETECTION_RANGE;

   public static void init(Builder builder) {
      builder.push("unit_attributes");
      UNIT_HEALTH = builder.comment("Max Health for All Units").defineInRange("health", 20.0, 1.0, 200.0);
      builder.comment("--------------------------------------------------");
      UNIT_SPEED = builder.comment("Walk Speed for All Units").defineInRange("speed", 0.27, 0.05, 1.5);
      builder.comment("--------------------------------------------------");
      UNIT_DETECTION_RANGE = builder.comment("Detection Range for All Units").defineInRange("detectionRange", 96.0, 32.0, 384.0);
      builder.pop();
   }
}