/*
 * Copyright (C) 2021 SirOswaldo
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.harimelt.kits.commands;

import net.harimelt.kits.HarimeltKits;
import net.harimelt.kits.kit.Kit;
import net.harimelt.kits.kit.KitsManager;
import net.harimelt.kits.util.command.SimpleCommand;
import net.harimelt.kits.util.yaml.Yaml;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ClaimKitCommand extends SimpleCommand {

    private final HarimeltKits plugin;

    public ClaimKitCommand(HarimeltKits plugin) {
        super(plugin, "ClaimKit");
        this.plugin = plugin;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (player.hasPermission("harimelt.claim.kit")) {
            if (arguments.length > 0) {
                String kitName = arguments[0];
                KitsManager kitsManager = plugin.getKitManager();
                if (kitsManager.existKit(kitName)) {
                    Kit kit = kitsManager.getKit(kitName);
                    if (player.hasPermission("harimelt.claim.kit." + kitName)) {
                        Yaml data = new Yaml(plugin, "players", player.getName());
                        data.registerFileConfiguration();
                        if (kit.getClaimTime() == 0) {
                            if (player.hasPermission("harimelt.bypass.claim.one.time") || !data.contains(kitName)) {
                                data.set(kitName, System.currentTimeMillis());
                                data.saveFileConfiguration();
                                for (ItemStack itemStack:kit.getItems()) {
                                    if (player.getInventory().firstEmpty() != -1) {
                                        player.getInventory().addItem(itemStack);
                                    } else {
                                        Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), itemStack);
                                    }
                                }
                                messages.sendMessage(player, "ClaimKit.kitClaimed", new String[][] {{"%kit.name%", kitName}});
                            } else {
                                messages.sendMessage(player, "ClaimKit.oneTimeClaimAlreadyTaken", new String[][] {{"%kit.name%", kitName}});
                            }
                        } else {
                            if (!data.contains(kitName)) {
                                data.set(kitName, System.currentTimeMillis());
                                data.saveFileConfiguration();
                                for (ItemStack itemStack:kit.getItems()) {
                                    if (player.getInventory().firstEmpty() != -1) {
                                        player.getInventory().addItem(itemStack);
                                    } else {
                                        Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), itemStack);
                                    }
                                }
                                messages.sendMessage(player, "ClaimKit.kitClaimed", new String[][] {{"%kit.name%", kitName}});
                            } else {
                                int claimTime = kit.getClaimTime();
                                long lastClaimTime = data.getLong(kitName);
                                long currentTime = System.currentTimeMillis();
                                long pass = (currentTime - lastClaimTime);
                                if (player.hasPermission("harimelt.bypass.claim.time") || (pass / 1000) >= claimTime) {
                                    data.set(kitName, currentTime);
                                    data.saveFileConfiguration();
                                    for (ItemStack itemStack:kit.getItems()) {
                                        if (player.getInventory().firstEmpty() != -1) {
                                            if (itemStack != null) {
                                                player.getInventory().addItem(itemStack);
                                            }
                                        } else {
                                            Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(), itemStack);
                                        }
                                    }
                                    messages.sendMessage(player, "ClaimKit.kitClaimed", new String[][] {{"%kit.name%", kitName}});
                                } else {
                                    messages.sendMessage(player, "ClaimKit.needWaitToClaim", new String[][] {{"%kit.name%", kitName}, {"%seconds%", "" + (claimTime - (pass / 1000))}});
                                }
                            }
                        }
                    } else {
                        messages.sendMessage(player, "ClaimKit.noKitPermission", new String[][] {{"%kit.name%", kitName}});
                    }
                } else {
                    messages.sendMessage(player, "ClaimKit.kitNoExist", new String[][] {{"%kit.name%", kitName}});
                }
            } else {
                messages.sendMessage(player, "ClaimKit.kitNameEmpty");
            }
        } else {
            messages.sendMessage(player, "ClaimKit.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        messages.sendMessage(console, "ClaimTime.isConsole");
        return true;
    }

}