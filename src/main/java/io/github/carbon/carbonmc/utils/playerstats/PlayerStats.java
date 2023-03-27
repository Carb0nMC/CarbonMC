package io.github.carbon.carbonmc.utils.playerstats;

import io.github.carbon.carbonmc.utils.PlayerRank;

import java.util.UUID;

public class PlayerStats {
    private UUID playerUUID;
    private int coins;
    private long lastLogin;
    private int kills;
    private int deaths;
    private int blocksBroken;
    private int blocksPlaced;
    private int mobsKilled;
    private int playersKilled;
    private int timesKicked;
    private int timesBanned;
    private int timesMuted;
    private PlayerRank rank;

    public PlayerStats(UUID playerUUID, int coins, long lastLogin, int kills, int deaths, int blocksBroken, int blocksPlaced, int mobsKilled, int playersKilled, int timesKicked, int timesBanned, int timesMuted, PlayerRank rank) {
        this.playerUUID = playerUUID;
        this.coins = coins;
        this.lastLogin = lastLogin;
        this.kills = kills;
        this.deaths = deaths;
        this.blocksBroken = blocksBroken;
        this.blocksPlaced = blocksPlaced;
        this.mobsKilled = mobsKilled;
        this.playersKilled = playersKilled;
        this.timesKicked = timesKicked;
        this.timesBanned = timesBanned;
        this.timesMuted = timesMuted;
        this.rank = rank;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getBlocksBroken() {
        return blocksBroken;
    }

    public void setBlocksBroken(int blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public int getBlocksPlaced() {
        return blocksPlaced;
    }

    public PlayerRank getRank() {
        return rank;
    }

    public void setBlocksPlaced(int blocksPlaced) {
        this.blocksPlaced = blocksPlaced;
    }

    public int getMobsKilled() {
        return mobsKilled;
    }

    public void setMobsKilled(int mobsKilled) {
        this.mobsKilled = mobsKilled;
    }

    public int getPlayersKilled() {
        return playersKilled;
    }

    public void setPlayersKilled(int playersKilled) {
        this.playersKilled = playersKilled;
    }

    public int getTimesKicked() {
        return timesKicked;
    }

    public void setTimesKicked(int timesKicked) {
        this.timesKicked = timesKicked;
    }

    public int getTimesBanned() {
        return timesBanned;
    }

    public void setTimesBanned(int timesBanned) {
        this.timesBanned = timesBanned;
    }

    public int getTimesMuted() {
        return timesMuted;
    }

    public void setTimesMuted(int timesMuted) {
        this.timesMuted = timesMuted;
    }

    public void setRank(PlayerRank rank) {
        this.rank = rank;
    }
}
