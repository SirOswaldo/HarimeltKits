package net.harimelt.kits.inventories;

import net.harimelt.kits.HarimeltKits;
import net.harimelt.kits.inputs.ClaimTimeInput;
import net.harimelt.kits.kit.Kit;
import net.harimelt.kits.util.inventory.SimpleInventory;
import net.harimelt.kits.util.itemstack.ItemStackUtil;
import net.harimelt.kits.util.yaml.Yaml;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MenuEditorInventory extends SimpleInventory {

    private final HarimeltKits harimeltKits;

    public MenuEditorInventory(HarimeltKits harimeltKits) {
        super(harimeltKits.getConfiguration().getString("inventory.kitEditor.title"), 54);
        this.harimeltKits = harimeltKits;
    }

    @Override
    public void setupItems(Inventory inventory, String[] data) {
        // Get Configuration Yaml
        Yaml configuration = harimeltKits.getConfiguration();
        // Panels
        ItemStack panel = configuration.getItemStack("inventory.kitEditor.items.panel");
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, panel);
        }
        // Get Kit Name
        String kitName = data[0];
        // Buttons
        Kit kit = harimeltKits.getKitManager().getKit(kitName);
        // Items
        ItemStack items = configuration.getItemStack("inventory.kitEditor.items.items");
        ItemStackUtil.replace(items, new String[][]{{"%name%", kit.getName()}, {"%ClaimTime%", kit.getClaimTime() + ""}});
        inventory.setItem(11, items);
        // ClaimTime
        ItemStack claimTime = configuration.getItemStack("inventory.kitEditor.items.claimTime");
        ItemStackUtil.replace(claimTime, new String[][]{{"%name%", kit.getName()}, {"%ClaimTime%", kit.getClaimTime() + ""}});
        inventory.setItem(13, claimTime);
        // Close Menu
        inventory.setItem(40, configuration.getItemStack("inventory.kitEditor.items.close"));
    }

    @Override
    public void onClick(Player player, int slot, InventoryClickEvent event) {
        // Get messages yaml
        Yaml messages = harimeltKits.getMessages();
        // Get Player UUID
        UUID uuid = player.getUniqueId();
        // Get Kit Name
        String kitName = harimeltKits.getEditing().get(uuid).split(":")[1];
        switch (slot) {
            case 11:
                // Update Editing
                harimeltKits.getEditing().put(player.getUniqueId(), "ITEMS:" + kitName);
                // Close Inventory
                player.closeInventory();
                // Create Items Editor Inventory
                ItemsEditorInventory itemsEditorInventory = new ItemsEditorInventory(harimeltKits);
                // Get New Inventory
                Inventory inventory = itemsEditorInventory.getInventory(new String[] {kitName});
                // Open Inventory
                player.openInventory(inventory);
                break;
            case 13:
                // Update Editing
                harimeltKits.getEditing().put(player.getUniqueId(), "CLAIM-TIME:" + kitName);
                // Close Inventory
                player.closeInventory();
                // Create Claim Time Input
                ClaimTimeInput claimTimeInput = new ClaimTimeInput(harimeltKits, player.getUniqueId());
                // Register Claim Time Input
                harimeltKits.getChatInputManager().addChatInput(claimTimeInput);
                // Send Message
                messages.sendMessage(player, "EditKit.inputWaitTime");
                break;
            case 15:
                break;
            case 40:
                // Remove From Editing
                harimeltKits.getEditing().remove(player.getUniqueId());
                // Close Inventory
                player.closeInventory();
                // Send Message
                messages.sendMessage(player, "EditKit.menuClose", new String[][] {{"%kitName%", kitName}});
                break;
        }
    }

    @Override
    public boolean onClose(Player player) {
        // Get Player UUID
        UUID uuid = player.getUniqueId();
        if (harimeltKits.getEditing().containsKey(uuid)) {
            // Get Edit Status
            String status = harimeltKits.getEditing().get(uuid).split(":")[0];
            return status.equals("MENU");
        }
        return false;
    }

}