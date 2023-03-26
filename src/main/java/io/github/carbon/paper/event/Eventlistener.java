package io.github.carbon.paper.event;

import io.github.carbon.carbonmc.CarbonMC;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Eventlistener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.joinMessage(Component.empty());
        CarbonMC.get().getDatabaseUtil().updatePermissions(event.getPlayer().getUniqueId());
    }
}
