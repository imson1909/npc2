package net.nekoyuni.SimpleEnemyMod.inventory;

import com.tacz.guns.api.entity.IGunOperator;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;

public class UnitInventoryHandler extends ItemStackHandler {
   private final PmcUnitEntity unit;

   public UnitInventoryHandler(PmcUnitEntity unit) {
      super(18);
      this.unit = unit;
   }

   protected void onContentsChanged(int slot) {
      ItemStack stack = this.getStackInSlot(slot);
      switch (slot) {
         case 0:
            this.unit.setItemSlot(EquipmentSlot.MAINHAND, stack);
            if (!this.unit.level().isClientSide) {
               IGunOperator operator = IGunOperator.fromLivingEntity(this.unit);
               if (!stack.isEmpty()) {
                  operator.draw(() -> stack);
               }
            }
            break;
         case 1:
            this.unit.setItemSlot(EquipmentSlot.OFFHAND, stack);
            break;
         case 2:
            this.unit.setItemSlot(EquipmentSlot.FEET, stack);
            break;
         case 3:
            this.unit.setItemSlot(EquipmentSlot.LEGS, stack);
            break;
         case 4:
            this.unit.setItemSlot(EquipmentSlot.CHEST, stack);
            break;
         case 5:
            this.unit.setItemSlot(EquipmentSlot.HEAD, stack);
      }
   }

   public boolean isItemValid(int slot, ItemStack stack) {
      return super.isItemValid(slot, stack);
   }
}
