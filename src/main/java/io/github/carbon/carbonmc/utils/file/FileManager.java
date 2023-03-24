package io.github.carbon.carbonmc.utils.file;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileManager {
    private File FOLDER, configFile;
    private Gson gson;

    public FileManager(){
        this.gson = new Gson();
        this.FOLDER = new File("/root/mcserver/waterfall/plugins/carbonmc/");
        this.configFile = new File(FOLDER, "config.json");

        try{
            if(!FOLDER.exists()) FOLDER.mkdirs();
            if(!configFile.exists()) configFile.createNewFile();

            FileWriter writer = new FileWriter(configFile);
            writer.write("{}");
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public JsonObject getValue(String key){
        try{
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(new FileReader(configFile)).getAsJsonObject();

            return object.has(key) ? object.getAsJsonObject(key) : null;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void set(String key, String value){
        try{
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(new FileReader(configFile));
            JsonObject object = element.getAsJsonObject();

            object.addProperty(key, value);
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(object, writer);
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
