package io.github.carbon.bungeecord.command;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.plugin.BungeeLoader;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;

public class HubCommand extends Command {
    public HubCommand() {
        super("hub");
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(CarbonMC.PREFIX + "§7Du wirst mit der §bLobby §7 verbunden..."));
            player.connect(BungeeLoader.getInstance().getProxy().getServerInfo("lobby"));
            return;
        }

        sender.sendMessage(CarbonMC.PREFIX + "§cDu musst ein Spieler sein um diesen Befehl zu benutzen!");
    }
}
