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

import java.util.List;

public class ListKitCommand extends SimpleCommand {

    private final HarimeltKits plugin;

    public ListKitCommand(HarimeltKits plugin) {
        super(plugin, "ListKit");
        this.plugin = plugin;
    }

    @Override
    public boolean onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        if (player.hasPermission("harimelt.list.kit")) {
            KitsManager kitsManager = plugin.getKitManager();
            List<String> kitNames = kitsManager.getKitsNames();
            if (kitNames.isEmpty()) {
                messages.sendMessage(player, "ListKit.isEmpty");
            } else {
                messages.sendMessage(player, "ListKit.header");
                for (String kitName:kitNames) {
                    Kit kit = kitsManager.getKit(kitName);
                    messages.sendMessage(player, "ListKit.format", new String[][] {
                            {"%kit.name%", kitName},
                            {"%kit.claim.time%", kit.getClaimTime() + ""}
                    });
                }
                messages.sendMessage(player, "ListKit.footer");
            }
        } else {
            messages.sendMessage(player, "ListKit.noPermission");
        }
        return true;
    }

    @Override
    public boolean onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = plugin.getMessages();
        KitsManager kitsManager = plugin.getKitManager();
        List<String> kitNames = kitsManager.getKitsNames();
        if (kitNames.isEmpty()) {
            messages.sendMessage(console, "ListKit.isEmpty");
        } else {
            messages.sendMessage(console, "ListKit.header");
            for (String kitName:kitNames) {
                Kit kit = kitsManager.getKit(kitName);
                messages.sendMessage(console, "ListKit.format", new String[][] {
                        {"%kit.name%", kitName},
                        {"%kit.claim.time%", kit.getClaimTime() + ""}
                });
            }
            messages.sendMessage(console, "ListKit.footer");
        }
        return true;
    }

}
