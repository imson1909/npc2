package net.nekoyuni.SimpleEnemyMod.entity.unit;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.config.common.UnitAttributesConfig;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.IAnimatedEntity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.LayeredAnimationManager;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.SoldierState;
import net.nekoyuni.SimpleEnemyMod.entity.unit.util.StrategyType;
import net.minecraft.world.entity.Mob;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.IRoleHolder;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;

public abstract class AbstractUnit extends Monster implements IRoleHolder, IAnimatedEntity {
   protected UnitRole role = UnitRole.HOSTILE;
   protected static final int HURT_ANIMATION_DURATION = 20;
   public static final EntityDataAccessor<Integer> DAMAGE_ANIMATION_TICKS = SynchedEntityData.defineId(AbstractUnit.class, EntityDataSerializers.INT);
   public static final EntityDataAccessor<Boolean> BACK_DEATH = SynchedEntityData.defineId(AbstractUnit.class, EntityDataSerializers.BOOLEAN);
   protected static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(AbstractUnit.class, EntityDataSerializers.INT);
   public final AnimationState idleAnimationState = new AnimationState();
   public final AnimationState hurtAnimationState = new AnimationState();
   public final AnimationState walkAnimationState = new AnimationState();
   public final AnimationState deathAnimationState = new AnimationState();
   public static final EntityDataAccessor<Boolean> IS_IN_COMBAT = SynchedEntityData.defineId(AbstractUnit.class, EntityDataSerializers.BOOLEAN);
   public static final EntityDataAccessor<Float> COMBAT_YAW = SynchedEntityData.defineId(AbstractUnit.class, EntityDataSerializers.FLOAT);
   public int currentHurtVariant = -1;
   private LayeredAnimationManager animationManager;
   private boolean hasComputedDeathDirection = false;
   private int lastAlertSoundTick = -200;
   private int lastHurtSoundTick = -200;
   private StrategyType strategy;
   private BlockPos coverBlock;
   private long lastMicroMoveTick = 0L;
   private SoldierState soldierState = SoldierState.IDLE;
   @Nullable
   private Vec3 lastKnownTargetPos = null;
   private long lastSeenTargetTick = 0L;
   private long flankingStartTick = 0L;
   private float flankingAngle = 0.0F;
   private boolean isFlankingActive = false;
   private int flankingDirection = 1;
   @Nullable
   private BlockPos coverSearchOrigin = null;
   private boolean movementLockedByManager = false;
   private long movementLockExpiry = 0L;
   private final boolean isDebug = false;

   protected AbstractUnit(EntityType<? extends Monster> entityType, Level level) {
      super(entityType, level);
      if (this.getNavigation() instanceof GroundPathNavigation groundNav) {
         groundNav.setCanOpenDoors(true);
      }
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DATA_VARIANT_ID, 0);
      this.entityData.define(BACK_DEATH, false);
      this.entityData.define(DAMAGE_ANIMATION_TICKS, 0);
      this.entityData.define(IS_IN_COMBAT, false);
      this.entityData.define(COMBAT_YAW, 0.0F);
   }

   public static Builder createAttributes() {
      return Mob.createMobAttributes()
              .add(Attributes.MAX_HEALTH, 20.0)
              .add(Attributes.MOVEMENT_SPEED, 0.27)
              .add(Attributes.ATTACK_DAMAGE, 3.0)
              .add(Attributes.FOLLOW_RANGE, 3.0)
              .add(Attributes.MAX_HEALTH, 96.0);
   }

   public SpawnGroupData finalizeSpawn(
           ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag
   ) {
      if (!this.level().isClientSide) {
         double health = (Double)UnitAttributesConfig.UNIT_HEALTH.get();
         double speed = (Double)UnitAttributesConfig.UNIT_SPEED.get();
         double range = (Double)UnitAttributesConfig.UNIT_DETECTION_RANGE.get();
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
         this.setHealth((float)health);
         this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
         this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(range);
      }

      this.setupRoleGoals();
      this.populateDefaultEquipmentSlots(this.getRandom(), this.level().getCurrentDifficultyAt(this.blockPosition()));
      if (dataTag == null) {
         this.equipRandomGun();
      }

      return super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);
   }

   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
   }

   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      this.populateDefaultEquipmentSlots(this.getRandom(), this.level().getCurrentDifficultyAt(this.blockPosition()));
      if (this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
         this.equipRandomGun();
      }

      this.setupRoleGoals();
   }

   public void tick() {
      super.tick();
      if (!this.level().isClientSide()) {
         int currentTicks = (Integer)this.entityData.get(DAMAGE_ANIMATION_TICKS);
         if (currentTicks > 0) {
            this.entityData.set(DAMAGE_ANIMATION_TICKS, currentTicks - 1);
         }

         LivingEntity target = this.getTarget();
         boolean inCombat = target != null && target.isAlive() || this.tickCount - this.getLastHurtByMobTimestamp() < 100;
         this.entityData.set(IS_IN_COMBAT, inCombat);
         if (target != null && target.isAlive()) {
            double dx = target.getX() - this.getX();
            double dz = target.getZ() - this.getZ();
            float angle = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
            this.entityData.set(COMBAT_YAW, angle);
         }
      }

      if (this.level().isClientSide()) {
         if (!this.idleAnimationState.isStarted()) {
            this.idleAnimationState.start(this.tickCount);
         }

         if (this.getDeltaMovement().horizontalDistanceSqr() > 0.001) {
            if (!this.walkAnimationState.isStarted()) {
               this.walkAnimationState.start(this.tickCount);
            }
         } else {
            this.walkAnimationState.stop();
         }
      }

      if ((Boolean)this.entityData.get(IS_IN_COMBAT) && !this.isDeadOrDying()) {
         float combatYaw = (Float)this.entityData.get(COMBAT_YAW);
         this.yBodyRot = combatYaw;
         this.yHeadRot = combatYaw;
      }
   }

   public boolean hurt(DamageSource source, float amount) {
      Entity attacker = source.getEntity();
      if (attacker == null) {
         attacker = source.getDirectEntity();
      }

      if (attacker != null && attacker.getClass() == this.getClass()) {
         return false;
      }

      boolean damageOccurred = super.hurt(source, amount);
      if (damageOccurred && !this.level().isClientSide()) {
         this.entityData.set(DAMAGE_ANIMATION_TICKS, 20);
      }
      if (damageOccurred && this.level().isClientSide()) {
         this.hurtAnimationState.start(this.tickCount);
      }
      return damageOccurred;
   }

   public void die(DamageSource pDamageSource) {
      if (!this.hasComputedDeathDirection) {
         this.hasComputedDeathDirection = true;
         Entity attacker = pDamageSource.getDirectEntity();
         if (attacker != null && !this.level().isClientSide()) {
            double dX = attacker.getX() - this.getX();
            double dZ = attacker.getZ() - this.getZ();
            float damageYaw = (float)(Mth.atan2(dZ, dX) * (180.0 / Math.PI)) - 90.0F;
            float referenceYaw = this.isInCombat() ? (Float)this.entityData.get(COMBAT_YAW) : this.getYRot();
            float angleDifference = Mth.wrapDegrees(damageYaw - referenceYaw);
            boolean shouldPlayBackDeath = Math.abs(angleDifference) > 90.0F;
            this.entityData.set(BACK_DEATH, shouldPlayBackDeath);
         } else if (!this.level().isClientSide()) {
            this.entityData.set(BACK_DEATH, false);
         }
      }

      super.die(pDamageSource);
   }

   public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
      super.onSyncedDataUpdated(key);
      if (BACK_DEATH.equals(key) && this.level().isClientSide() && this.isDeadOrDying()) {
         this.deathAnimationState.start(this.tickCount);
      }
   }

   public void lockMovementForStrategy(StrategyType strategy) {
      if (strategy == null) {
         strategy = StrategyType.MID_RANGE;
      }

      switch (strategy) {
         case CLOSE_RANGE:
            this.lockMovementForManager(40);
            break;
         case MID_RANGE:
            this.lockMovementForManager(80);
            break;
         case LONG_RANGE:
            this.lockMovementForManager(120);
      }
   }

   public boolean isDeadOrDying() {
      return !this.level().isClientSide() ? super.isDeadOrDying() : super.isDeadOrDying() || this.deathAnimationState.isStarted();
   }

   public void setAnimationManager(LayeredAnimationManager manager) {
      this.animationManager = manager;
   }

   public LayeredAnimationManager getAnimationManager() {
      return this.animationManager;
   }

   @Override
   public void onAnimationVariantSelected(String layerName, int variantIndex) {
      if ("hurt".equals(layerName)) {
         this.currentHurtVariant = variantIndex;
      }
   }

   public void setTarget(@Nullable LivingEntity newTarget) {
      LivingEntity oldTarget = this.getTarget();
      super.setTarget(newTarget);
      if (newTarget != null && !newTarget.equals(oldTarget) && this.tickCount >= this.lastAlertSoundTick + 200) {
         this.playSound(this.getCustomAlertSound(), 1.5F, this.getVoicePitch());
         this.lastAlertSoundTick = this.tickCount;
      }
   }

   protected SoundEvent getHurtSound(DamageSource damageSource) {
      int COOLDOWN_TICKS = 20;
      if (this.tickCount >= this.lastHurtSoundTick + 20) {
         this.lastHurtSoundTick = this.tickCount;
         return this.getCustomHurtSound();
      } else {
         return null;
      }
   }

   protected SoundEvent getDeathSound() {
      return this.getCustomDeathSound();
   }

   public float getSoundVolume() {
      return 1.0F;
   }

   public float getVoicePitch() {
      return 1.0F;
   }

   public boolean isInCombat() {
      return (Boolean)this.entityData.get(IS_IN_COMBAT);
   }

   public float getCombatYaw() {
      return (Float)this.entityData.get(COMBAT_YAW);
   }

   public StrategyType getStrategy() {
      return this.strategy;
   }

   public void setStrategy(StrategyType strategy) {
      if (strategy != null) {
         this.strategy = strategy;
      }
   }

   public BlockPos getCoverBlock() {
      return this.coverBlock;
   }

   public void setCoverBlock(BlockPos pos) {
      this.coverBlock = pos;
   }

   public void setLastMicroMoveTick(long tick) {
      this.lastMicroMoveTick = tick;
   }

   public long getLastMicroMoveTick() {
      return this.lastMicroMoveTick;
   }

   public SoldierState getSoldierState() {
      return this.soldierState;
   }

   public void setSoldierState(SoldierState state) {
      this.soldierState = state;
   }

   public void setCoverSearchOrigin(@Nullable BlockPos pos) {
      this.coverSearchOrigin = pos;
   }

   @Nullable
   public BlockPos getCoverSearchOrigin() {
      return this.coverSearchOrigin;
   }

   @Nullable
   public Vec3 getLastKnownTargetPos() {
      return this.lastKnownTargetPos;
   }

   public void setLastKnownTargetPos(@Nullable Vec3 pos) {
      this.lastKnownTargetPos = pos;
   }

   public long getLastSeenTargetTick() {
      return this.lastSeenTargetTick;
   }

   public void setLastSeenTargetTick(long tick) {
      this.lastSeenTargetTick = tick;
   }

   public long getFlankingStartTick() {
      return this.flankingStartTick;
   }

   public void setFlankingStartTick(long tick) {
      this.flankingStartTick = tick;
   }

   public float getFlankingAngle() {
      return this.flankingAngle;
   }

   public void setFlankingAngle(float angle) {
      this.flankingAngle = angle;
   }

   public boolean isFlankingActive() {
      return this.isFlankingActive;
   }

   public void setFlankingActive(boolean active) {
      this.isFlankingActive = active;
   }

   public int getFlankingDirection() {
      return this.flankingDirection;
   }

   public void setFlankingDirection(int direction) {
      this.flankingDirection = direction;
   }

   public void lockMovementForManager(int ticks) {
      this.movementLockedByManager = true;
      this.movementLockExpiry = this.level().getGameTime() + ticks;
   }

   @Override
   public UnitRole getRole() {
      return this.role;
   }

   public void setRole(UnitRole newRole) {
      this.role = newRole;
   }

   public boolean isMovementLockedByManager() {
      long now = this.level().getGameTime();
      if (now >= this.movementLockExpiry) {
         this.movementLockedByManager = false;
      }

      return this.movementLockedByManager;
   }

   public void releaseMovementLock() {
      this.movementLockedByManager = false;
   }

   public abstract void equipRandomGun();

   public abstract void setupRoleGoals();

   protected abstract SoundEvent getCustomHurtSound();

   protected abstract SoundEvent getCustomDeathSound();

   protected abstract SoundEvent getCustomAlertSound();
}