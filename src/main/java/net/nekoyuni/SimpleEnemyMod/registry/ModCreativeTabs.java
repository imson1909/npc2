package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "simpleenemymod");
    public static final RegistryObject<CreativeModeTab> SIMPLE_ENEMY_MOD_TAB = CREATIVE_MODE_TABS.register(
            "simple_enemy_mod_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativeTab.simple_enemy_mod_tab"))
                    .icon(() -> new ItemStack((ItemLike)ModItems.PMC_UNIT_SPAWN_EGG.get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept((ItemLike)ModItems.PMC_UNIT_SPAWN_EGG.get());
                        output.accept((ItemLike)ModItems.PMC_SQUAD_LEADER_SPAWN_EGG.get());
                        output.accept((ItemLike)ModItems.PMC_SQUAD_UNIT_SPAWN_EGG.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}