package io.github.carbon.paper.event;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.utils.ServerType;
import io.github.carbon.carbonmc.utils.messages.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Eventlistener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.joinMessage(Component.empty());
        CarbonMC.get().getDatabaseUtil().updatePermissions(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        if(CarbonMC.get().getServerType() == ServerType.LOBBY && !player.hasPermission("carbon.lobby.break")){
            String value = CarbonMC.get().getDatabaseUtil().getMessage(Messages.DENY_BLOCK_BREAK).getValue();
            player.sendMessage(CarbonMC.PREFIX + value);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        if(CarbonMC.get().getServerType() == ServerType.LOBBY && !player.hasPermission("carbon.lobby.place")){
            String value = CarbonMC.get().getDatabaseUtil().getMessage(Messages.DENY_BLOCK_PLACE).getValue();
            player.sendMessage(CarbonMC.PREFIX + value);
            event.setCancelled(true);
        }
    }
}
