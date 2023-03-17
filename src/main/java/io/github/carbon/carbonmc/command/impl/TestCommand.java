package io.github.carbon.carbonmc.command.impl;

import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.command.ICommandSender;

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
        return true;
    }
}
