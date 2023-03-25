package io.github.carbon.carbonmc.utils.setting;

public enum Settings {
    MAINTENANCE_MODE("maintenance_mode", false);

    private String id;
    private boolean defaultValue;

    Settings(String id, boolean defaultValue){
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public String getId(){
        return id;
    }

    public boolean getDefaultValue(){
        return defaultValue;
    }
}
