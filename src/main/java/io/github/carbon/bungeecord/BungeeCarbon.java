package io.github.carbon.bungeecord;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.carbon.bungeecord.command.BungeeMainCommand;
import io.github.carbon.bungeecord.event.Eventlistener;
import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.utils.Setting;
import io.github.carbon.carbonmc.utils.file.FileManager;
import io.github.carbon.discord.DiscordBot;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.FileReader;

public class BungeeCarbon extends Plugin implements CarbonMC {
    private CommandManager commandManager;
    private FileManager fileManager;
    private DiscordBot discordBot;

    @Override
    public void onEnable() {
        new PluginServiceProvider(this);
        this.commandManager = new CommandManager();
        this.fileManager = new FileManager();

        getProxy().getPluginManager().registerCommand(this, new BungeeMainCommand());
        getProxy().getPluginManager().registerListener(this, new Eventlistener());

        if(fileManager.getBoolSetting(Setting.START_DC_BOT)) {
            try {
                PluginServiceProvider.getCarbonMC().getLogger().warning("Starting Discord Bot...");
                JsonObject object = new JsonParser().parse(new FileReader("/Users/lexseifert/IdeaProjects/CarbonMC/discord.json")).getAsJsonObject();
                String token = object.get("token").getAsString();
                String prefix = object.get("prefix").getAsString();
                this.discordBot = new DiscordBot(token, prefix);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    public FileManager getFileManager() {
        return fileManager;
    }
}
