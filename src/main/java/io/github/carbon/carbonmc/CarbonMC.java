package io.github.carbon.carbonmc;

import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import io.github.carbon.carbonmc.plugin.PluginService;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.ServerType;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class CarbonMC {
    public static String PREFIX = "§8[§6CarbonMC§8] §r";
    public static String VERSION = "1.0.0";
    public static String AUTHOR = "CarbonMC";
    protected CommandManager commandManager;
    protected DatabaseUtil databaseUtil;
    protected ServerType serverType;
    protected String serverID;
    protected Logger logger;

    protected boolean isBungee;

    public CarbonMC(){
        isBungee = PluginService.getSruntime().isBungee();
        System.out.println("ServerType: " + PluginService.getSruntime().name());
        System.out.println("isBungee: " + isBungee);
    }

    public void enable(){
        this.logger = Logger.getLogger("CarbonMC");

        this.databaseUtil = new DatabaseUtil();
        this.commandManager = new CommandManager();

        String asci =
                "\n§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-\n" +
                        "§c   _____ §6           §e  _____  §a  ____  §b   ____  §9  _   _ §5  __  __ §d   _____ \n" +
                        "§c  / ____|§6     /\\    §e |  __ \\ §a |  _ \\ §b  / __ \\ §9 | \\ | |§5 |  \\/  |§d  / ____|\n" +
                        "§c | |     §6    /  \\   §e | |__) |§a | |_) |§b | |  | |§9 |  \\| |§5 | \\  / |§d | |     \n" +
                        "§c | |     §6   / /\\ \\  §e |  _  / §a |  _ < §b | |  | |§9 | . ` |§5 | |\\/| |§d | |     \n" +
                        "§c | |____ §6  / ____ \\ §e | | \\ \\ §a | |_) |§b | |__| |§9 | |\\  |§5 | |  | |§d | |____ \n" +
                        "§c  \\_____|§6 /_/    \\_\\§e |_|  \\_\\§a |____/ §b  \\____/ §9 |_| \\_|§5 |_|  |_|§d  \\_____|\n" +
                        "§c         §6           §e         §a        §b         §9        §5         §d         \n" +
                        "§c         §6           §e         §a        §b         §9        §5         §d         " +
                        "\n" +
                        "§eStatus: §aOnline\n" +
                        "§eVersion: §a" + VERSION + "\n" +
                        "§eAuthor: §a" + AUTHOR + "\n" +
                        "§eType: §3" + (isBungee() ? "BungeeCord" : "Spigot") + "\n" +
                        "§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-§c-§6-§e-§a-§b-§9-§5-§d-\n";

        if(isBungee()) {
            logger.info(asci);
        } else PaperLoader.consolePrint(asci);

        onEnable();
    }

    protected void  onEnable(){

    }
    protected abstract void onDisable();

    public abstract ArrayList<String> getOnlinePlayerNames();

    public abstract UUID getPlayerUUID(String playerName);

    public abstract void setPermission(UUID player, String permission, boolean value);

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public String getServerID() {
        return serverID;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isBungee() {
        return isBungee;
    }

    public static CarbonMC get(){
        return PluginService.getCarbonMC();
    }
}
