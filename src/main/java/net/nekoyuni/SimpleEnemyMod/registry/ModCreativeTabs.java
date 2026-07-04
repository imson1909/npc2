package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "simpleenemymod");

    public static final RegistryObject<CreativeModeTab> SEM_TAB = CREATIVE_MODE_TABS.register(
            "sem_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.PMC_UNIT_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.sem_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.PMC_UNIT_SPAWN_EGG.get());
                        output.accept(ModItems.TOWER_UNIT_SPAWN_EGG.get());
                        output.accept(ModItems.BUNKER_UNIT_SPAWN_EGG.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}