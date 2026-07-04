package net.nekoyuni.SimpleEnemyMod.client.input;

import com.mojang.blaze3d.platform.InputConstants.Type;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBindings {
   public static final String KEY_CATEGORY_SEM = "key.category.simpleenemymod.general";
   public static final String KEY_COMMANDER_MENU = "key.simpleenemymod.commander_menu";
   public static final KeyMapping COMMANDER_MENU_KEY = new KeyMapping(
           "key.simpleenemymod.commander_menu", KeyConflictContext.IN_GAME, Type.KEYSYM, 67, "key.category.simpleenemymod.general"
   );
}