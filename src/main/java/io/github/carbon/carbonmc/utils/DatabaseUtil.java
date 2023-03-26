package io.github.carbon.carbonmc.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.utils.messages.Message;
import io.github.carbon.carbonmc.utils.messages.Messages;
import io.github.carbon.carbonmc.utils.permission.UserPermissionTable;
import io.github.carbon.carbonmc.utils.playerstats.PlayerStats;
import io.github.carbon.carbonmc.utils.setting.Setting;
import io.github.carbon.carbonmc.utils.setting.Settings;

import java.io.FileReader;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

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
            CarbonMC.get().getLogger().info("Connected to database! Checking tables...");

            initTable("CREATE TABLE IF NOT EXISTS settings(id varchar(100) primary key, value bool)");
            initTable("CREATE TABLE IF NOT EXISTS messages(id varchar(100) primary key, value text)");
            initTable("CREATE TABLE IF NOT EXISTS permissions(id varchar(100), permission text, value bool)");
            initTable("CREATE TABLE IF NOT EXISTS playerstats(uuid varchar(100) primary key, coins int, lastlogin bigint, kills int, deaths int, blocksbroken int, blocksplaced int, mobskilled int, playerskilled int, timeskicked int, timesbanned int, timesmuted int)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initTable(String sql){
        try{
            Statement statement = getConnection().createStatement();
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

    public Message getMessage(Messages messages){
        String messageID = messages.getId();

        try{
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM messages WHERE id = ?");
            statement.setString(1, messageID);
            ResultSet results = statement.executeQuery();

            if(results.next()){
                String value = results.getString("value");
                Message message = new Message(messageID, value);
                statement.close();

                return message;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO messages(id, value) VALUES (?, ?)");
            statement.setString(1, messageID);
            statement.setString(2, messages.getDefaultMessage());

            statement.executeUpdate();
            statement.close();

            return new Message(messageID, messages.getDefaultMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Message(messageID, messages.getDefaultMessage());
    }

    public void updateMessage(Messages messages, String value){
        try{
            PreparedStatement statement = getConnection().prepareStatement("UPDATE messages SET value = ? WHERE id = ?");
            statement.setString(1, value);
            statement.setString(2, messages.getId());

            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserPermissionTable getPermissions(UUID uuid){
        try{
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM permissions WHERE id = ?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();

            HashMap<String, Boolean> permissionMap = new HashMap<>();
            while (results.next()){
                String key = results.getString("permission");
                boolean value = results.getBoolean("value");
                permissionMap.put(key, value);
            }

            statement.close();


            UserPermissionTable table = new UserPermissionTable(uuid, permissionMap);
            updatePermissions(uuid, table);
            return table;
        } catch (Exception e) {
            e.printStackTrace();
        }

        CarbonMC.get().getLogger().severe("Returning empty UserPermissionTable");
        //Should never happen
        updatePermissions(uuid);
        return new UserPermissionTable(uuid, new HashMap<>());
    }

    public void setPermission(UUID user, String permission, boolean value){
        try{
            UserPermissionTable permissions = getPermissions(user);
            if(!permissions.getPermissions().containsKey(permission)){
                CarbonMC.get().getLogger().severe("Permission is not set, defaulting to false first");

                PreparedStatement statement = getConnection().prepareStatement("INSERT INTO permissions(id, permission, value) VALUES (?, ?, ?)");
                statement.setString(1, user.toString());
                statement.setString(2, permission);
                statement.setBoolean(3, false);

                statement.executeUpdate();
                statement.close();

                CarbonMC.get().getLogger().severe("Default Set, retrying...");
            }

            PreparedStatement statement = getConnection().prepareStatement("UPDATE permissions SET value = ? WHERE id = ? AND permission = ?");
            statement.setBoolean(1, value);
            statement.setString(2, user.toString());
            statement.setString(3, permission);

            statement.executeUpdate();
            statement.close();

            updatePermissions(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePermissions(UUID uuid){
        updatePermissions(uuid, getPermissions(uuid));
    }

    public void updatePermissions(UUID uuid, UserPermissionTable table){
        HashMap<String, Boolean> permissions = table.getPermissions();

        CarbonMC carbonMC = CarbonMC.get();

        permissions.forEach((permission, value) -> {
            carbonMC.setPermission(uuid, permission, value);
        });
    }

    public PlayerStats getPlayerStats(UUID uuid){
        try{
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM playerstats WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();

            if(results.next()){
                int coins = results.getInt("coins");
                long lastLogin = results.getLong("lastlogin");
                int kills = results.getInt("kills");
                int deaths = results.getInt("deaths");
                int blocksbroken = results.getInt("blocksbroken");
                int blocksplaced = results.getInt("blocksplaced");
                int mobskilled = results.getInt("mobskilled");
                int playerskilled = results.getInt("playerskilled");
                int timeskicked = results.getInt("timeskicked");
                int timesbanned = results.getInt("timesbanned");
                int timesmuted = results.getInt("timesmuted");

                PlayerStats stats = new PlayerStats(uuid, coins, lastLogin, kills, deaths, blocksbroken, blocksplaced, mobskilled, playerskilled, timeskicked, timesbanned, timesmuted);
                statement.close();
                return stats;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO playerstats(uuid, coins, lastlogin, kills, deaths, blocksbroken, blocksplaced, mobskilled, playerskilled, timeskicked, timesbanned, timesmuted) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1, uuid.toString());
            statement.setInt(2, 0);
            statement.setLong(3, System.currentTimeMillis());
            statement.setInt(4, 0);
            statement.setInt(5, 0);
            statement.setInt(6, 0);
            statement.setInt(7, 0);
            statement.setInt(8, 0);
            statement.setInt(9, 0);
            statement.setInt(10, 0);
            statement.setInt(11, 0);
            statement.setInt(12, 0);

            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PlayerStats(uuid, 0, System.currentTimeMillis(), 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public void updatePlayerStats(PlayerStats stats){
        try{
            PreparedStatement statement = getConnection().prepareStatement("UPDATE playerstats SET coins = ?, lastlogin = ?, kills = ?, deaths = ?, blocksbroken = ?, blocksplaced = ?, mobskilled = ?, playerskilled = ?, timeskicked = ?, timesbanned = ?, timesmuted = ? WHERE uuid = ?");
            statement.setInt(1, stats.getCoins());
            statement.setLong(2, stats.getLastLogin());
            statement.setInt(3, stats.getKills());
            statement.setInt(4, stats.getDeaths());
            statement.setInt(5, stats.getBlocksBroken());
            statement.setInt(6, stats.getBlocksPlaced());
            statement.setInt(7, stats.getMobsKilled());
            statement.setInt(8, stats.getPlayersKilled());
            statement.setInt(9, stats.getTimesKicked());
            statement.setInt(10, stats.getTimesBanned());
            statement.setInt(11, stats.getTimesMuted());
            statement.setString(12, stats.getPlayerUUID().toString());

            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
