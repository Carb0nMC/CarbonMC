package io.github.carbon.carbonmc.command;

import net.md_5.bungee.api.CommandSender;

public class CommandSenderBungee implements ICommandSender {
    private CommandSender commandSender;

    public CommandSenderBungee(CommandSender commandSender){
        this.commandSender = commandSender;
    }

    @Override
    public String getName() {
        return commandSender.getName();
    }

    @Override
    public boolean hasPermission(String permission) {
        return commandSender.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        commandSender.sendMessage(message);
    }
}
