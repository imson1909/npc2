package net.nekoyuni.SimpleEnemyMod.entity.ai.orders;

import java.util.UUID;
import net.minecraft.world.phys.Vec3;

public interface ICommandableMob {
   OrderType getOrder();

   void setOrder(OrderType var1);

   UUID getOwnerUUID();

   Vec3 getMoveToTarget();
}
