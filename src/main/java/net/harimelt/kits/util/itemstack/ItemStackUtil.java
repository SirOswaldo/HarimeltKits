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

package net.harimelt.kits.util.itemstack;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackUtil {

    public static ItemStack replace(ItemStack item, String[][] replacements) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasDisplayName()) {
                String displayName = meta.getDisplayName();
                for (String[] replacement:replacements) {
                    displayName = displayName.replaceAll(replacement[0], replacement[1]);
                }
                meta.setDisplayName(displayName);
            }
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                List<String> newLore = new ArrayList<>();
                if (lore != null) {
                    for (String line:lore) {
                        for (String[] replacement:replacements) {
                            line = line.replaceAll(replacement[0], replacement[1]);
                        }
                        newLore.add(line);
                    }
                    meta.setLore(newLore);
                }
            }

        }
        item.setItemMeta(meta);
        return item;
    }

}
