package io.github.carbon.bungeecord.event;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.setting.Settings;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Eventlistener implements Listener {
    @EventHandler
    public void onProxyPing(ProxyPingEvent event){
        ServerPing response = event.getResponse();

        DatabaseUtil databaseUtil = PluginServiceProvider.getCarbonMC().getDatabaseUtil();
        boolean maintenanceMode = databaseUtil.getSetting(Settings.MAINTENANCE_MODE).getValue();
        if(maintenanceMode){
            String message = "§c§lWartungsarbeiten";
            response.setDescription(message);
            response.setVersion(new ServerPing.Protocol("§c§lWartungsarbeiten", 0));
            response.setPlayers(new ServerPing.Players(0, 0, null));
            event.setResponse(response);
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event){
        ProxiedPlayer player = event.getPlayer();
        String permission = "carbonmc.bypass.maintenance";
        DatabaseUtil databaseUtil = PluginServiceProvider.getCarbonMC().getDatabaseUtil();
        boolean maintenanceMode = databaseUtil.getSetting(Settings.MAINTENANCE_MODE).getValue();

        if(!player.hasPermission(permission) && maintenanceMode){
            event.getPlayer().disconnect("" +
                    "§c§lWartungsarbeiten\n" +
                    "\n" +
                    "§7Der Server befindet sich derzeit im Wartungsmodus.\n" +
                    "§7Bitte versuche es später noch einmal.\n" +
                    "\n" +
                    "§7§oDieser Server wird von CarbonMC betrieben.\n" +
                    "§e§ohttps://www.carbonmc.net");
        }
    }
}
