package io.github.carbon.bungeecord;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCarbon extends Plugin implements CarbonMC {
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
}
