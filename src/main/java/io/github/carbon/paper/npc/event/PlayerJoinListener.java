package io.github.carbon.paper.npc.event;

import io.github.carbon.paper.npc.Npc;
import io.github.carbon.paper.npc.NpcLoader;
import io.github.carbon.paper.npc.PacketReader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PacketReader packetReader = new PacketReader(event.getPlayer());
        packetReader.inject();


        for (Npc npc : NpcLoader.getInstance().getNpcManager().getAllNpcs()) {
            npc.getIsTeamCreated().put(event.getPlayer().getUniqueId(), false);
            npc.spawn(event.getPlayer());
        }
    }

}
