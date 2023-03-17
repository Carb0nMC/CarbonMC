package io.github.carbon.bungeecord.command;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.carbonmc.command.CommandSenderBungee;
import io.github.carbon.carbonmc.command.ICommandSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BungeeMainCommand extends Command {
    public BungeeMainCommand() {
        super("carbon");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0){
            String prefix = PluginServiceProvider.getCarbonMC().getPrefix();
            sender.sendMessage(prefix + "§cPlease use /carbon <command>");
            return;
        }

        String command = args[0];
        ICommandSender iCommandSender = new CommandSenderBungee(sender);
        CommandManager commandManager = PluginServiceProvider.getCarbonMC().getCommandManager();
        commandManager.execute(command, iCommandSender, args);
    }
}
