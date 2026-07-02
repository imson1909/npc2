package net.nekoyuni.SimpleEnemyMod.compat.curios;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.nekoyuni.SimpleEnemyMod.registry.ModEntities;
import top.theillusivec4.curios.api.CuriosDataProvider;
import net.minecraft.data.CachedOutput;

public class SimpleCuriosDataProvider extends CuriosDataProvider {
   public SimpleCuriosDataProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<Provider> registries) {
      super(modId, output, fileHelper, registries);
   }

   public void generate(Provider registries, ExistingFileHelper fileHelper) {
      this.createEntities("units_entities").addEntities(new EntityType[]{(EntityType)ModEntities.PMCUNIT.get()}).addSlots(new String[]{"head", "back"});
   }
   @Override public String getName() { return "SEM Curios Data"; }
   @Override public CompletableFuture<?> run(CachedOutput output) { return CompletableFuture.completedFuture(null); }
}
