package net.nekoyuni.SimpleEnemyMod.client.gui.screens;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.nekoyuni.SimpleEnemyMod.client.gui.overlay.CommanderOverlayRenderer;
import net.nekoyuni.SimpleEnemyMod.client.system.ClientGlowManager;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.utils.SquadData;
import net.nekoyuni.SimpleEnemyMod.entity.ai.orders.OrderType;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.network.ModNetworking;
import net.nekoyuni.SimpleEnemyMod.network.packets.PacketIssueOrder;

public class CommanderMenuScreen extends Screen {
   private static final int COL_WIDTH = 100;
   private static final int ITEM_HEIGHT = 15;
   private static final int PADDING = 10;
   private static final int BG_COLOR = -1879048192;
   private static final int TEXT_COLOR = 16777215;
   private static final int HOVER_COLOR = 16766720;
   private static final int SELECTED_COLOR = 65280;
   private static final int HEADER_COLOR = 16753920;
   private final List<PmcUnitEntity> nearbyLeaders = new ArrayList<>();
   private final List<PmcUnitEntity> nearbyUnits = new ArrayList<>();
   private final Set<Integer> selectedEntityIds = new HashSet<>();
   private static final String[] MAIN_OPTIONS = new String[]{
           "Select Unit", "Select All", "Hold Position", "Follow Me", "Move To...", "Attack that...", "Cease Fire", "Free Fire", "F: Wedge", "F: Column"
   };
   private boolean showSubmenus = false;
   private static long lastOrderTime = 0L;

   public CommanderMenuScreen() {
      super(Component.literal("Commander"));
   }

   protected void init() {
      super.init();
      this.scanForUnits();
   }

   public void repositionElements() {
      ClientGlowManager.clear();
      super.repositionElements();
   }

   private void scanForUnits() {
      this.nearbyLeaders.clear();
      this.nearbyUnits.clear();
      if (this.minecraft != null && this.minecraft.player != null) {
         LocalPlayer player = this.minecraft.player;
         Level level = player.level();

         for (PmcUnitEntity unit : level.getEntitiesOfClass(PmcUnitEntity.class, player.getBoundingBox().inflate(64.0))) {
            if (unit.isOwnedBy(player)) {
               if (this.isSquadLeader(unit)) {
                  this.nearbyLeaders.add(unit);
               } else if (!SquadData.hasValidSquadData(unit)) {
                  this.nearbyUnits.add(unit);
               }
            }
         }

         this.nearbyLeaders.sort(Comparator.comparingInt(Entity::getId));
         this.nearbyUnits.sort(Comparator.comparingInt(Entity::getId));
      }
   }

   private boolean isSquadLeader(PmcUnitEntity unit) {
      return unit.getRole() == UnitRole.FRIENDLY_SQUAD_LEADER;
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      int x1 = 10;
      int yStart = 30;
      int h1 = MAIN_OPTIONS.length * 15 + 20;
      guiGraphics.fill(x1, yStart, x1 + 100, yStart + h1, -1879048192);
      guiGraphics.drawCenteredString(this.font, "COMMANDER", x1 + 50, yStart + 2, 16753920);

      for (int i = 0; i < MAIN_OPTIONS.length; i++) {
         int y = yStart + 15 + i * 15;
         boolean isHover = this.isMouseOver(mouseX, mouseY, x1, y, 100, 15);
         int color = isHover ? 16766720 : 16777215;
         if (i == 0 && this.showSubmenus) {
            color = 16766720;
         }

         String textToDisplay = MAIN_OPTIONS[i];
         if (i == 1) {
            textToDisplay = this.areAllSelected() ? "Deselect All" : "Select All";
            if (this.areAllSelected()) {
               color = 11184810;
            }
         }

         guiGraphics.drawString(this.font, textToDisplay, x1 + 5, y + 4, color, false);
      }

      if (this.showSubmenus) {
         int x2 = x1 + 100 + 5;
         int x3 = x2 + 100 + 5;
         if (!this.nearbyLeaders.isEmpty()) {
            this.renderUnitColumn(guiGraphics, mouseX, mouseY, x2, yStart, "SQUADS", this.nearbyLeaders, true);
         }

         int unitsX = this.nearbyLeaders.isEmpty() ? x2 : x3;
         if (!this.nearbyUnits.isEmpty()) {
            this.renderUnitColumn(guiGraphics, mouseX, mouseY, unitsX, yStart, "UNITS", this.nearbyUnits, false);
         }
      }
   }

   private void renderUnitColumn(GuiGraphics g, int mx, int my, int x, int yStart, String title, List<PmcUnitEntity> units, boolean isSquad) {
      int contentHeight = units.size() * 15 + 20;
      g.fill(x, yStart, x + 100, yStart + contentHeight, -1879048192);
      g.drawCenteredString(this.font, title, x + 50, yStart + 2, 16753920);

      for (int i = 0; i < units.size(); i++) {
         PmcUnitEntity unit = units.get(i);
         int y = yStart + 15 + i * 15;
         boolean isHover = this.isMouseOver(mx, my, x, y, 100, 15);
         boolean isSelected = this.selectedEntityIds.contains(unit.getId());
         int color = isSelected ? 65280 : (isHover ? 16766720 : 16777215);
         String text = isSquad ? "SQUAD [" + (i + 1) + "]" : "[" + (i + 1) + "] Unit";
         if (isSelected) {
            text = "> " + text;
         }

         g.drawString(this.font, text, x + 5, y + 4, color, false);
      }
   }

   private boolean isMouseOver(double mx, double my, int x, int y, int w, int h) {
      return mx >= x && mx < x + w && my >= y && my < y + h;
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button != 0) {
         return super.mouseClicked(mouseX, mouseY, button);
      }

      int x1 = 10;
      int yStart = 30;

      for (int i = 0; i < MAIN_OPTIONS.length; i++) {
         int y = yStart + 15 + i * 15;
         if (this.isMouseOver(mouseX, mouseY, x1, y, 100, 15)) {
            this.handleMainAction(i);
            return true;
         }
      }

      if (this.showSubmenus) {
         int x2 = x1 + 100 + 5;
         int x3 = x2 + 100 + 5;
         int unitsX = this.nearbyLeaders.isEmpty() ? x2 : x3;
         if (!this.nearbyLeaders.isEmpty()) {
            this.checkUnitListClick(mouseX, mouseY, x2, yStart, this.nearbyLeaders);
         }

         if (!this.nearbyUnits.isEmpty()) {
            this.checkUnitListClick(mouseX, mouseY, unitsX, yStart, this.nearbyUnits);
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   private void checkUnitListClick(double mx, double my, int x, int yStart, List<PmcUnitEntity> units) {
      for (int i = 0; i < units.size(); i++) {
         int y = yStart + 15 + i * 15;
         if (this.isMouseOver(mx, my, x, y, 100, 15)) {
            int id = units.get(i).getId();
            if (this.selectedEntityIds.contains(id)) {
               this.selectedEntityIds.remove(id);
               ClientGlowManager.removeEntity(id);
            } else {
               this.selectedEntityIds.add(id);
               ClientGlowManager.addEntity(id);
            }

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }
      }
   }

   private void handleMainAction(int index) {
      switch (index) {
         case 0:
            this.showSubmenus = !this.showSubmenus;
            break;
         case 1:
            this.toggleSelectAll();
            break;
         case 2:
            this.issueOrderToSelected(OrderType.HOLD_POSITION, -1);
            break;
         case 3:
            this.issueOrderToSelected(OrderType.FOLLOW_COMMANDER, -1);
            break;
         case 4:
            if (this.selectedEntityIds.isEmpty()) {
               this.sendMessage("§cSelect at least one unit first!");
            } else {
               CommanderOverlayRenderer.isSelectingPosition = true;
               CommanderOverlayRenderer.selectedUnitsSnapshot = new HashSet<>(this.selectedEntityIds);
               this.onClose();
               this.sendMessage("§aSelect position...");
            }
            break;
         case 5:
            if (this.selectedEntityIds.isEmpty()) {
               this.sendMessage("§cSelect at least one unit first!");
            } else {
               CommanderOverlayRenderer.isSelectingTarget = true;
               CommanderOverlayRenderer.selectedUnitsSnapshot = new HashSet<>(this.selectedEntityIds);
               this.onClose();
               this.sendMessage("§aSelect a target...");
            }
            break;
         case 6:
            this.issueOrderToSelected(OrderType.CEASE_FIRE, -1);
            break;
         case 7:
            this.issueOrderToSelected(OrderType.FREE_FIRE, -1);
            break;
         case 8:
            this.issueOrderToSelected(OrderType.FORM_WEDGE, -1);
            break;
         case 9:
            this.issueOrderToSelected(OrderType.FORM_COLUMN, -1);
      }
   }

   public static boolean shouldSuppressFire() {
      return System.currentTimeMillis() - lastOrderTime < 200L;
   }

   private void issueOrderToSelected(OrderType order, int targetId) {
      if (this.selectedEntityIds.isEmpty()) {
         this.sendMessage("§cNo units selected!");
      } else {
         List<Integer> sortedIds = new ArrayList<>(this.selectedEntityIds);
         sortedIds.sort((id1, id2) -> Integer.compare(id2, id1));
         lastOrderTime = System.currentTimeMillis();
         int count = 0;

         for (int i = 0; i < sortedIds.size(); i++) {
            int unitId = sortedIds.get(i);
            int newIndex = i;
            ModNetworking.sendToServer(new PacketIssueOrder(unitId, order, Vec3.ZERO, newIndex, targetId));
            count++;
         }

         this.sendMessage("§eOrder sent to " + count + " units.");
         this.onClose();
      }
   }

   private void sendMessage(String msg) {
      if (this.minecraft.player != null) {
         this.minecraft.player.displayClientMessage(Component.literal(msg), true);
      }
   }

   private void toggleSelectAll() {
      int totalUnits = this.nearbyLeaders.size() + this.nearbyUnits.size();
      if (this.selectedEntityIds.size() >= totalUnits && totalUnits > 0) {
         this.selectedEntityIds.clear();
         ClientGlowManager.clear();
      } else {
         for (PmcUnitEntity leader : this.nearbyLeaders) {
            int id = leader.getId();
            this.selectedEntityIds.add(id);
            ClientGlowManager.addEntity(id);
         }

         for (PmcUnitEntity unit : this.nearbyUnits) {
            int id = unit.getId();
            this.selectedEntityIds.add(id);
            ClientGlowManager.addEntity(id);
         }
      }

      Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   private boolean areAllSelected() {
      int totalUnits = this.nearbyLeaders.size() + this.nearbyUnits.size();
      return totalUnits > 0 && this.selectedEntityIds.size() >= totalUnits;
   }
}