package net.nekoyuni.SimpleEnemyMod.inventory;

import com.mojang.datafixers.util.Pair;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import net.nekoyuni.SimpleEnemyMod.compat.curios.CuriosCompat;
import net.nekoyuni.SimpleEnemyMod.compat.curios.CuriosHelper;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.registry.ModMenuTypes;

public class PmcUnitMenu extends AbstractContainerMenu {
   public final PmcUnitEntity unit;
   private final Level level;
   private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{
      InventoryMenu.BLOCK_ATLAS, InventoryMenu.BLOCK_ATLAS, InventoryMenu.BLOCK_ATLAS, InventoryMenu.BLOCK_ATLAS
   };
   private static final ResourceLocation[] ARMOR_SLOT_ICONS = new ResourceLocation[]{
      new ResourceLocation("minecraft", "item/empty_armor_slot_boots"),
      new ResourceLocation("minecraft", "item/empty_armor_slot_leggings"),
      new ResourceLocation("minecraft", "item/empty_armor_slot_chestplate"),
      new ResourceLocation("minecraft", "item/empty_armor_slot_helmet")
   };

   public PmcUnitMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
      this(pContainerId, inv, inv.player.level().getEntity(extraData.readInt()) instanceof PmcUnitEntity entity ? entity : null);
   }

   public PmcUnitMenu(int pContainerId, Inventory inv, PmcUnitEntity entity) {
      super((MenuType)ModMenuTypes.PMC_UNIT_MENU.get(), pContainerId);
      // FIX: Removed duplicate player inventory slots that were positioned in unit's inventory area
      // The original code had:
      //   for (int i = 0; i < 3; ++i) {
      //       for (int j = 0; j < 9; ++j) {
      //           this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 18 + i * 18));
      //       }
      //   }
      //   for (int k = 0; k < 9; ++k) {
      //       this.addSlot(new Slot(inv, k, 8 + k * 18, 76));
      //   }
      // These lines added PLAYER inventory slots in the UNIT's inventory area (y=18-76),
      // causing items to appear duplicated.

      this.unit = entity;
      this.level = inv.player.level();
      if (this.unit != null) {
         this.unit.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            int[] armorSlotIds = new int[]{5, 4, 3, 2};
            EquipmentSlot[] armorTypes = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

            for (int i = 0; i < 4; i++) {
               final EquipmentSlot slotType = armorTypes[i];
               int slotId = armorSlotIds[i];
               final int iconIndex = 3 - i;
               this.addSlot(new SlotItemHandler(handler, slotId, 8, 19 + i * 18) {
                  public boolean mayPlace(ItemStack stack) {
                     return stack.canEquip(slotType, PmcUnitMenu.this.unit);
                  }

                  public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                     return Pair.of(PmcUnitMenu.ARMOR_SLOT_TEXTURES[iconIndex], PmcUnitMenu.ARMOR_SLOT_ICONS[iconIndex]);
                  }
               });
            }

            this.addSlot(new SlotItemHandler(handler, 0, 90, 37) {
               public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                  return Pair.of(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("minecraft", "item/empty_slot_sword"));
               }
            });
            this.addSlot(new SlotItemHandler(handler, 1, 90, 55) {
               public boolean mayPlace(ItemStack stack) {
                  return stack.getItem() instanceof AbstractGunItem;
               }

               public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                  return Pair.of(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("minecraft", "item/empty_slot_shovel"));
               }
            });
            int startX = 116;
            int startY = 19;

            for (int row = 0; row < 4; row++) {
               for (int col = 0; col < 3; col++) {
                  this.addSlot(new SlotItemHandler(handler, 6 + col + row * 3, startX + col * 18, startY + row * 18));
               }
            }
         });
         if (CuriosCompat.LOADED) {
            CuriosHelper.addCuriosSlots(x$0 -> this.addSlot(x$0), this.unit);
         }

         this.addPlayerInventory(inv, 105);
         this.addPlayerHotbar(inv, 163);
      }
   }

   private void addPlayerInventory(Inventory playerInventory, int yStart) {
      for (int i = 0; i < 3; i++) {
         for (int l = 0; l < 9; l++) {
            this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, yStart + i * 18));
         }
      }
   }

   private void addPlayerHotbar(Inventory playerInventory, int yStart) {
      for (int i = 0; i < 9; i++) {
         this.addSlot(new Slot(playerInventory, i, 8 + i * 18, yStart));
      }
   }

   public ItemStack quickMoveStack(Player playerIn, int index) {
      ItemStack resultStack = ItemStack.EMPTY;
      Slot sourceSlot = (Slot)this.slots.get(index);
      if (sourceSlot != null && sourceSlot.hasItem()) {
         ItemStack sourceStack = sourceSlot.getItem();
         resultStack = sourceStack.copy();
         int playerInvSize = 36;
         int unitSlotsEnd = this.slots.size() - playerInvSize;
         int playerInvStart = unitSlotsEnd;
         int playerInvEnd = this.slots.size();
         if (index < unitSlotsEnd) {
            if (!this.moveItemStackTo(sourceStack, playerInvStart, playerInvEnd, true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(sourceStack, 0, unitSlotsEnd, false)) {
            return ItemStack.EMPTY;
         }

         if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
         } else {
            sourceSlot.setChanged();
         }

         if (sourceStack.getCount() == resultStack.getCount()) {
            return ItemStack.EMPTY;
         }

         sourceSlot.onTake(playerIn, sourceStack);
         return resultStack;
      } else {
         return resultStack;
      }
   }

   public boolean stillValid(Player player) {
      return this.unit != null && this.unit.isAlive() && this.unit.distanceTo(player) < 8.0F;
   }
}
