package io.github.carbon.carbonmc.utils.file;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.carbon.carbonmc.utils.Setting;

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
            if(!configFile.exists()) {
                configFile.createNewFile();

                FileWriter writer = new FileWriter(configFile);
                writer.write("{}");
                writer.flush();
                writer.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void regenerateConfig(){

    }

    public Object getValue(String key){
        try{
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(new FileReader(configFile)).getAsJsonObject();

            if(!object.has(key) || object.get(key).isJsonNull() || object == null){
                boolean isSetting = Setting.getByPath(key) != null;

                if(isSetting){
                    Object defaultValue = Setting.getByPath(key).getDefaultValue();
                    set(key, defaultValue);
                } else {
                    set(key, "N/A");
                }

                return getValue(key);
            }

            return object.getAsJsonObject().get(key);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void set(String key, Object value){
        try{
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(new FileReader(configFile));
            JsonObject object = element.getAsJsonObject();

            if (value instanceof Boolean) {
                object.addProperty(key, (Boolean) value);
            } else if (value instanceof Number) {
                object.addProperty(key, (Number) value);
            } else if (value instanceof String) {
                object.addProperty(key, (String) value);
            } else {
                object.addProperty(key, value.toString());
            }
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(object, writer);
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void set(Setting setting, boolean value){
        String key = setting.getPath();
        set(key, Boolean.valueOf(value).toString());
    }

    public boolean getBoolSetting(Setting setting){
        String key = setting.getPath();
        return ((JsonElement) getValue(key)).getAsBoolean();
    }

    public String getStringSetting(Setting setting){
        String key = setting.getPath();
        return ((JsonElement) getValue(key)).getAsString();
    }
}
