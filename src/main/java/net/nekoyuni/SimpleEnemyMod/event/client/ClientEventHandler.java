package net.nekoyuni.SimpleEnemyMod.event.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.nekoyuni.SimpleEnemyMod.client.input.KeyBindings;
import net.nekoyuni.SimpleEnemyMod.entity.client.pmc_unit.PmcUnitModel;
import net.nekoyuni.SimpleEnemyMod.entity.client.pmc_unit.PmcUnitModelLayers;

@EventBusSubscriber(modid = "simpleenemymod", bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
   @SubscribeEvent
   public static void onRegisterLayerDefinitions(RegisterLayerDefinitions event) {
      event.registerLayerDefinition(PmcUnitModelLayers.PMCUNIT_LAYER, PmcUnitModel::createBodyLayer);
   }

   @SubscribeEvent
   public static void onKeyRegister(RegisterKeyMappingsEvent event) {
      event.register(KeyBindings.COMMANDER_MENU_KEY);
   }
}
