package io.github.carbon.carbonmc;

import java.util.logging.Logger;

public class PluginServiceProvider {
    private static CarbonMC carbonMC;

    public PluginServiceProvider(CarbonMC carbonMC) {
        PluginServiceProvider.carbonMC = carbonMC;

        Logger logger = carbonMC.getLogger();

        String asci =
                "\n§7-------------------------------------------------\n" +
                "   ____           _                 __  __  ____ \n" +
                "  / ___|__ _ _ __| |__   ___  _ __ |  \\/  |/ ___|\n" +
                " | |   / _` | '__| '_ \\ / _ \\| '_ \\| |\\/| | |    \n" +
                " | |__| (_| | |  | |_) | (_) | | | | |  | | |___ \n" +
                "  \\____\\__,_|_|  |_.__/ \\___/|_| |_|_|  |_|\\____|\n" +
                "                                                 " + "\n" +
                        "\n" +
                        "§eStatus: §aOnline\n" +
                        "§eVersion: §a" + carbonMC.getVersion() + "\n" +
                        "§eAuthor: §a" + carbonMC.getAuthor() + "\n" +
                        "§eType: §3" + (carbonMC.isBungee() ? "BungeeCord" : "Spigot") + "\n" +
                        "§7-------------------------------------------------\n";

        logger.info(asci);
    }

    public static CarbonMC getCarbonMC() {
        return carbonMC;
    }
}
