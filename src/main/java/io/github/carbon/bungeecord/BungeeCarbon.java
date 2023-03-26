package io.github.carbon.bungeecord;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.plugin.BungeeLoader;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;


public class BungeeCarbon extends CarbonMC {
    private ProxyServer proxy;

    public BungeeCarbon(){
        super();
        this.proxy = BungeeLoader.getInstance().getProxy();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {

    }

    @Override
    public ArrayList<String> getOnlinePlayerNames() {
        return new ArrayList<String>(proxy.getPlayers().stream().map(ProxiedPlayer::getName).collect(Collectors.toList()));
    }

    @Override
    public UUID getPlayerUUID(String playerName) {
        return proxy.getPlayer(playerName).getUniqueId();
    }

    @Override
    public void setPermission(UUID player, String permission, boolean value) {
        ProxiedPlayer proxyPlayer = proxy.getPlayer(player);
        if(proxyPlayer == null) return;

        proxyPlayer.setPermission(permission, value);
    }
}
