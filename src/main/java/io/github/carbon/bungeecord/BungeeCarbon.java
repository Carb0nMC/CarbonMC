package io.github.carbon.bungeecord;

import io.github.carbon.bungeecord.command.BungeeMainCommand;
import io.github.carbon.bungeecord.event.Eventlistener;
import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.utils.file.FileManager;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCarbon extends Plugin implements CarbonMC {
    private CommandManager commandManager;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        new PluginServiceProvider(this);
        this.commandManager = new CommandManager();
        this.fileManager = new FileManager();

        getProxy().getPluginManager().registerCommand(this, new BungeeMainCommand());
        getProxy().getPluginManager().registerListener(this, new Eventlistener());
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
