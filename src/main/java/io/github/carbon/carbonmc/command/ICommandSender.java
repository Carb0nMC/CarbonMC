package io.github.carbon.carbonmc.command;

public interface ICommandSender {
    String getName();
    boolean hasPermission(String permission);
    void sendMessage(String message);
}
