package io.github.carbon.paper.scoreboard;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.PlayerRank;
import io.github.carbon.carbonmc.utils.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MainLobbyScoreboard extends ScoreboardBuilder{

    public MainLobbyScoreboard(Player player){
        super(player, "§eLoading...");
        new BukkitRunnable(){
            @Override
            public void run() {
                update();
            }
        }.runTaskTimer(PaperLoader.getInstance(), 20, 20);
    }

    @Override
    public void createScoreboard() {
        setDisplayName("§e§l§cC§6a§er§ab§bo§9n§5M§dC");
        setScore("§7§l|-----------------", 10);
        setScore("§7§l| §7§l» §eOnline§7§l: §3"  + Bukkit.getOnlinePlayers().size(), 9);
        setScore("§7§l|", 8);
        setScore("§7§l| §7§l» §eDein Rang§7§l:", 7);
        setScore("§7§l| Rank", 6);
        setScore("§7§l|", 5);
        setScore("§7§l| §7§l» §eCoins§7§l:" + "§3 " + "0", 4);
        setScore("§7§l|", 3);
        setScore("§7§l| §8play.carbonmc.net", 2);
        setScore("§7§l| §8" + CarbonMC.get().getServerID(), 1);
        setScore("§7§l|-----------------", 0);
    }

    @Override
    public void update() {
        DatabaseUtil databaseUtil = CarbonMC.get().getDatabaseUtil();
        PlayerRank rank = databaseUtil.getRank(player.getUniqueId());
        switch (rank){
            case ADMIN -> setScore("§7§l| " + Messages.TEAM_ADMIN_PREFIX, 6);
            case DEVELOPER -> setScore("§7§l| " + Messages.TEAM_DEVELOPERS_PREFIX, 6);
            case PLAYER -> setScore("§7§l| " + Messages.TEAM_PLAYERS_PREFIX, 6);
        }

        setScore("§7§l| " + , 6);
        setScore("§7§l| §7§l» §eCoins§7§l:" + "§3 " + databaseUtil.getCoins(player.getUniqueId()), 4);
    }
}
