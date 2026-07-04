package net.nekoyuni.SimpleEnemyMod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.nekoyuni.SimpleEnemyMod.client.gui.screens.PmcUnitScreen;
import net.nekoyuni.SimpleEnemyMod.compat.cloth.ClothConfigCompat;
import net.nekoyuni.SimpleEnemyMod.compat.cloth.ClothConfigScreenHelper;
import net.nekoyuni.SimpleEnemyMod.config.ModConfigs;
import net.nekoyuni.SimpleEnemyMod.entity.client.pmc_unit.PmcUnitRenderer;
import net.nekoyuni.SimpleEnemyMod.network.ModNetworking;
import net.nekoyuni.SimpleEnemyMod.procedural.events.DynamicEventManager;
import net.nekoyuni.SimpleEnemyMod.registry.ModCreativeTabs;
import net.nekoyuni.SimpleEnemyMod.registry.ModEntities;
import net.nekoyuni.SimpleEnemyMod.registry.ModItems;
import net.nekoyuni.SimpleEnemyMod.registry.ModMenuTypes;
import net.nekoyuni.SimpleEnemyMod.registry.ModSounds;
import org.slf4j.Logger;

@Mod("simpleenemymod")
public class SimpleEnemyMod {
   public static final String MODID = "simpleenemymod";
   private static final Logger LOGGER = LogUtils.getLogger();

   public SimpleEnemyMod(FMLJavaModLoadingContext context) {
      IEventBus modEventBus = context.getModEventBus();
      modEventBus.addListener(this::commonSetup);
      modEventBus.addListener(this::addCreative);
      ModItems.register(modEventBus);
      ModCreativeTabs.register(modEventBus);
      ModEntities.register(modEventBus);
      ModSounds.register(modEventBus);
      ModMenuTypes.register(modEventBus);
      ModConfigs.register(context);
      DynamicEventManager.register();
      if (ClothConfigCompat.LOADED) {
         context.registerExtensionPoint(
                 ConfigScreenFactory.class, () -> new ConfigScreenFactory((minecraft, parentScreen) -> ClothConfigScreenHelper.createConfigScreen(parentScreen))
         );
      }
   }

   private void commonSetup(FMLCommonSetupEvent event) {
      ModNetworking.register();
   }

   private void addCreative(BuildCreativeModeTabContentsEvent event) {
      if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
         event.accept(ModItems.PMC_UNIT_SPAWN_EGG);
         // NEW: Add faction spawn eggs to creative tab
         event.accept(ModItems.TOWER_UNIT_SPAWN_EGG);
         event.accept(ModItems.BUNKER_UNIT_SPAWN_EGG);
      }
   }

   @SubscribeEvent
   public void onServerStarting(ServerStartingEvent event) {
      LOGGER.info("NYAHELLO from server starting");
   }

   @EventBusSubscriber(modid = "simpleenemymod", bus = Bus.MOD, value = Dist.CLIENT)
   public static class ClientModEvents {
      @SubscribeEvent
      public static void onClientSetup(FMLClientSetupEvent event) {
         EntityRenderers.register((EntityType)ModEntities.PMCUNIT.get(), PmcUnitRenderer::new);
         MenuScreens.register((MenuType)ModMenuTypes.PMC_UNIT_MENU.get(), PmcUnitScreen::new);
      }
   }
}