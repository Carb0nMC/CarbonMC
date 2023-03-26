package io.github.carbon.paper;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.ServerType;
import io.github.carbon.paper.command.PaperMainCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public class PaperCarbon extends JavaPlugin implements CarbonMC{
    private CommandManager commandManager;
    private DatabaseUtil databaseUtil;
    private String serverID;
    private ServerType serverType;

    @Override
    public void onEnable() {
        new PluginServiceProvider(this);
        this.commandManager = new CommandManager();
        this.databaseUtil = new DatabaseUtil();

        getCommand("carbon").setExecutor(new PaperMainCommand());
        getCommand("carbon").setTabCompleter(new PaperMainCommand());


    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public Logger getLogger() {
        return super.getLogger();
    }
    @Override
    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }

    @Override
    public ArrayList<String> getOnlinePlayerNames() {
        return new ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
    }

    @Override
    public UUID getPlayerUUID(String playerName) {
        Player player = Bukkit.getPlayer(playerName);

        if(player == null) return null;

        return player.getUniqueId();
    }

    @Override
    public void setPermission(UUID player, String permission, boolean value) {
        Player p = Bukkit.getPlayer(player);
        if(p == null) return;

        PermissionAttachment attachment = p.addAttachment(this);
        attachment.setPermission(permission, value);
    }

    @Override
    public String getServerID() {
        return serverID;
    }

    @Override
    public ServerType getServerType() {
        return serverType;
    }
}
