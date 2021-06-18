package net.harimelt.kits.playerdata;

import net.harimelt.kits.HarimeltKits;

import java.util.HashMap;

public class PlayersDataManager {

    private final HarimeltKits harimeltKits;

    public PlayersDataManager(HarimeltKits harimeltKits) {
        this.harimeltKits = harimeltKits;
    }

    private final HashMap<String, PlayerData> players = new HashMap<>();

    public void loadClaims(String name) {

    }

    public void saveClaims(String name) {

    }

    public PlayerData getPlayerData(String name) {
        return players.get(name);
    }

}