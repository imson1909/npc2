package net.nekoyuni.SimpleEnemyMod.compat.cloth.common;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;
import net.nekoyuni.SimpleEnemyMod.config.common.EventSpawnConfig;

public class SpawnEventClothConfig {
   public static void setup(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
      ConfigCategory spawnEvents = builder.getOrCreateCategory(Component.translatable("config.simpleenemymod.cat.spawn_event"));
      spawnEvents.addEntry(
         entryBuilder.startDoubleField(
               Component.translatable("config.simpleenemymod.spawn_event.patrol_base_chance"), (Double)EventSpawnConfig.PATROL_BASE_CHANCE.get()
            )
            .setDefaultValue(0.1)
            .setMin(0.01)
            .setMax(1.0)
            .setTooltip(new Component[]{Component.translatable("config.simpleenemymod.spawn_event.patrol_base_chance.tooltip")})
            .setSaveConsumer(newValue -> EventSpawnConfig.PATROL_BASE_CHANCE.set(newValue))
            .build()
      );
      spawnEvents.addEntry(
         entryBuilder.startDoubleField(
               Component.translatable("config.simpleenemymod.spawn_event.patrol_failure_multiplier"), (Double)EventSpawnConfig.PATROL_FAILURE_MULTIPLIER.get()
            )
            .setDefaultValue(0.15)
            .setMin(0.01)
            .setMax(0.5)
            .setTooltip(new Component[]{Component.translatable("config.simpleenemymod.spawn_event.patrol_failure_multiplier.tooltip")})
            .setSaveConsumer(newValue -> EventSpawnConfig.PATROL_FAILURE_MULTIPLIER.set(newValue))
            .build()
      );
      spawnEvents.addEntry(
         entryBuilder.startDoubleField(
               Component.translatable("config.simpleenemymod.spawn_event.combat_base_chance"), (Double)EventSpawnConfig.COMBAT_BASE_CHANCE.get()
            )
            .setDefaultValue(0.08)
            .setMin(0.01)
            .setMax(1.0)
            .setTooltip(new Component[]{Component.translatable("config.simpleenemymod.spawn_event.combat_base_chance.tooltip")})
            .setSaveConsumer(newValue -> EventSpawnConfig.COMBAT_BASE_CHANCE.set(newValue))
            .build()
      );
      spawnEvents.addEntry(
         entryBuilder.startDoubleField(
               Component.translatable("config.simpleenemymod.spawn_event.combat_failure_multiplier"), (Double)EventSpawnConfig.COMBAT_FAILURE_MULTIPLIER.get()
            )
            .setDefaultValue(0.12)
            .setMin(0.01)
            .setMax(0.5)
            .setTooltip(new Component[]{Component.translatable("config.simpleenemymod.spawn_event.combat_failure_multiplier.tooltip")})
            .setSaveConsumer(newValue -> EventSpawnConfig.COMBAT_FAILURE_MULTIPLIER.set(newValue))
            .build()
      );
      spawnEvents.addEntry(
         entryBuilder.startDoubleField(
               Component.translatable("config.simpleenemymod.spawn_event.cave_base_chance"), (Double)EventSpawnConfig.CAVE_BASE_CHANCE.get()
            )
            .setDefaultValue(0.02)
            .setMin(0.01)
            .setMax(1.0)
            .setTooltip(new Component[]{Component.translatable("config.simpleenemymod.spawn_event.cave_base_chance.tooltip")})
            .setSaveConsumer(newValue -> EventSpawnConfig.CAVE_BASE_CHANCE.set(newValue))
            .build()
      );
      spawnEvents.addEntry(
         entryBuilder.startDoubleField(
               Component.translatable("config.simpleenemymod.spawn_event.cave_failure_multiplier"), (Double)EventSpawnConfig.CAVE_FAILURE_MULTIPLIER.get()
            )
            .setDefaultValue(0.2)
            .setMin(0.01)
            .setMax(0.5)
            .setTooltip(new Component[]{Component.translatable("config.simpleenemymod.spawn_event.cave_failure_multiplier.tooltip")})
            .setSaveConsumer(newValue -> EventSpawnConfig.CAVE_FAILURE_MULTIPLIER.set(newValue))
            .build()
      );
   }
}
