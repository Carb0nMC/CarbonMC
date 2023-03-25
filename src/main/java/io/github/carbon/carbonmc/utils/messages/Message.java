package io.github.carbon.carbonmc.utils.messages;

public class Message {
    private String id;
    private String value;

    public Message(String id, String value){
        this.id = id;
        this.value = value;
    }

    public String getId(){
        return id;
    }

    public String getValue(){
        return value;
    }
}
