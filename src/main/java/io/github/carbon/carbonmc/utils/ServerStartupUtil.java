package io.github.carbon.carbonmc.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;

public class ServerStartupUtil {
    public static String findServerID(){
        JsonObject dataFile = getDataFile();

        if(dataFile == null) throw new IllegalStateException("Server could not be started. Data file not found.");
        if(!dataFile.has("serverID")) throw new IllegalStateException("Server could not be started. Server ID not found.");

        return dataFile.get("serverID").getAsString();
    }

    public static ServerType findServerType(){
        JsonObject dataFile = getDataFile();

        if(dataFile == null) throw new IllegalStateException("Server could not be started. Data file not found.");
        if(!dataFile.has("serverType")) throw new IllegalStateException("Server could not be started. Server type not found.");

        return ServerType.valueOf(dataFile.get("serverType").getAsString());
    }

    public static JsonObject getDataFile(){
        File dataFile = new File("./plugins/data.json");
        if(!dataFile.exists()) return null;
        try {
            JsonObject object = new JsonParser().parse(new FileReader(dataFile)).getAsJsonObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
