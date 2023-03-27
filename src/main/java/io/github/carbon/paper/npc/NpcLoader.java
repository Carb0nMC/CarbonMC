package io.github.carbon.paper.npc;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import io.github.carbon.paper.npc.commands.NpcCMD;
import io.github.carbon.paper.npc.event.PacketReceivedListener;
import io.github.carbon.paper.npc.event.PlayerChangedWorldListener;
import io.github.carbon.paper.npc.event.PlayerJoinListener;
import io.github.carbon.paper.npc.event.PlayerMoveListener;
import io.github.carbon.paper.npc.utils.FileManager;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Logger;

public class NpcLoader {

    public static final String SUPPORTED_VERSION = "1.19.4";

    private static NpcLoader instance;
    private final NpcManager npcManager;
    private FileManager fileManager;
    private boolean muteVersionNotification;

    public NpcLoader() {
        instance = this;
        this.fileManager = new FileManager();
        this.npcManager = new NpcManager();
    }

    public void onEnable() {
        if(!getConfig().isBoolean("mute_version_notification")){
            getConfig().set("mute_version_notification", false);
            saveConfig();
        }

        muteVersionNotification = getConfig().getBoolean("mute_version_notification");

        PluginManager pluginManager = Bukkit.getPluginManager();


        DedicatedServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();

        String serverVersion = nmsServer.getServerVersion();
        if(!serverVersion.equals(SUPPORTED_VERSION)){
            getLogger().warning("--------------------------------------------------");
            getLogger().warning("Unsupported minecraft server version.");
            getLogger().warning("Please update the server to " + SUPPORTED_VERSION + ".");
            getLogger().warning("Disabling NPC plugin.");
            getLogger().warning("--------------------------------------------------");
            pluginManager.disablePlugin(PaperLoader.getInstance());
            return;
        }

        String serverSoftware = nmsServer.getServerModName();
        if(!serverSoftware.equals("Paper")){
            getLogger().warning("--------------------------------------------------");
            getLogger().warning("It is recommended to use Paper as server software.");
            getLogger().warning("Because you are not using paper, the plugin");
            getLogger().warning("might not work correctly.");
            getLogger().warning("--------------------------------------------------");
        }

        PaperLoader.getInstance().getCommand("npc").setExecutor(new NpcCMD());

        pluginManager.registerEvents(new PlayerJoinListener(), PaperLoader.getInstance());
        pluginManager.registerEvents(new PlayerMoveListener(), PaperLoader.getInstance());
        pluginManager.registerEvents(new PlayerChangedWorldListener(), PaperLoader.getInstance());
        pluginManager.registerEvents(new PacketReceivedListener(), PaperLoader.getInstance());

        PaperLoader.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(PaperLoader.getInstance(), "BungeeCord");

        // load and spawn npcs
        Bukkit.getScheduler().runTaskLater(PaperLoader.getInstance(), () -> {
            npcManager.loadNpcs();

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                PacketReader packetReader = new PacketReader(onlinePlayer);
                packetReader.inject();

                npcManager.getAllNpcs().forEach(npc -> npc.spawn(onlinePlayer));
            }
        }, 20L*5);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(PaperLoader.getInstance(), () -> npcManager.saveNpcs(false), 20L*60*5, 20L*60*15);
    }

    public void onDisable() {
        PaperLoader.getInstance().getServer().getMessenger().unregisterOutgoingPluginChannel(PaperLoader.getInstance());

        npcManager.saveNpcs(true);
    }

    public NpcManager getNpcManager() {
        return npcManager;
    }

    public boolean isMuteVersionNotification() {
        return muteVersionNotification;
    }

    public static NpcLoader getInstance() {
        return instance;
    }

    public Logger getLogger(){
        return CarbonMC.get().getLogger();
    }

    public FileConfiguration getConfig(){
        return fileManager.getConfig();
    }

    public void saveConfig(){
        fileManager.save();
    }

    public void reloadConfig(){
        this.fileManager = new FileManager();
    }
}
