package io.github.carbon.paper.event;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import io.github.carbon.carbonmc.utils.LobbyUtil;
import io.github.carbon.carbonmc.utils.ServerType;
import io.github.carbon.carbonmc.utils.messages.Messages;
import io.github.carbon.paper.PaperCarbon;
import io.github.carbon.paper.scoreboard.MainLobbyScoreboard;
import io.github.carbon.paper.task.DailyLoginTask;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Eventlistener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        event.joinMessage(Component.empty());
        CarbonMC.get().getDatabaseUtil().updatePermissions(event.getPlayer().getUniqueId());

        if(CarbonMC.get().getServerType() == ServerType.GAME_LOBBY){
            Location spawnLocation = LobbyUtil.getLobbySpawnLocation();
            spawnLocation.setWorld(Bukkit.getWorld("world"));
            event.getPlayer().teleport(spawnLocation);
            System.out.println("Teleporting " + player.getName());
        }

        if(CarbonMC.get().getServerType() == ServerType.LOBBY){
            Location lobbySpawnLocation = LobbyUtil.getLobbySpawnLocation();
            event.getPlayer().teleport(lobbySpawnLocation);

            String welcomeMessage = CarbonMC.get().getDatabaseUtil().getMessage(Messages.WELCOME_MESSAGE).getValue();
            player.sendMessage(welcomeMessage);
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

            new MainLobbyScoreboard(player);

            new DailyLoginTask(player).runTaskLater(PaperLoader.getInstance(), 20 * 3);
        }

        ((PaperCarbon) PaperCarbon.get()).getTablistManager().setTablist(player);
        ((PaperCarbon) PaperCarbon.get()).getTablistManager().setAllPlayersTeams();

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        if((CarbonMC.get().getServerType() == ServerType.LOBBY || CarbonMC.get().getServerType() == ServerType.GAME_LOBBY) && !player.hasPermission("carbon.lobby.break")){
            String value = CarbonMC.get().getDatabaseUtil().getMessage(Messages.DENY_BLOCK_BREAK).getValue();
            player.sendMessage(CarbonMC.PREFIX + value);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        if((CarbonMC.get().getServerType() == ServerType.LOBBY || CarbonMC.get().getServerType() == ServerType.GAME_LOBBY) && !player.hasPermission("carbon.lobby.place")){
            String value = CarbonMC.get().getDatabaseUtil().getMessage(Messages.DENY_BLOCK_PLACE).getValue();
            player.sendMessage(CarbonMC.PREFIX + value);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if(CarbonMC.get().getServerType() == ServerType.LOBBY){
            if(player.getLocation().getY() < 50){
                player.teleport(LobbyUtil.getLobbySpawnLocation());
            }
        }
    }

}
