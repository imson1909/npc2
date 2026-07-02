package net.nekoyuni.SimpleEnemyMod.entity.client.animation.core;

public enum AnimationPriority {
   CRITICAL(100),
   HIGH(80),
   MEDIUM(50),
   LOW(20),
   NONE(0);

   private final int value;

   AnimationPriority(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }
}
