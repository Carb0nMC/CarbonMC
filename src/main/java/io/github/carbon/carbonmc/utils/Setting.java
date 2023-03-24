package io.github.carbon.carbonmc.utils;

public enum Setting {
    MAINTENANCE_MODE("Wartungsmodus", "maintenance_mode", false),
    MAINTENANCE_MESSAGE("Wartungsmodus Nachricht", "maintenance_mode_message", "§e§lCarbonMC §7>> §cDer Server ist im Wartungsmodus!");

    private String name;
    private String path;
    private Object defaultValue;

    Setting(String name, String path, Object defaultValue) {
        this.name = name;
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public static Setting getByPath(String path){
        for(Setting setting : Setting.values()){
            if(setting.getPath().equalsIgnoreCase(path)) return setting;
        }
        return null;
    }
}
