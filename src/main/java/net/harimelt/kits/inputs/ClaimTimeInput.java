package net.harimelt.kits.inputs;

import net.harimelt.kits.HarimeltKits;
import net.harimelt.kits.inventories.MenuEditorInventory;
import net.harimelt.kits.kit.Kit;
import net.harimelt.kits.kit.KitsManager;
import net.harimelt.kits.util.chatimput.ChatInput;
import net.harimelt.kits.util.task.tasks.OpenInventoryTask;
import net.harimelt.kits.util.yaml.Yaml;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class ClaimTimeInput extends ChatInput {

    private final HarimeltKits harimeltKits;

    public ClaimTimeInput(HarimeltKits harimeltKits, UUID uuid) {
        super(uuid);
        this.harimeltKits = harimeltKits;
    }

    @Override
    public boolean onChatInput(Player player, String input) {
        // Get Player UUID
        UUID uuid = player.getUniqueId();
        if (harimeltKits.getEditing().containsKey(uuid)) {
            // Get Messages Yaml
            Yaml messages = harimeltKits.getMessages();
            if (input.matches("^[0-9]+$")) {
                // Parse Claim Time
                int claimTime = Integer.parseInt(input);
                // Get Kit Name
                String kitName = harimeltKits.getEditing().get(uuid).split(":")[1];
                // Get Kit Manager
                KitsManager kitsManager = harimeltKits.getKitManager();
                // Get Kit
                Kit kit = kitsManager.getKit(kitName);
                // Set claimTime
                kit.setClaimTime(claimTime);
                // Update Editing
                harimeltKits.getEditing().put(player.getUniqueId(), "MENU:" + kitName);
                // Create Menu Editor Inventory
                MenuEditorInventory menuEditorInventory = new MenuEditorInventory(harimeltKits);
                // Get New Inventory
                Inventory inventory= menuEditorInventory.getInventory(new String[] {kitName});
                // Create Open Inventory Task
                OpenInventoryTask openInventoryTask = new OpenInventoryTask(harimeltKits, player, inventory, 5);
                // Start Task
                openInventoryTask.startScheduler();
                return true;
            } else {
                messages.sendMessage(player, "EditKit.claimTimeInvalid", new String[][] {{"%claimTime%", input}});
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onPlayerSneak(Player player) {
        // Get Player UUID
        UUID uuid = player.getUniqueId();
        if (harimeltKits.getEditing().containsKey(uuid)) {
            // Get Kit Name
            String kitName = harimeltKits.getEditing().get(uuid).split(":")[1];
            // Update Editing
            harimeltKits.getEditing().put(player.getUniqueId(), "MENU:" + kitName);
            // Create Menu Editor Inventory
            MenuEditorInventory menuEditor = new MenuEditorInventory(harimeltKits);
            // Get New Inventory
            Inventory inv = menuEditor.getInventory(new String[] {kitName});
            // Open Inventory
            player.openInventory(inv);
        }
    }

}