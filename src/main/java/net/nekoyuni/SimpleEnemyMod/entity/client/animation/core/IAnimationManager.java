package net.nekoyuni.SimpleEnemyMod.entity.client.animation.core;

import net.minecraft.world.entity.Entity;

public interface IAnimationManager {
   void update(Entity var1, int var2);

   void reset();

   boolean isPlaying(String var1);
}
