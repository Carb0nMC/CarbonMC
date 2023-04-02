package io.github.carbon.paper.task;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SendPlayerToServerTask extends BukkitRunnable {
    public static ArrayList<String> playersConnecting = new ArrayList<>();
    private Player player;
    private String serverIP;
    private int countdown = 4;

    public SendPlayerToServerTask(Player player, String serverIP) {
        if(playersConnecting.contains(player.getName())){
            player.sendActionBar(Component.text("§cDu wirst bereits mit einem Server verbunden!"));
        } else{
            SendPlayerToServerTask.playersConnecting.add(player.getName());
            this.player = player;
            this.serverIP = serverIP;
            this.runTaskTimer(PaperLoader.getInstance(), 0, 20);
        }
    }

    @Override
    public void run() {
        if(countdown > 0){
            String message = "§aDu wirst in §e" + (countdown - 1) + " §aSekunden mit §e" + serverIP + " §averbunden.";
            player.sendActionBar(Component.text(message));
            countdown--;
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        } else if (countdown == 0){
            player.sendActionBar(Component.text("§3Verbinde..."));
            countdown--;
        } else if(countdown < 0){
            cancel();
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(serverIP);
            player.sendPluginMessage(PaperLoader.getInstance(), "BungeeCord", out.toByteArray());
            player.sendActionBar(Component.text("§aDu wurdest mit §e" + serverIP + " §averbunden."));
            SendPlayerToServerTask.playersConnecting.remove(player.getName());
        }
    }
}
