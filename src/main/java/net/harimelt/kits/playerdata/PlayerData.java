package net.harimelt.kits.playerdata;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    public UUID getUuid() {
        return uuid;
    }

    private final HashMap<String, Long> claims;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        claims = new HashMap<>();
    }

    public boolean hasClaim(String kitName) {
        return claims.containsKey(kitName);
    }

    public long getClaimTime(String kitName) {
        return claims.get(kitName);
    }

    public void updateClaimTime(String kitName) {
        claims.put(kitName, System.currentTimeMillis());
    }

}