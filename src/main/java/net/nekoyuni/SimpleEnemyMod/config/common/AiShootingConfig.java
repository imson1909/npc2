package net.nekoyuni.SimpleEnemyMod.config.common;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class AiShootingConfig {
   public static DoubleValue MAX_SHOOT_DISTANCE;
   public static DoubleValue BASE_SPREAD;
   public static DoubleValue SPREAD_INCREASE;
   public static DoubleValue MAX_SPREAD;
   public static IntValue MIN_BURST;
   public static IntValue MAX_BURST;
   public static IntValue MIN_BURST_COOLDOWN;
   public static IntValue MAX_BURST_COOLDOWN;

   public static void init(Builder builder) {
      builder.push("gun_ai_shooting_config");
      MAX_SHOOT_DISTANCE = builder.comment("Maximum shooting distance").defineInRange("maxShootDistance", 90.0, 10.0, 384.0);
      BASE_SPREAD = builder.comment("Base bullet spread (lower = more accurate)").defineInRange("baseSpread", 1.4, 0.0, 10.0);
      SPREAD_INCREASE = builder.comment("Spread increase per block of distance to target (accuracy penalty at range)")
              .defineInRange("spreadIncrease", 0.012, 0.0, 1.0);
      MAX_SPREAD = builder.comment(new String[]{"Maximum bullet spread limit", "Prevents accuracy penalties from increasing indefinitely at long range"})
              .defineInRange("maxSpread", 1.2, 0.0, 10.0);
      MIN_BURST = builder.comment("Minimum shots per burst").defineInRange("minBurst", 3, 1, 20);
      MAX_BURST = builder.comment("Maximum shots per burst").defineInRange("maxBurst", 5, 1, 30);
      MIN_BURST_COOLDOWN = builder.comment("Minimum cooldown between bursts (ticks)").defineInRange("minBurstCooldown", 10, 0, 200);
      MAX_BURST_COOLDOWN = builder.comment("Maximum cooldown between bursts (ticks)").defineInRange("maxBurstCooldown", 15, 0, 400);
      builder.pop();
   }
}