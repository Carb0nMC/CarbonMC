package io.github.carbon.paper.task;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.utils.playerstats.PlayerStats;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DailyLoginTask extends BukkitRunnable {
    private int coinAmount = 15;
    private Player player;

    public DailyLoginTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        PlayerStats playerStats = CarbonMC.get().getDatabaseUtil().getPlayerStats(player.getUniqueId());

        if(System.currentTimeMillis() >= playerStats.getLastLogin() + (86400000 / 2)){
            playerStats.setCoins(playerStats.getCoins() + coinAmount);
            playerStats.setLastLogin(System.currentTimeMillis());
            CarbonMC.get().getDatabaseUtil().updatePlayerStats(playerStats);
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            player.clearTitle();
            player.sendActionBar(Component.text("§eTäglicher Login: §a+" + coinAmount + " Coins"));
            CarbonMC.get().getLogger().info("§eTäglicher Login: §a+" + coinAmount + " Coins");
        }
    }
}
