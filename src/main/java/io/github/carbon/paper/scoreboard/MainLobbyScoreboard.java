package io.github.carbon.paper.scoreboard;

import io.github.carbon.carbonmc.CarbonMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MainLobbyScoreboard extends ScoreboardBuilder{

    public MainLobbyScoreboard(Player player){
        super(player, "§eLoading...");
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

    }
}
