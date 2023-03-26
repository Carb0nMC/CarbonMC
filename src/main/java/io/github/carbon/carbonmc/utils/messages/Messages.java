package io.github.carbon.carbonmc.utils.messages;

public enum Messages {
    WELCOME_MESSAGE("welcome_message", "§e§lCarbonMC §8» §7Willkommen auf dem §e§lCarbonMC §7Netzwerk!"),
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
