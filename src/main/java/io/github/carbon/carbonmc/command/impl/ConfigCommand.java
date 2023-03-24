package io.github.carbon.carbonmc.command.impl;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandArgument;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.utils.file.FileManager;

import java.util.Arrays;
import java.util.List;

@CarbonCommand("config")
public class ConfigCommand implements ICommand {
    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String getUsage() {
        return "config <regenerate>";
    }

    @Override
    public String getDescription() {
        return "Allows to modify the config file";
    }

    @Override
    public String getPermission() {
        return "carbonmc.command.config";
    }

    @Override
    public List<CommandArgument> getArguments() {
        return Arrays.asList(
                new CommandArgument(1, "regenerate")
        );
    }

    @Override
    public boolean execute(CommandContext context) {
        if(context.getArgs().length == 0) return false;

        String toDo = context.getArgs()[0];

        switch(toDo){
            case "regenerate":
                FileManager fileManager = PluginServiceProvider.getCarbonMC().getFileManager();
                fileManager.regenerateConfig();
                context.getCommandSender().sendMessage("§aConfig regenerated!");
                return true;
            default:
                return false;
        }
    }
}
