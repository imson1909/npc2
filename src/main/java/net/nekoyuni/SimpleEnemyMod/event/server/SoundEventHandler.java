package net.nekoyuni.SimpleEnemyMod.event.server;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.registry.ModSounds;

public class SoundEventHandler {
   public static void onGunShoot(LivingEntity shooter) {
      if (shooter instanceof PmcUnitEntity pmc) {
         Level level = shooter.level();
         level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), ModSounds.SOUND_PMC_UNIT_ALERT.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
      }
   }
}