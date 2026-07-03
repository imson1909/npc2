package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
   public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "simpleenemymod");
   public static final RegistryObject<SoundEvent> SOUND_BULLET_IMPACT = registerSoundEvents("sound_bullet_impact");
   public static final RegistryObject<SoundEvent> SOUND_PMC_UNIT_ALERT = registerSoundEvents("entity.pmc_unit.alert");

   private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
      return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("simpleenemymod", name)));
   }

   public static void register(IEventBus eventBus) {
      SOUND_EVENTS.register(eventBus);
   }
}