package net.nekoyuni.SimpleEnemyMod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ClientConfig {
   public static final ForgeConfigSpec SPEC;
   public static IntValue RENDER_DISTANCE;

   static {
      Builder builder = new Builder();
      builder.push("unit_render_distance");
      RENDER_DISTANCE = builder.comment(new String[]{"Max Render Distance (in Blocks) for Units", "Important!!: Render Distance => Detection Range"})
         .defineInRange("renderDistance", 128, 32, 384);
      builder.pop();
      SPEC = builder.build();
   }
}