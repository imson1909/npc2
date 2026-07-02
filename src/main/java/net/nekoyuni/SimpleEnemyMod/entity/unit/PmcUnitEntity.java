package net.nekoyuni.SimpleEnemyMod.entity.unit;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import net.nekoyuni.SimpleEnemyMod.config.common.AiShootingConfig;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.AttackSpecificTargetGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.CommanderOrderGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.LongPatrolGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.MoveToAttackRangeGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.NoPlayerHurtByTargetGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.PeekFromCoverGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.RangedGunAttackGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SeekCoverGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SmartSquadDoorGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.SquadLeaderHandshakeGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.TacticalManeuverGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.VerticalAwareTargetGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.orders.ICommandableMob;
import net.nekoyuni.SimpleEnemyMod.entity.ai.orders.OrderType;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.IRoleHolder;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.entity.equipment.PmcUnitWeaponEquipper;
import net.nekoyuni.SimpleEnemyMod.inventory.PmcUnitMenu;
import net.nekoyuni.SimpleEnemyMod.inventory.UnitInventoryHandler;

public class PmcUnitEntity extends AbstractUnit implements ICommandableMob, IRoleHolder {
   private static final int VARIANT_COUNT = 6;
   private int variant;
   private static final EntityDataAccessor<Integer> ORDER_TYPE_ID = SynchedEntityData.defineId(PmcUnitEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(PmcUnitEntity.class, EntityDataSerializers.OPTIONAL_UUID);
   private static final EntityDataAccessor<String> SYNC_ROLE = SynchedEntityData.defineId(PmcUnitEntity.class, EntityDataSerializers.STRING);
   private static final EntityDataAccessor<Integer> FORMATION_INDEX = SynchedEntityData.defineId(PmcUnitEntity.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> ATTACK_TARGET_ID = SynchedEntityData.defineId(PmcUnitEntity.class, EntityDataSerializers.INT);
   private Vec3 moveOrderPosition = Vec3.ZERO;
   private final UnitInventoryHandler inventory = new UnitInventoryHandler(this);
   private final LazyOptional<IItemHandler> inventoryOptional = LazyOptional.of(() -> this.inventory);

   public PmcUnitEntity(EntityType<? extends Monster> entityType, Level level) {
      super(entityType, level);
      this.variant = this.random.nextInt(6);
   }

   public int getVariant() {
      return this.variant;
   }

   public void setVariant(int variant) {
      this.variant = variant;
      this.entityData.set(DATA_VARIANT_ID, variant);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ORDER_TYPE_ID, OrderType.FREE_FIRE.ordinal());
      this.entityData.define(OWNER_UUID, Optional.empty());
      this.entityData.define(SYNC_ROLE, UnitRole.HOSTILE.name());
      this.entityData.define(FORMATION_INDEX, 0);
      this.entityData.define(ATTACK_TARGET_ID, -1);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("Variant")) {
         this.setVariant(tag.getInt("Variant"));
      }

      if (tag.contains("CurrentOrder")) {
         this.setOrder(OrderType.values()[tag.getInt("CurrentOrder")]);
      }

      if (tag.hasUUID("OwnerUUID")) {
         this.setOwner(tag.getUUID("OwnerUUID"));
      }

      if (tag.contains("MoveX")) {
         this.moveOrderPosition = new Vec3(tag.getDouble("MoveX"), tag.getDouble("MoveY"), tag.getDouble("MoveZ"));
      }

      if (tag.contains("FormationIndex")) {
         this.setFormationIndex(tag.getInt("FormationIndex"));
      }

      if (tag.contains("Inventory")) {
         this.inventory.deserializeNBT(tag.getCompound("Inventory"));
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("Variant", this.getVariant());
      tag.putInt("CurrentOrder", this.getOrder().ordinal());
      if (this.getOwnerUUID() != null) {
         tag.putUUID("OwnerUUID", this.getOwnerUUID());
      }

      if (this.moveOrderPosition != Vec3.ZERO) {
         tag.putDouble("MoveX", this.moveOrderPosition.x);
         tag.putDouble("MoveY", this.moveOrderPosition.y);
         tag.putDouble("MoveZ", this.moveOrderPosition.z);
      }

      tag.putInt("FormationIndex", this.getFormationIndex());
      tag.put("Inventory", this.inventory.serializeNBT());
   }

   public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
      return capability == ForgeCapabilities.ITEM_HANDLER ? this.inventoryOptional.cast() : super.getCapability(capability, facing);
   }

   public void invalidateCaps() {
      super.invalidateCaps();
      this.inventoryOptional.invalidate();
   }

   protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
      super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);

      for (int i = 0; i < this.inventory.getSlots(); i++) {
         ItemStack stack = this.inventory.getStackInSlot(i);
         if (!stack.isEmpty()) {
            this.spawnAtLocation(stack);
         }
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

   @Override
   public void equipRandomGun() {
      PmcUnitWeaponEquipper.equipRandomGun(this, this.getRandom());
   }

   @Override
   public void setupRoleGoals() {
      double vRange = 32.0;
      this.goalSelector.removeAllGoals(pGoal -> true);
      this.targetSelector.removeAllGoals(pGoal -> true);

      System.out.println("[Goal Setup] Entity started with: " + this.getRole());
      this.targetSelector.addGoal(0, new AttackSpecificTargetGoal(this));
      Predicate<LivingEntity> fireFilter = target -> this.isFireAllowed();
      this.targetSelector.addGoal(1, new NoPlayerHurtByTargetGoal(this).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(3, new VerticalAwareTargetGoal(this, Zombie.class, true, vRange, fireFilter));
      this.targetSelector.addGoal(3, new VerticalAwareTargetGoal(this, Skeleton.class, true, vRange, fireFilter));
      this.targetSelector.addGoal(3, new VerticalAwareTargetGoal(this, AbstractIllager.class, true, vRange, fireFilter));
      this.targetSelector.addGoal(2, new VerticalAwareTargetGoal(this, Monster.class, true, vRange, target -> {
         if (target.getClass() == this.getClass()) {
            return false;
         } else {
            return target instanceof AbstractUnit ? true : target instanceof Enemy;
         }
      }));

      UnitRole.HOSTILE.getGoals().addGoals(this);
      System.out.println("[Goal Setup] Creating an Entity with: " + this.getRole());
   }

   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (!this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
         if (player.isCreative() && player.isCrouching()) {
            this.syncEquipmentToInventory();
            if (player instanceof ServerPlayer serverPlayer) {
               NetworkHooks.openScreen(
                       serverPlayer,
                       new SimpleMenuProvider((id, playerInv, p) -> new PmcUnitMenu(id, playerInv, this), Component.literal("PMC Unit Equipment")),
                       buf -> buf.writeInt(this.getId())
               );
            }
            return InteractionResult.SUCCESS;
         } else if (player.isCrouching() && !player.isCreative()) {
            player.displayClientMessage(Component.literal("§c[SEM] Only creative mode players can access unit inventory."), false);
            return InteractionResult.SUCCESS;
         }
      }

      return super.mobInteract(player, hand);
   }

   public void resetCommanderGoalCooldown() {
      this.goalSelector.getAvailableGoals().forEach(wrappedGoal -> {
         if (wrappedGoal.getGoal() instanceof CommanderOrderGoal commanderGoal) {
            commanderGoal.resetCombatCooldown();
         }
      });
   }

   @Override
   protected SoundEvent getCustomHurtSound() {
      return SoundEvents.GENERIC_DEATH;
   }

   @Override
   protected SoundEvent getCustomDeathSound() {
      return SoundEvents.GENERIC_HURT;
   }

   @Override
   protected SoundEvent getCustomAlertSound() {
      return SoundEvents.GENERIC_DEATH;
   }

   @Override
   public float getSoundVolume() {
      return 1.5F;
   }

   public void setRole(UnitRole role) {
      super.setRole(role);
      this.entityData.set(SYNC_ROLE, role.name());
   }

   @Override
   public UnitRole getRole() {
      try {
         return UnitRole.valueOf((String)this.entityData.get(SYNC_ROLE));
      } catch (IllegalArgumentException e) {
         return UnitRole.HOSTILE;
      }
   }

   @Override
   public void setOrder(OrderType order) {
      this.entityData.set(ORDER_TYPE_ID, order.ordinal());
   }

   @Override
   public OrderType getOrder() {
      return OrderType.values()[this.entityData.get(ORDER_TYPE_ID)];
   }

   public void setMoveToTarget(Vec3 pos) {
      this.moveOrderPosition = pos;
      this.setOrder(OrderType.MOVE_TO_POSITION);
   }

   @Override
   public Vec3 getMoveToTarget() {
      return this.moveOrderPosition;
   }

   public void setOwner(UUID uuid) {
      this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
   }

   @Override
   public UUID getOwnerUUID() {
      return (UUID)((Optional)this.entityData.get(OWNER_UUID)).orElse(null);
   }

   public boolean isOwnedBy(Player player) {
      return player.getUUID().equals(this.getOwnerUUID());
   }

   public void setFormationIndex(int index) {
      this.entityData.set(FORMATION_INDEX, index);
   }

   public int getFormationIndex() {
      return (Integer)this.entityData.get(FORMATION_INDEX);
   }

   public void setAttackTargetId(int entityId) {
      this.entityData.set(ATTACK_TARGET_ID, entityId);
   }

   public int getAttackTargetId() {
      return (Integer)this.entityData.get(ATTACK_TARGET_ID);
   }

   public boolean isFireAllowed() {
      return this.getOrder() != OrderType.CEASE_FIRE;
   }
}