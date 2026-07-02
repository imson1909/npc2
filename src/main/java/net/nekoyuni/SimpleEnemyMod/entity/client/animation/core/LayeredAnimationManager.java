package net.nekoyuni.SimpleEnemyMod.entity.client.animation.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.layer.IAnimationLayer;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.procedural.IProceduralLayer;

public class LayeredAnimationManager implements IAnimationManager {
   private final List<IAnimationLayer> animationLayers;
   private final List<IProceduralLayer> proceduralLayers;
   private IAnimationLayer currentActiveLayer = null;
   private boolean debugMode = false;

   private LayeredAnimationManager(List<IAnimationLayer> animationLayers, List<IProceduralLayer> proceduralLayers) {
      this.animationLayers = new ArrayList<>(animationLayers);
      this.proceduralLayers = new ArrayList<>(proceduralLayers);
      this.sortLayersByPriority();
   }

   private void sortLayersByPriority() {
      this.animationLayers.sort(Comparator.comparing(IAnimationLayer::getPriority));
   }

   @Override
   public void update(Entity entity, int tickCount) {
      if (entity != null) {
         IAnimationLayer layerToPlay = this.findHighestPriorityLayer(entity, tickCount);
         if (layerToPlay != this.currentActiveLayer) {
            if (this.currentActiveLayer != null) {
               if (this.debugMode) {
                  System.out.println("[Manager | Tick " + tickCount + "] Stopping inactive Layer: " + this.currentActiveLayer.getName());
               }

               this.currentActiveLayer.stop();
            }

            this.currentActiveLayer = layerToPlay;
            if (this.debugMode && layerToPlay != null) {
               System.out.println("[LayeredAnimationManager] Layer Active: " + layerToPlay.getName() + " (Priority: " + layerToPlay.getPriority() + ")");
            }
         }

         if (layerToPlay != null) {
            layerToPlay.play(entity, tickCount);
         }
      }
   }

   public void applyProceduralLayers(ModelPart root, Entity entity, float partialTick) {
      for (IProceduralLayer layer : this.proceduralLayers) {
         if (layer.isEnabled()) {
            layer.apply(root, entity, partialTick);
         }
      }
   }

   private IAnimationLayer findHighestPriorityLayer(Entity entity, int tickCount) {
      for (IAnimationLayer layer : this.animationLayers) {
         boolean canPlay = layer.canPlay(entity, tickCount);
         if (canPlay) {
            return layer;
         }
      }

      return null;
   }

   @Override
   public void reset() {
      for (IAnimationLayer layer : this.animationLayers) {
         layer.stop();
      }

      this.currentActiveLayer = null;
      if (this.debugMode) {
         System.out.println("[LayeredAnimationManager] RESET completo");
      }
   }

   @Override
   public boolean isPlaying(String layerName) {
      for (IAnimationLayer layer : this.animationLayers) {
         if (layer.getName().equals(layerName)) {
            return layer.isPlaying();
         }
      }

      return false;
   }

   public IAnimationLayer getCurrentActiveLayer() {
      return this.currentActiveLayer;
   }

   public List<IAnimationLayer> getAllAnimationLayers() {
      return new ArrayList<>(this.animationLayers);
   }

   public List<IProceduralLayer> getAllProceduralLayers() {
      return new ArrayList<>(this.proceduralLayers);
   }

   public void setDebugMode(boolean enabled) {
      this.debugMode = enabled;
   }

   public static LayeredAnimationManager.Builder builder() {
      return new LayeredAnimationManager.Builder();
   }

   public static class Builder {
      private final List<IAnimationLayer> animationLayers = new ArrayList<>();
      private final List<IProceduralLayer> proceduralLayers = new ArrayList<>();

      public LayeredAnimationManager.Builder addAnimationLayer(IAnimationLayer layer) {
         if (layer == null) {
            throw new IllegalArgumentException("Animation layer cannot be null");
         }

         this.animationLayers.add(layer);
         return this;
      }

      public LayeredAnimationManager.Builder addProceduralLayer(IProceduralLayer layer) {
         if (layer == null) {
            throw new IllegalArgumentException("Procedural layer cannot be null");
         }

         this.proceduralLayers.add(layer);
         return this;
      }

      public LayeredAnimationManager.Builder addAnimationLayers(List<IAnimationLayer> layers) {
         this.animationLayers.addAll(layers);
         return this;
      }

      public LayeredAnimationManager.Builder addProceduralLayers(List<IProceduralLayer> layers) {
         this.proceduralLayers.addAll(layers);
         return this;
      }

      public LayeredAnimationManager build() {
         if (this.animationLayers.isEmpty()) {
            throw new IllegalStateException("Cannot build LayeredAnimationManager without animation layers");
         } else {
            return new LayeredAnimationManager(this.animationLayers, this.proceduralLayers);
         }
      }
   }
}
