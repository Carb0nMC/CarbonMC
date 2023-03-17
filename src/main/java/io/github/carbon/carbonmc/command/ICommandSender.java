package io.github.carbon.carbonmc.command;

public interface ICommandSender {
    String getName();
    void sendMessage(String message);
}
