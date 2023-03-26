package io.github.carbon.paper.event;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Eventlistener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.joinMessage(Component.empty());
    }
}
