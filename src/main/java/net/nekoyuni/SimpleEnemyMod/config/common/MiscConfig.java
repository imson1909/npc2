package net.nekoyuni.SimpleEnemyMod.config.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;

public class MiscConfig {
   public static ConfigValue<String> RECRUIT_ITEM;
   public static IntValue RECRUIT_PRICE;

   public static void init(Builder builder) {
      builder.push("misc_config");
      RECRUIT_ITEM = builder.comment("Item ID required to recruit a PMC").define("recruitItem", "minecraft:emerald");
      RECRUIT_PRICE = builder.comment("Amount of items required to recruit a PMC").defineInRange("recruitPrice", 16, 1, 64);
      builder.pop();
   }

   public static Item getRecruitItem() {
      ResourceLocation id = ResourceLocation.tryParse((String)RECRUIT_ITEM.get());
      return (Item)ForgeRegistries.ITEMS.getValue(id);
   }
}