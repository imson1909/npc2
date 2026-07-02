package net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer;

import net.minecraft.world.entity.Entity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.core.AnimationPriority;

public interface IAnimationLayer {
   String getName();

   AnimationPriority getPriority();

   boolean canPlay(Entity var1, int var2);

   void play(Entity var1, int var2);

   void stop();

   boolean isPlaying();
}
