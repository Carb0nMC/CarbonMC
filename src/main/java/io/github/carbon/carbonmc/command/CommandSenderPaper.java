package io.github.carbon.carbonmc.command;

import org.bukkit.command.CommandSender;

public class CommandSenderPaper implements ICommandSender {

    private CommandSender commandSender;

    public CommandSenderPaper(CommandSender commandSender){
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
