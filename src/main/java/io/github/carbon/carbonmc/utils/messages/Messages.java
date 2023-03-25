package io.github.carbon.carbonmc.utils.messages;

public enum Messages {
    MAINTENANCE_MODE("maintenance_mode", "§e§lCarbonMC §8» §7Der Server befindet sich zurzeit in Wartungsarbeiten.");

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
