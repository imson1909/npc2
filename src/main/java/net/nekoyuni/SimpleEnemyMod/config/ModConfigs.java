package net.nekoyuni.SimpleEnemyMod.config;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ModConfigs {
   public static void register(ModLoadingContext context) {
      context.registerConfig(Type.CLIENT, ClientConfig.SPEC, "sem-client.toml");
      context.registerConfig(Type.COMMON, CommonConfig.SPEC, "sem-common.toml");
   }
}