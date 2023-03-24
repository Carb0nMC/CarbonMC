package io.github.carbon.carbonmc.command.impl;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.command.ICommandSender;
import io.github.carbon.carbonmc.utils.file.FileManager;

@CarbonCommand("test")
public class TestCommand implements ICommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getUsage() {
        return "/test";
    }

    @Override
    public String getDescription() {
        return "Test command";
    }

    @Override
    public String getPermission() {
        return "carbonmc.test";
    }

    @Override
    public boolean execute(CommandContext context) {
        context.getCommandSender().sendMessage("Test command executed!");
        FileManager fileManager = PluginServiceProvider.getCarbonMC().getFileManager();
        fileManager.set("test", "tets2");
        return true;
    }
}
