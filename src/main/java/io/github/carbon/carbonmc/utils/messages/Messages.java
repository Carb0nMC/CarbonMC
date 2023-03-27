package io.github.carbon.carbonmc.utils.messages;

public enum Messages {
    WELCOME_MESSAGE("welcome_message", "§e§lCarbonMC §8» §7Willkommen auf dem §e§lCarbonMC §7Netzwerk!"),
    TABLIST_HEADER("tablist_header", "§8» §cW§6i§el§al§bk§9o§5m§dm§ce§6n §ea§au§bf §9d§5e§dm §cC§6a§er§ab§bo§9n§5M§dC §cN§6e§et§az§bw§9e§5r§dk §8«\n"),
    TABLIST_FOOTER("tablist_footer", "\n    §8»§6www.carbonmc.net§8«\n§8-------------------§7[§e§lCarbonMC§7]§8-------------------"),

    TEAM_PLAYERS_PREFIX("team_players_prefix", "§7Spieler"),
    TEAM_DEVELOPERS_PREFIX("team_developers_prefix", "§6Developer"),
    TEAM_ADMIN_PREFIX("team_admin_prefix", "§3Admin"),
    DENY_BLOCK_BREAK("deny_block_break", "§c§lDu darfst hier keine Blöcke abbauen."),
    DENY_BLOCK_PLACE("deny_block_place", "§c§lDu darfst hier keine Blöcke platzieren."),
    MAINTENANCE_MODE("maintenance_mode", "§e§lCarbonMC §8» §7Der Server befindet sich zurzeit in Wartungsarbeiten."),
    MAINTENANCE_MODE_KICK("maintenance_mode_kick", "" +
            "§c§lWartungsarbeiten\n" +
            "\n" +
            "§7Der Server befindet sich derzeit im Wartungsmodus.\n" +
            "§7Bitte versuche es später noch einmal.\n" +
            "\n" +
            "§7§oDieser Server wird von CarbonMC betrieben.\n" +
            "§e§ohttps://www.carbonmc.net");

    private String id;
    private String defaultMessage;

    Messages(String id, String defaultMessage){
        this.id = id;
        this.defaultMessage = defaultMessage;
    }

    public String getId(){
        return id;
    }

    public String getDefaultMessage(){
        return defaultMessage;
    }
}
