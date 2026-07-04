package net.nekoyuni.SimpleEnemyMod.inventory;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.registry.ModMenuTypes;

public class PmcUnitMenu extends AbstractContainerMenu {
   public final PmcUnitEntity unit;
   private final PmcUnitEntity entity;
   private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{
           new ResourceLocation("minecraft:item/empty_armor_slot_boots"),
           new ResourceLocation("minecraft:item/empty_armor_slot_leggings"),
           new ResourceLocation("minecraft:item/empty_armor_slot_chestplate"),
           new ResourceLocation("minecraft:item/empty_armor_slot_helmet")
   };

   public PmcUnitMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
      this(id, playerInv, (PmcUnitEntity) playerInv.player.level().getEntity(buf.readInt()));
   }

   public PmcUnitMenu(int id, Inventory playerInv, PmcUnitEntity entity) {
      super(ModMenuTypes.PMC_UNIT_MENU.get(), id);
      this.entity = entity;
      this.unit = entity;
      IItemHandler handler = entity.getCapability(net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
      if (handler != null) {
         this.addSlot(new SlotItemHandler(handler, 0, 8, 18) {
            public boolean mayPlace(ItemStack stack) {
               return true;
            }

            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
               return Pair.of(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("minecraft:item/empty_slot_sword"));
            }
         });
         this.addSlot(new SlotItemHandler(handler, 1, 26, 18) {
            public boolean mayPlace(ItemStack stack) {
               return true;
            }

            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
               return Pair.of(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("minecraft:item/empty_slot_shovel"));
            }
         });

         for(int i = 0; i < 4; ++i) {
            final int armorSlotIndex = i;
            this.addSlot(new SlotItemHandler(handler, 2 + i, 8, 36 + i * 18) {
               public boolean mayPlace(ItemStack stack) {
                  if (stack.isEmpty()) {
                     return true;
                  } else {
                     EquipmentSlot slotType = EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, 3 - armorSlotIndex);
                     return stack.getItem() instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == slotType;
                  }
               }

               public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                  int iconIndex = 3 - armorSlotIndex;
                  return Pair.of(InventoryMenu.BLOCK_ATLAS, ARMOR_SLOT_TEXTURES[iconIndex]);
               }
            });
         }
      }

      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 120 + i * 18));
         }
      }

      for(int k = 0; k < 9; ++k) {
         this.addSlot(new Slot(playerInv, k, 8 + k * 18, 178));
      }
   }

   public PmcUnitEntity getEntity() {
      return this.entity;
   }

   public boolean stillValid(Player player) {
      return this.entity.isAlive();
   }

   public ItemStack quickMoveStack(Player playerIn, int index) {
      ItemStack itemstack = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack itemstack1 = slot.getItem();
         itemstack = itemstack1.copy();
         if (index < 6) {
            if (!this.moveItemStackTo(itemstack1, 6, this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(itemstack1, 0, 6, false)) {
            return ItemStack.EMPTY;
         }

         if (itemstack1.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }

      return itemstack;
   }

   public void removed(Player playerIn) {
      super.removed(playerIn);
      if (!playerIn.level().isClientSide) {
         this.entity.setItemSlot(EquipmentSlot.MAINHAND, this.slots.get(0).getItem());
         this.entity.setItemSlot(EquipmentSlot.OFFHAND, this.slots.get(1).getItem());
         this.entity.setItemSlot(EquipmentSlot.FEET, this.slots.get(2).getItem());
         this.entity.setItemSlot(EquipmentSlot.LEGS, this.slots.get(3).getItem());
         this.entity.setItemSlot(EquipmentSlot.CHEST, this.slots.get(4).getItem());
         this.entity.setItemSlot(EquipmentSlot.HEAD, this.slots.get(5).getItem());
      }
   }
}