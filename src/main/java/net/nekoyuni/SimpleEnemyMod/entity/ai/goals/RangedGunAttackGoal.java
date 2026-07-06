package net.nekoyuni.SimpleEnemyMod.entity.ai.goals;

import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ShootResult;
import com.tacz.guns.api.item.IGun;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils.AiGunSpreadHelper;

import java.util.EnumSet;

public class RangedGunAttackGoal extends Goal {
   private final PathfinderMob mob;
   private final float maxShootDistance;
   private final float baseSpread;
   private final float spreadIncrease;
   private final int minBurst;
   private final int maxBurst;
   private final int minBurstCooldown;
   private final int maxBurstCooldown;

   private int burstCount = 0;
   private int burstSize = 0;
   private int burstCooldown = 0;
   private int aimTimer = 0;
   private int jamTimer = 0;
   private int consecutiveMisses = 0;
   private static final int AIM_TIME = 12;
   private static final float JAM_CHANCE = 0.03F;
   private static final int JAM_MIN_DURATION = 30;
   private static final int JAM_MAX_DURATION = 80;
   private static final float MOVEMENT_SPREAD_PENALTY = 2.5F;
   private static final double DISTANCE_SPREAD_PENALTY_START = 30.0;
   private static final float DISTANCE_SPREAD_MULTIPLIER = 1.5F;

   public RangedGunAttackGoal(PathfinderMob mob, float maxShootDistance, float baseSpread,
                              float spreadIncrease, int minBurst, int maxBurst,
                              int minBurstCooldown, int maxBurstCooldown) {
      this.mob = mob;
      this.maxShootDistance = maxShootDistance;
      this.baseSpread = baseSpread;
      this.spreadIncrease = spreadIncrease;
      this.minBurst = minBurst;
      this.maxBurst = maxBurst;
      this.minBurstCooldown = minBurstCooldown;
      this.maxBurstCooldown = maxBurstCooldown;
      this.setFlags(EnumSet.of(Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      if (this.jamTimer > 0) {
         this.jamTimer--;
         return false;
      }

      LivingEntity target = this.mob.getTarget();
      if (target == null || !target.isAlive()) {
         return false;
      }

      double distance = this.mob.distanceTo(target);
      if (distance > this.maxShootDistance) {
         return false;
      }

      return this.mob.hasLineOfSight(target);
   }

   @Override
   public boolean canContinueToUse() {
      if (this.jamTimer > 0) {
         this.jamTimer--;
         return false;
      }

      LivingEntity target = this.mob.getTarget();
      if (target == null || !target.isAlive()) {
         return false;
      }

      double distance = this.mob.distanceTo(target);
      return distance <= this.maxShootDistance && this.mob.hasLineOfSight(target);
   }

   @Override
   public void start() {
      this.burstCount = 0;
      this.burstSize = this.minBurst + this.mob.getRandom().nextInt(this.maxBurst - this.minBurst + 1);
      this.burstCooldown = 0;
      this.aimTimer = 0;
   }

   @Override
   public void stop() {
      this.aimTimer = 0;
      this.burstCount = 0;
   }

   @Override
   public void tick() {
      LivingEntity target = this.mob.getTarget();
      if (target == null || !target.isAlive()) {
         return;
      }

      this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

      double distance = this.mob.distanceTo(target);
      boolean targetMoving = target.getDeltaMovement().lengthSqr() > 0.001;

      if (this.aimTimer < AIM_TIME) {
         this.aimTimer++;
         if (targetMoving && this.mob.getRandom().nextFloat() < 0.3F) {
            this.aimTimer = Math.max(0, this.aimTimer - 3);
         }
         return;
      }

      if (this.mob.getRandom().nextFloat() < JAM_CHANCE) {
         this.jamTimer = JAM_MIN_DURATION + this.mob.getRandom().nextInt(JAM_MAX_DURATION - JAM_MIN_DURATION);
         this.aimTimer = 0;
         return;
      }

      if (this.burstCooldown > 0) {
         this.burstCooldown--;
         return;
      }

      if (this.burstCount >= this.burstSize) {
         this.burstCount = 0;
         this.burstSize = this.minBurst + this.mob.getRandom().nextInt(this.maxBurst - this.minBurst + 1);
         this.burstCooldown = this.minBurstCooldown + this.mob.getRandom().nextInt(this.maxBurstCooldown - this.minBurstCooldown + 1);
         this.aimTimer = 0;
         return;
      }

      ItemStack gunStack = this.mob.getMainHandItem();
      IGun iGun = IGun.getIGunOrNull(gunStack);
      if (iGun == null) {
         return;
      }

      float spread = this.baseSpread;

      if (distance > DISTANCE_SPREAD_PENALTY_START) {
         float distanceFactor = (float)((distance - DISTANCE_SPREAD_PENALTY_START) / 50.0);
         spread *= (1.0F + distanceFactor * DISTANCE_SPREAD_MULTIPLIER);
      }

      if (targetMoving) {
         spread *= MOVEMENT_SPREAD_PENALTY;
      }

      if (this.consecutiveMisses > 0) {
         spread *= Math.max(0.5F, 1.0F - (this.consecutiveMisses * 0.1F));
      }

      spread += this.mob.getRandom().nextFloat() * this.spreadIncrease;

      if (target.isSprinting()) {
         spread *= 1.8F;
      }

      if (target.isCrouching()) {
         spread *= 0.7F;
      }

      Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.8, 0);
      Vec3 mobPos = this.mob.position().add(0, this.mob.getEyeHeight(), 0);
      Vec3 direction = targetPos.subtract(mobPos).normalize();
      direction = AiGunSpreadHelper.applySpread(direction, spread, this.mob.getRandom());

      if (this.mob instanceof IGunOperator operator) {
         double yaw = Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0;
         double pitch = Math.toDegrees(Math.atan2(direction.y, Math.sqrt(direction.x * direction.x + direction.z * direction.z)));
         ShootResult result = operator.shoot(() -> (float) pitch, () -> (float) yaw);

         if (result == ShootResult.SUCCESS) {
            this.burstCount++;

            if (targetMoving && this.mob.getRandom().nextFloat() < 0.6F) {
               this.consecutiveMisses++;
            } else {
               this.consecutiveMisses = Math.max(0, this.consecutiveMisses - 1);
            }
         }
      }
   }
}