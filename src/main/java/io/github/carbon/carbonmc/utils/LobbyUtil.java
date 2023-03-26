package io.github.carbon.carbonmc.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;

public class LobbyUtil {
    public static Location getLobbySpawnLocation(){
        JsonObject dataFile = getDataFile();
        return new Location(
                Bukkit.getWorld("world"),
                dataFile.get("lobbySpawn").getAsJsonObject().get("x").getAsDouble(),
                dataFile.get("lobbySpawn").getAsJsonObject().get("y").getAsDouble(),
                dataFile.get("lobbySpawn").getAsJsonObject().get("z").getAsDouble(),
                dataFile.get("lobbySpawn").getAsJsonObject().get("yaw").getAsFloat(),
                dataFile.get("lobbySpawn").getAsJsonObject().get("pitch").getAsFloat()
        );

    }
    public static JsonObject getDataFile(){
        try {
            File dataFile = new File("./plugins/data.json");
            return new JsonParser().parse(new FileReader(dataFile)).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Could not load data.json");
    }
}
