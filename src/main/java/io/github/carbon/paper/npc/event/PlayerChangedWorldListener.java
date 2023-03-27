package io.github.carbon.paper.npc.event;

import io.github.carbon.paper.npc.Npc;
import io.github.carbon.paper.npc.NpcLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener {

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event){
        for (Npc npc : NpcLoader.getInstance().getNpcManager().getAllNpcs()) {
            npc.spawn(event.getPlayer());
        }
    }

}
