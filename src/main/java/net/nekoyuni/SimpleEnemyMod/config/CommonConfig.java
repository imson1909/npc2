package net.nekoyuni.SimpleEnemyMod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.nekoyuni.SimpleEnemyMod.config.common.AiDifficultyConfig;
import net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig;
import net.nekoyuni.SimpleEnemyMod.config.common.DropsConfig;
import net.nekoyuni.SimpleEnemyMod.config.common.EffectConfig;
import net.nekoyuni.SimpleEnemyMod.config.common.EventSpawnConfig;
import net.nekoyuni.SimpleEnemyMod.config.common.FactionsConfig;
import net.nekoyuni.SimpleEnemyMod.config.common.GeneralConfig;
import net.nekoyuni.SimpleEnemyMod.config.common.MiscConfig;
import net.nekoyuni.SimpleEnemyMod.config.common.UnitAttributesConfig;

public final class CommonConfig {
   public static final ForgeConfigSpec SPEC;

   static {
      Builder builder = new Builder();
      GeneralConfig.init(builder);
      FactionsConfig.init(builder);
      UnitAttributesConfig.init(builder);
      AiDifficultyConfig.init(builder);
      AiShootingConfig.init(builder);
      EventSpawnConfig.init(builder);
      DropsConfig.init(builder);
      EffectConfig.init(builder);
      MiscConfig.init(builder);
      SPEC = builder.build();
   }
}