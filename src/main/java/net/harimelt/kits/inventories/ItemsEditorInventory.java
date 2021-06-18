package net.harimelt.kits.inventories;

import net.harimelt.kits.HarimeltKits;
import net.harimelt.kits.kit.Kit;
import net.harimelt.kits.kit.KitsManager;
import net.harimelt.kits.util.inventory.SimpleInventory;
import net.harimelt.kits.util.yaml.Yaml;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemsEditorInventory extends SimpleInventory {

    private final HarimeltKits harimeltKits;

    public ItemsEditorInventory(HarimeltKits harimeltKits) {
        super(harimeltKits.getConfiguration().getString("inventory.itemsEditor.title"), 54);
        this.harimeltKits = harimeltKits;
    }

    @Override
    public void setupItems(Inventory inventory, String[] data) {
        // Get Configuration Yaml
        Yaml configuration = harimeltKits.getConfiguration();
        // Panels
        ItemStack panel = configuration.getItemStack("inventory.itemsEditor.items.panel");
        for (int i = 27; i < 54; i++) {
            inventory.setItem(i, panel);
        }
        // Get Kit Name
        String kitName = data[0];
        // Get Kits Manager
        KitsManager kitsManager = harimeltKits.getKitManager();
        // Get Kit
        Kit kit = kitsManager.getKit(kitName);
        // Add kit items in the inventory
        if (!kit.getItems().isEmpty()) {
            for (int i = 0; i < 27; i++) {
                if (kit.getItems().size() > i) {
                    inventory.setItem(i, kit.getItems().get(i));
                }
            }
        }
        // Cancel Button
        inventory.setItem(38, configuration.getItemStack("inventory.itemsEditor.items.cancel"));
        // Save Button
        inventory.setItem(42, configuration.getItemStack("inventory.itemsEditor.items.save"));
    }

    @Override
    public void onClick(Player player, int slot, InventoryClickEvent event) {
        if (slot > 26 && slot < 54) {
            // Get Player UUID
            UUID uuid = player.getUniqueId();
            // Get Kit Name
            String kitName = harimeltKits.getEditing().get(uuid).split(":")[1];
            switch (slot) {
                case 38: // Cancel Button
                    // Update Editing
                    harimeltKits.getEditing().put(player.getUniqueId(), "MENU:" + kitName);
                    // Close Inventory
                    player.closeInventory();
                    // Create Menu Editor Inventory
                    MenuEditorInventory menuEditorInventory = new MenuEditorInventory(harimeltKits);
                    // Get New Inventory
                    Inventory inventory = menuEditorInventory.getInventory(new String[] {kitName});
                    // Open Inventory
                    player.openInventory(inventory);
                    break;
                case 42: // Save Button
                    // Crete New ItemStack List
                    List<ItemStack> items = new ArrayList<>();
                    for (int i = 0; i < 27; i++) {
                        ItemStack itemStack = event.getInventory().getItem(i);
                        if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                            items.add(itemStack);
                        }
                    }
                    // Get Kit Manager
                    KitsManager kitsManager = harimeltKits.getKitManager();
                    // Get Kit
                    Kit kit = kitsManager.getKit(kitName);
                    // Set Items
                    kit.setItems(items);
                    // Save Kit
                    kitsManager.saveKit(kitName);
                    // Update Editing
                    harimeltKits.getEditing().put(player.getUniqueId(), "MENU:" + kitName);
                    // Close Inventory
                    player.closeInventory();
                    // Create Menu Editor Inventory
                    MenuEditorInventory menuEditor = new MenuEditorInventory(harimeltKits);
                    // Get New Inventory
                    Inventory inv = menuEditor.getInventory(new String[] {kitName});
                    // Open Inventory
                    player.openInventory(inv);
                    break;
            }
        } else {
            event.setCancelled(false);
        }
    }

    @Override
    public boolean onClose(Player player) {
        UUID uuid = player.getUniqueId();
        if (harimeltKits.getEditing().containsKey(uuid)) {
            String status = harimeltKits.getEditing().get(uuid).split(":")[0];
            return status.equals("ITEMS");
        }
        return false;
    }

}