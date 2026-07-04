package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.FactionType;
import net.nekoyuni.SimpleEnemyMod.item.RoleSpawnEggItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "simpleenemymod");

    // Original spawn egg (no faction specified, random)
    public static final RegistryObject<Item> PMC_UNIT_SPAWN_EGG = ITEMS.register(
            "pmc_unit_spawn_egg",
            () -> new RoleSpawnEggItem(
                    () -> (EntityType<? extends Mob>)ModEntities.PMCUNIT.get(), 5985087, 2435871, new Item.Properties().stacksTo(64), UnitRole.HOSTILE
            )
    );

    // NEW: Tower faction spawn egg (greenish color)
    public static final RegistryObject<Item> TOWER_UNIT_SPAWN_EGG = ITEMS.register(
            "tower_unit_spawn_egg",
            () -> new RoleSpawnEggItem(
                    () -> (EntityType<? extends Mob>)ModEntities.PMCUNIT.get(), 0x4CAF50, 0x2E7D32, new Item.Properties().stacksTo(64), UnitRole.HOSTILE, FactionType.TOWER
            )
    );

    // NEW: Bunker faction spawn egg (reddish color)
    public static final RegistryObject<Item> BUNKER_UNIT_SPAWN_EGG = ITEMS.register(
            "bunker_unit_spawn_egg",
            () -> new RoleSpawnEggItem(
                    () -> (EntityType<? extends Mob>)ModEntities.PMCUNIT.get(), 0xF44336, 0xC62828, new Item.Properties().stacksTo(64), UnitRole.HOSTILE, FactionType.BUNKER
            )
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}