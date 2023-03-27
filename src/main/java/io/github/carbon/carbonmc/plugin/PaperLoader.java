package io.github.carbon.carbonmc.plugin;

import io.github.carbon.carbonmc.utils.ServerRuntime;
import io.github.carbon.paper.command.PaperMainCommand;
import io.github.carbon.paper.event.Eventlistener;
import io.github.carbon.paper.jukebox.JukeBox;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperLoader extends JavaPlugin {
    private static PaperLoader instance;
    private JukeBox jukeBox;
    @Override
    public void onEnable() {
        instance = this;
        PluginService.register(ServerRuntime.PAPER);

        getCommand("carbon").setExecutor(new PaperMainCommand());
        getCommand("carbon").setTabCompleter(new PaperMainCommand());

        Bukkit.getPluginManager().registerEvents(new Eventlistener(), this);

        this.jukeBox = new JukeBox();
        jukeBox.onEnable();
    }

    public static void consolePrint(String message){
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

    public JukeBox getJukeBox() {
        return jukeBox;
    }

    public static PaperLoader getInstance() {
        return instance;
    }
}
