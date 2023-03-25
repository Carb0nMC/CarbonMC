package io.github.carbon.paper;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.paper.command.PaperMainCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PaperCarbon extends JavaPlugin implements CarbonMC{
    private CommandManager commandManager;
    private DatabaseUtil databaseUtil;

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
}
