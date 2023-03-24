package io.github.carbon.bungeecord.event;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.utils.Setting;
import io.github.carbon.carbonmc.utils.file.FileManager;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Eventlistener implements Listener {
    @EventHandler
    public void onProxyPing(ProxyPingEvent event){
        FileManager fileManager = PluginServiceProvider.getCarbonMC().getFileManager();
        ServerPing response = event.getResponse();

        //Check for maintenance Mode
        boolean maintenanceMode = fileManager.getBoolSetting(Setting.MAINTENANCE_MODE);
        if(maintenanceMode){
            String message = fileManager.getStringSetting(Setting.MAINTENANCE_MESSAGE);
            response.setDescription(message);
            response.setVersion(new ServerPing.Protocol("§c§lWartungsarbeiten", 0));
            response.setPlayers(new ServerPing.Players(0, 0, null));
            event.setResponse(response);
        }


    }
}
