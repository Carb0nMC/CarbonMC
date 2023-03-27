package io.github.carbon.paper.tablist;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.PlayerRank;
import io.github.carbon.carbonmc.utils.messages.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TablistManager {
    public void setTablist(Player player){
        DatabaseUtil databaseUtil = CarbonMC.get().getDatabaseUtil();

        String header = databaseUtil.getMessage(Messages.TABLIST_HEADER).getValue();
        String footer = databaseUtil.getMessage(Messages.TABLIST_FOOTER).getValue();

        player.sendPlayerListHeaderAndFooter(Component.text(header), Component.text(footer));
    }

    public void setAllPlayersTeams(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            setPlayerTeams(player);
        }
    }

    public void setPlayerTeams(Player player){
        Scoreboard scoreboard = player.getScoreboard();

        Team players = scoreboard.getTeam("100players");
        Team developers = scoreboard.getTeam("010developers");
        Team admins = scoreboard.getTeam("001admins");

        if(players == null){
            players = scoreboard.registerNewTeam("100players");
        }

        if(developers == null){
            developers = scoreboard.registerNewTeam("010developers");
        }

        if(admins == null){
            admins = scoreboard.registerNewTeam("001admins");
        }

        DatabaseUtil databaseUtil = CarbonMC.get().getDatabaseUtil();

        String playersPrefix = databaseUtil.getMessage(Messages.TEAM_PLAYERS_PREFIX).getValue();
        String developersPrefix = databaseUtil.getMessage(Messages.TEAM_DEVELOPERS_PREFIX).getValue();
        String adminsPrefix = databaseUtil.getMessage(Messages.TEAM_ADMIN_PREFIX).getValue();

        players.prefix(Component.text(" " +playersPrefix + " §7| §f"));
        players.color(NamedTextColor.GRAY);

        developers.prefix(Component.text(" "+ developersPrefix + " §7| §f"));

        admins.prefix(Component.text(" " + adminsPrefix + " §7| §e"));

        for (Player target : Bukkit.getOnlinePlayers()) {
            PlayerRank rank = databaseUtil.getRank(target.getUniqueId());

            switch (rank){
                case PLAYER -> players.addEntry(target.getName());
                case DEVELOPER -> developers.addEntry(target.getName());
                case ADMIN -> admins.addEntry(target.getName());
            }
        }
    }
}
