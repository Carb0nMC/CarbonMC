package io.github.carbon.paper;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import io.github.carbon.carbonmc.utils.ServerStartupUtil;
import io.github.carbon.carbonmc.utils.ServerType;
import io.github.carbon.paper.menu.MenuManager;
import io.github.carbon.paper.scoreboard.MainLobbyScoreboard;
import io.github.carbon.paper.tablist.TablistManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;


public class PaperCarbon extends CarbonMC{
    private TablistManager tablistManager;
    private MenuManager menuManager;

    public PaperCarbon(){
        super();

        this.serverID = ServerStartupUtil.findServerID();
        this.serverType = ServerStartupUtil.findServerType();
        String message = "CarbonMC is running on " + Bukkit.getName() + " " + Bukkit.getVersion() + " with CarbonMC " + CarbonMC.VERSION + ". Starting Server ID: " + serverID + " with type " + serverType.name();
        getLogger().info(message);
    }


    @Override
    protected void onEnable() {
        this.tablistManager = new TablistManager();
        this.menuManager = new MenuManager();
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(this.getServerType() == ServerType.LOBBY) {
                new MainLobbyScoreboard(player);
            }
            tablistManager.setTablist(player);
            tablistManager.setAllPlayersTeams();
        });

    }

    @Override
    protected void onDisable() {

    }

    @Override
    public ArrayList<String> getOnlinePlayerNames() {
        return new ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
    }

    @Override
    public UUID getPlayerUUID(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if(player == null) return null;
        return player.getUniqueId();
    }

    @Override
    public void setPermission(UUID player, String permission, boolean value) {
        Player playerToSet = Bukkit.getPlayer(player);
        if(playerToSet == null) return;
        PermissionAttachment attachment = playerToSet.addAttachment(PaperLoader.getInstance());
        attachment.setPermission(permission, value);
    }


    public TablistManager getTablistManager() {
        return tablistManager;
    }
}
