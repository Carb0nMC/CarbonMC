package io.github.carbon.paper;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;


public class PaperCarbon extends CarbonMC{

    public PaperCarbon(){
        super();
    }

    @Override
    protected void onEnable() {

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
        return Bukkit.getPlayer(playerName).getUniqueId();
    }

    @Override
    public void setPermission(UUID player, String permission, boolean value) {
        Player playerToSet = Bukkit.getPlayer(player);
        if(playerToSet == null) return;
        PermissionAttachment attachment = playerToSet.addAttachment(PaperLoader.getInstance());
        attachment.setPermission(permission, value);

    }
}
