package io.github.carbon.paper;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PaperCarbon extends JavaPlugin implements CarbonMC{
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        new PluginServiceProvider(this);
        this.commandManager = new CommandManager();
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public Logger getLogger() {
        return super.getLogger();
    }
}
