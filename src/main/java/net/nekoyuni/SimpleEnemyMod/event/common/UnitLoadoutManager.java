package net.nekoyuni.SimpleEnemyMod.event.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.nekoyuni.SimpleEnemyMod.data.UnitLoadout;

@EventBusSubscriber(modid = "simpleenemymod", bus = Bus.FORGE)
public class UnitLoadoutManager extends SimpleJsonResourceReloadListener {
   private static final Gson GSON = new GsonBuilder().create();
   private static final String FOLDER_PATH = "unit_loadouts";
   private static Map<String, List<UnitLoadout>> LOADOUTS_BY_FACTION = Collections.emptyMap();

   public UnitLoadoutManager() {
      super(GSON, "unit_loadouts");
   }

   @SubscribeEvent
   public static void onAddReloadListeners(AddReloadListenerEvent event) {
      event.addListener(new UnitLoadoutManager());
   }

   protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
      Map<String, List<UnitLoadout>> tempLoadoutsByFaction = new HashMap<>();

      for (Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
         ResourceLocation fileId = entry.getKey();

         try {
            String factionPath = fileId.getPath().replace(".json", "");
            String[] pathParts = factionPath.split("/");
            if (pathParts.length >= 1) {
               String factionId = pathParts[0];
               JsonObject root = entry.getValue().getAsJsonObject();
               if (root.has("loadouts") && root.get("loadouts").isJsonObject()) {
                  JsonObject loadoutsMap = root.getAsJsonObject("loadouts");
                  loadoutsMap.entrySet()
                          .forEach(
                                  loadoutEntry -> {
                                     JsonObject loadoutJson = ((JsonElement)loadoutEntry.getValue()).getAsJsonObject();
                                     String loadoutName = (String)loadoutEntry.getKey();

                                     try {
                                        if (!loadoutJson.has("gun_id") || !loadoutJson.has("ammo_count") || !loadoutJson.has("fire_mode")) {
                                           System.err
                                                   .println(
                                                           "Loadout Manager ERROR: Loadout '"
                                                                   + loadoutName
                                                                   + "' in file "
                                                                   + fileId
                                                                   + " is missing a required field (gun_id, ammo_count, or fire_mode)."
                                                   );
                                           return;
                                        }

                                        ResourceLocation gunId = ResourceLocation.tryParse(loadoutJson.get("gun_id").getAsString());
                                        int ammoCount = loadoutJson.get("ammo_count").getAsInt();
                                        String fireMode = loadoutJson.get("fire_mode").getAsString();
                                        ResourceLocation scopeId = loadoutJson.has("scope_id") ? ResourceLocation.tryParse(loadoutJson.get("scope_id").getAsString()) : null;
                                        ResourceLocation muzzleId = loadoutJson.has("muzzle_id")
                                                ? ResourceLocation.tryParse(loadoutJson.get("muzzle_id").getAsString())
                                                : null;
                                        ResourceLocation gripId = loadoutJson.has("grip_id") ? ResourceLocation.tryParse(loadoutJson.get("grip_id").getAsString()) : null;
                                        UnitLoadout loadout = new UnitLoadout(gunId, ammoCount, fireMode, scopeId, muzzleId, gripId);
                                        List<UnitLoadout> factionList = tempLoadoutsByFaction.computeIfAbsent(factionId, k -> new ArrayList<>());
                                        factionList.add(loadout);
                                     } catch (Exception e) {
                                        System.err.println("Loadout Manager FATAL ERROR while parsing '" + loadoutName + "' in file " + fileId + ".");
                                        e.printStackTrace();
                                     }
                                  }
                          );
               } else {
                  System.err.println("Loadout Manager: File " + fileId + " does not contain a 'loadouts' object.");
               }
            }
         } catch (Exception e) {
            System.err.println("Loadout Manager: General error processing file " + fileId + ": " + e.getMessage());
         }
      }

      LOADOUTS_BY_FACTION = tempLoadoutsByFaction;
      System.out.println("Loaded loadouts. Total number of factions: " + LOADOUTS_BY_FACTION.size());
   }

   public static UnitLoadout getRandomLoadout(String factionId, RandomSource random) {
      List<UnitLoadout> loadoutPool = LOADOUTS_BY_FACTION.getOrDefault(factionId, Collections.emptyList());
      if (loadoutPool.isEmpty()) {
         throw new IllegalStateException("The faction '" + factionId + "' it has no loadouts loaded.");
      } else {
         return loadoutPool.get(random.nextInt(loadoutPool.size()));
      }
   }
}