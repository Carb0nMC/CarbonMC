package io.github.carbon.carbonmc.command;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.PluginServiceProvider;
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
    public void sendMessage(String message) {
        commandSender.sendMessage(PluginServiceProvider.getCarbonMC().getPrefix() + message);
    }
}
