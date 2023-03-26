package io.github.carbon.carbonmc;

import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.ServerType;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public interface CarbonMC {
    CommandManager getCommandManager();
    DatabaseUtil getDatabaseUtil();

    Logger getLogger();

    default boolean isBungee() {
        return false;
    }

    default String getPrefix(){
        return "§8[§6CarbonMC§8] §r";
    }

    default String getVersion(){
        return "1.0.0";
    }

    default String getAuthor(){
        return "CarbonMC";
    }

    ArrayList<String> getOnlinePlayerNames();

    UUID getPlayerUUID(String playerName);
    String getServerID();
    ServerType getServerType();

    void setPermission(UUID player, String permission, boolean value);
}
