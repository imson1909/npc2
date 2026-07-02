package net.nekoyuni.SimpleEnemyMod.compat.geckolib;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemStack;
import net.nekoyuni.SimpleEnemyMod.compat.geckolib.internal.GeckoArmorLayerImpl;
import net.nekoyuni.SimpleEnemyMod.compat.geckolib.internal.GeckoHooksImpl;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class GeckoCompatClient {
   public static <T extends AbstractUnit, M extends EntityModel<T>> RenderLayer<T, M> createArmorLayer(
      RenderLayerParent<T, M> parent, HumanoidModel<?> dummyModel
   ) {
      return GeckoArmorLayerImpl.create(parent, dummyModel);
   }

   public static boolean isGeckoArmor(ItemStack stack) {
      return GeckoCompat.LOADED && !stack.isEmpty() ? GeckoHooksImpl.isGeckoArmor(stack) : false;
   }
}
