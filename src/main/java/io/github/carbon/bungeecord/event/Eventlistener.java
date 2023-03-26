package io.github.carbon.bungeecord.event;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.messages.Messages;
import io.github.carbon.carbonmc.utils.permission.UserPermissionTable;
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
            String message = databaseUtil.getMessage(Messages.MAINTENANCE_MODE).getValue();
            response.setDescription(message);
            response.setVersion(new ServerPing.Protocol("§c§lWartungsarbeiten", 0));
            response.setPlayers(new ServerPing.Players(0, 0, null));
            event.setResponse(response);
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event){
        ProxiedPlayer player = event.getPlayer();
        String permission = "carbon.bypass.maintenance";
        DatabaseUtil databaseUtil = PluginServiceProvider.getCarbonMC().getDatabaseUtil();
        boolean maintenanceMode = databaseUtil.getSetting(Settings.MAINTENANCE_MODE).getValue();

        UserPermissionTable permissions = databaseUtil.getPermissions(player.getUniqueId());
        permissions.getPermissions().forEach((perm, value) -> {
            PluginServiceProvider.getCarbonMC().getLogger().info("Setting permission " + perm + " to " + value);
            player.setPermission(perm, value);
        });

        if(!player.hasPermission(permission) && maintenanceMode){
            String message = databaseUtil.getMessage(Messages.MAINTENANCE_MODE_KICK).getValue();
            event.getPlayer().disconnect(message);
        }
    }
}
