package io.github.carbon.carbonmc.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.carbon.carbonmc.PluginServiceProvider;

import java.io.FileReader;
import java.sql.*;

public class DatabaseUtil {
    private Connection connection;
    public DatabaseUtil(){
        try {
            JsonObject object = new JsonParser().parse(new FileReader("/root/mcserver/waterfall/plugins/carbonmc/database.json")).getAsJsonObject();

            String host = object.get("host").getAsString();
            String database = object.get("database").getAsString();
            String username = object.get("username").getAsString();
            String password = object.get("password").getAsString();

            String url = "jdbc:mysql://" + host + "/" + database;

            this.connection = DriverManager.getConnection(url, username, password);
            PluginServiceProvider.getCarbonMC().getLogger().info("Connected to database! Checking tables...");

            Statement statement = this.connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS settings(id varchar(100) primary key, value bool)";
            statement.execute(sql);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        if(connection != null){
            return connection;
        }

        try {

            JsonObject object = new JsonParser().parse(new FileReader("/root/mcserver/waterfall/plugins/carbonmc/database.json")).getAsJsonObject();

            String host = object.get("host").getAsString();
            String database = object.get("database").getAsString();
            String username = object.get("username").getAsString();
            String password = object.get("password").getAsString();

            String url = "jdbc:mysql://" + host + "/" + database;

            this.connection = DriverManager.getConnection(url);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Setting getSetting(Settings settings){
        try {
            String settingID = settings.getId();

            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM settings WHERE id = ?");
            statement.setString(1, settingID);
            ResultSet results = statement.executeQuery();

            if(results.next()){
                boolean value = results.getBoolean("value");
                Setting setting = new Setting(settingID, value);
                statement.close();

                return setting;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            String settingID = settings.getId();
            boolean defaultValue = settings.getDefaultValue();

            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO settings(id, value) VALUES (?, ?)");
            statement.setString(1, settingID);
            statement.setBoolean(2, defaultValue);

            statement.executeUpdate();
            statement.close();

            return new Setting(settingID, settings.getDefaultValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Setting(settings.getId(), settings.getDefaultValue());
    }

    public void updateSettings(Settings settings, boolean value){
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE settings SET value = ? WHERE id = ?");
            statement.setBoolean(1, value);
            statement.setString(2, settings.getId());

            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
