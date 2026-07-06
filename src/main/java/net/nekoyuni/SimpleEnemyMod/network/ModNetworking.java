package net.nekoyuni.SimpleEnemyMod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.nekoyuni.SimpleEnemyMod.network.packets.PacketPlayImpactSound;
import net.nekoyuni.SimpleEnemyMod.network.packets.PacketSuppression;

public class ModNetworking {
   private static final String PROTOCOL_VERSION = "1.0.1";
   public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
           new ResourceLocation("simpleenemymod", "main"), () -> "1.0.1", "1.0.1"::equals, "1.0.1"::equals
   );

   public static void register() {
      int id = 0;
      CHANNEL.registerMessage(id++, PacketPlayImpactSound.class, PacketPlayImpactSound::encode, PacketPlayImpactSound::decode, PacketPlayImpactSound::handle);
      CHANNEL.registerMessage(id++, PacketSuppression.class, PacketSuppression::encode, PacketSuppression::decode, PacketSuppression::handle);
   }

   public static <MSG> void sendToServer(MSG message) {
      CHANNEL.sendToServer(message);
   }
}