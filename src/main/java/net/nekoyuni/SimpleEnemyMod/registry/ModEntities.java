package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;

public class ModEntities {
   public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "simpleenemymod");
   public static final RegistryObject<EntityType<PmcUnitEntity>> PMCUNIT = ENTITY_TYPES.register(
           "pmcunit", () -> EntityType.Builder.of(PmcUnitEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(256).build("pmcunit")
   );

   public static void register(IEventBus eventBus) {
      ENTITY_TYPES.register(eventBus);
   }
}