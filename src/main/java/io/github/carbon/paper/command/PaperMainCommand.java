package io.github.carbon.paper.command;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.command.CommandSenderPaper;
import io.github.carbon.carbonmc.command.ICommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PaperMainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 0){
            String prefix = PluginServiceProvider.getCarbonMC().getPrefix();
            commandSender.sendMessage(prefix + "§cPlease use /carbon <command>");
            return false;
        }

        String subCommand = args[0];
        ICommandSender sender = new CommandSenderPaper(commandSender);
        CommandManager commandManager = PluginServiceProvider.getCarbonMC().getCommandManager();
        commandManager.execute(subCommand, sender, args);
        return true;
    }
}
