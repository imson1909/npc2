package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.item.RoleSpawnEggItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "simpleenemymod");

    public static final RegistryObject<Item> PMC_UNIT_SPAWN_EGG = ITEMS.register(
            "pmc_unit_spawn_egg",
            () -> new RoleSpawnEggItem(
                    () -> (EntityType<? extends Mob>)ModEntities.PMCUNIT.get(), 5985087, 2435871, new Item.Properties().stacksTo(64), UnitRole.HOSTILE
            )
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}