package net.nekoyuni.SimpleEnemyMod.entity.unit;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.entity.equipment.PmcUnitWeaponEquipper;
import net.nekoyuni.SimpleEnemyMod.inventory.PmcUnitMenu;
import net.nekoyuni.SimpleEnemyMod.inventory.UnitInventoryHandler;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.FactionType;

public class PmcUnitEntity extends AbstractUnit {
   private static final int VARIANT_COUNT = 6;
   private int variant;
   private final UnitInventoryHandler inventory = new UnitInventoryHandler(this);
   private final LazyOptional<IItemHandler> inventoryOptional = LazyOptional.of(() -> this.inventory);

   public PmcUnitEntity(EntityType<? extends Monster> entityType, Level level) {
      super(entityType, level);
      this.variant = this.random.nextInt(6);
      this.setPersistenceRequired();
   }

   public int getVariant() { return this.variant; }
   public void setVariant(int variant) { this.variant = variant; this.entityData.set(DATA_VARIANT_ID, variant); }

   @Override protected void defineSynchedData() { super.defineSynchedData(); }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("Variant")) this.setVariant(tag.getInt("Variant"));
      if (tag.contains("Inventory")) this.inventory.deserializeNBT(tag.getCompound("Inventory"));
      if (tag.contains("Faction")) {
         try { setFaction(FactionType.valueOf(tag.getString("Faction")));
         } catch (IllegalArgumentException e) { setFaction(FactionType.TOWER); }
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("Variant", this.getVariant());
      tag.put("Inventory", this.inventory.serializeNBT());
      tag.putString("Faction", getFaction().name());
   }

   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
      return capability == ForgeCapabilities.ITEM_HANDLER ? this.inventoryOptional.cast() : super.getCapability(capability, facing);
   }

   @Override
   public void invalidateCaps() { super.invalidateCaps(); this.inventoryOptional.invalidate(); }

   protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
      super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
      for (int i = 0; i < this.inventory.getSlots(); i++) {
         ItemStack stack = this.inventory.getStackInSlot(i);
         if (!stack.isEmpty()) this.spawnAtLocation(stack);
      }
   }

   public void syncEquipmentToInventory() {
      this.inventory.setStackInSlot(0, this.getItemBySlot(EquipmentSlot.MAINHAND).copy());
      this.inventory.setStackInSlot(1, this.getItemBySlot(EquipmentSlot.OFFHAND).copy());
      this.inventory.setStackInSlot(2, this.getItemBySlot(EquipmentSlot.FEET).copy());
      this.inventory.setStackInSlot(3, this.getItemBySlot(EquipmentSlot.LEGS).copy());
      this.inventory.setStackInSlot(4, this.getItemBySlot(EquipmentSlot.CHEST).copy());
      this.inventory.setStackInSlot(5, this.getItemBySlot(EquipmentSlot.HEAD).copy());
   }

   @Override public void equipRandomGun() { PmcUnitWeaponEquipper.equipRandomGun(this, this.getRandom()); }

   @Override
   public void setupRoleGoals() {
      double vRange = 1000.0;
      this.goalSelector.removeAllGoals(pGoal -> true);
      this.targetSelector.removeAllGoals(pGoal -> true);
      if (this.role == null) this.setRole(UnitRole.HOSTILE);

      this.targetSelector.addGoal(0, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.FactionAwareTargetGoal(this, Player.class, true, vRange));
      this.targetSelector.addGoal(1, new net.nekoyuni.SimpleEnemyMod.entity.ai.goals.FactionAwareTargetGoal(this, Monster.class, true, vRange));

      UnitRole.HOSTILE.getGoals().addGoals(this);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (!this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
         if (player.isCreative() && player.isCrouching()) {
            this.syncEquipmentToInventory();
            if (player instanceof ServerPlayer serverPlayer) {
               NetworkHooks.openScreen(serverPlayer,
                       new SimpleMenuProvider((id, playerInv, p) -> new PmcUnitMenu(id, playerInv, this), Component.literal("PMC Unit Equipment")),
                       buf -> buf.writeInt(this.getId()));
            }
            return InteractionResult.SUCCESS;
         }
      }
      return super.mobInteract(player, hand);
   }

   @Override
   public void checkDespawn() {
      // Never despawn
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return false;
   }

   @Override protected SoundEvent getCustomHurtSound() { return SoundEvents.GENERIC_DEATH; }
   @Override protected SoundEvent getCustomDeathSound() { return SoundEvents.GENERIC_HURT; }
   @Override protected SoundEvent getCustomAlertSound() { return SoundEvents.GENERIC_DEATH; }
   @Override public float getSoundVolume() { return 1.5F; }
   @Override public void setRole(UnitRole role) { this.role = role; }
   @Override public UnitRole getRole() { return this.role; }
}