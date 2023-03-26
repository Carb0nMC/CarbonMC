package io.github.carbon.carbonmc.plugin;

import io.github.carbon.bungeecord.command.BungeeMainCommand;
import io.github.carbon.bungeecord.event.Eventlistener;
import io.github.carbon.carbonmc.utils.ServerRuntime;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeLoader extends Plugin {
    private static BungeeLoader instance;
    @Override
    public void onEnable() {
        instance = this;
        PluginService.register(ServerRuntime.WATERFALL);

        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerCommand(this, new BungeeMainCommand());
        pluginManager.registerListener(this, new Eventlistener());
    }

    public static BungeeLoader getInstance() {
        return instance;
    }
}
