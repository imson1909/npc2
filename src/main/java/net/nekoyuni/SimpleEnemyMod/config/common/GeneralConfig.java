package net.nekoyuni.SimpleEnemyMod.config.common;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent.Loading;
import net.minecraftforge.fml.event.config.ModConfigEvent.Reloading;
import net.nekoyuni.SimpleEnemyMod.config.CommonConfig;

@EventBusSubscriber(modid = "simpleenemymod", bus = Bus.MOD)
public class GeneralConfig {
   public static ConfigValue<Boolean> ENABLE_VILLAGE_GARRISON_CONFIG;
   public static boolean enableVillageGarrison = true;

   public static void init(Builder builder) {
      builder.push("general_settings");
      ENABLE_VILLAGE_GARRISON_CONFIG = builder.comment("When set to true, soldiers will spawn in villages. When set to false, the event is disabled")
              .define("enableVillageGarrison", true);
      builder.pop();
   }

   @SubscribeEvent
   public static void onLoad(Loading event) {
      if (event.getConfig().getSpec() == CommonConfig.SPEC) {
         enableVillageGarrison = (Boolean)ENABLE_VILLAGE_GARRISON_CONFIG.get();
      }
   }

   @SubscribeEvent
   public static void onReload(Reloading event) {
      if (event.getConfig().getSpec() == CommonConfig.SPEC) {
         enableVillageGarrison = (Boolean)ENABLE_VILLAGE_GARRISON_CONFIG.get();
      }
   }
}