package net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils;

import net.minecraft.util.RandomSource;
import net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig;

public final class AiGunSpreadHelper {
   public static float CalculateSpread(float idealAngle, float distance, float baseSpread, float spreadIncreasePerBlock, RandomSource random) {
      float spread = baseSpread + Math.max(0.0F, distance - 5.0F) * spreadIncreasePerBlock;
      spread = Math.min(spread, ((Double)AiShootingConfig.MAX_SPREAD.get()).floatValue());
      return idealAngle + (random.nextFloat() - 0.5F) * 2.0F * spread;
   }
}
