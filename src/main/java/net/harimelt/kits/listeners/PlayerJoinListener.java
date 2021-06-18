package net.harimelt.kits.listeners;

import net.harimelt.kits.HarimeltKits;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final HarimeltKits harimeltKits;

    public PlayerJoinListener(HarimeltKits harimeltKits) {
        this.harimeltKits = harimeltKits;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

}