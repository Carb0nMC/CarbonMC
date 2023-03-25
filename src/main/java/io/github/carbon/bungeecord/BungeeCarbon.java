package io.github.carbon.bungeecord;

import io.github.carbon.bungeecord.command.BungeeMainCommand;
import io.github.carbon.bungeecord.event.Eventlistener;
import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.discord.DiscordBot;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;


public class BungeeCarbon extends Plugin implements CarbonMC {
    private CommandManager commandManager;
    private DiscordBot discordBot;
    private DatabaseUtil databaseUtil;

    @Override
    public void onEnable() {
        new PluginServiceProvider(this);
        this.commandManager = new CommandManager();
        this.databaseUtil = new DatabaseUtil();

        getProxy().getPluginManager().registerCommand(this, new BungeeMainCommand());
        getProxy().getPluginManager().registerListener(this, new Eventlistener());

        /*
        if(fileManager.getBoolSetting(Setting.START_DC_BOT)) {
            try {
                PluginServiceProvider.getCarbonMC().getLogger().warning("Starting Discord Bot...");
                JsonObject object = new JsonParser().parse(new FileReader("/root/mcserver/waterfall/plugins/carbonmc/discord.json")).getAsJsonObject();
                String token = object.get("token").getAsString();
                String prefix = object.get("prefix").getAsString();
                this.discordBot = new DiscordBot(token, prefix);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

         */
    }

    @Override
    public boolean isBungee() {
        return true;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }

    @Override
    public ArrayList<String> getOnlinePlayerNames() {
        return new ArrayList<>(getProxy().getPlayers().stream().map(ProxiedPlayer::getName).toList());
    }

    @Override
    public UUID getPlayerUUID(String playerName) {
        ProxiedPlayer player = getProxy().getPlayer(playerName);

        if(player == null) return null;

        return player.getUniqueId();
    }

    @Override
    public void setPermission(String playerName, String permission, boolean value) {
        ProxiedPlayer player = getProxy().getPlayer(playerName);

        if(player == null) return;

        player.setPermission(permission, value);
    }
}
