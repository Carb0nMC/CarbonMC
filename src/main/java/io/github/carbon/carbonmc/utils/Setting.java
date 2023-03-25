package io.github.carbon.carbonmc.utils;

public class Setting {
    private String id;
    private boolean value;

    public Setting(String id, boolean value){
        this.id = id;
        this.value = value;
    }

    public String getId(){
        return id;
    }

    public boolean getValue(){
        return value;
    }

}
