package io.github.carbon.paper.npc.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager {
    private File FOLDER = new File("./plugins/CarbonMC/npc/");
    private File configFile = new File(FOLDER, "config.yml");
    private FileConfiguration config;

    public FileManager() {
        if (!FOLDER.exists()) {
            FOLDER.mkdirs();
        }

        try{
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save(){
        try{
            config.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
