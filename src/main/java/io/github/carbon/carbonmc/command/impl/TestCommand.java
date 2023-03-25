package io.github.carbon.carbonmc.command.impl;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.Setting;
import io.github.carbon.carbonmc.utils.Settings;

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
        DatabaseUtil databaseUtil = PluginServiceProvider.getCarbonMC().getDatabaseUtil();
        Setting setting = databaseUtil.getSetting(Settings.MAINTENANCE_MODE);
        boolean value = setting.getValue();

        context.getCommandSender().sendMessage("Maintenance mode is currently " + (value ? "enabled" : "disabled"));
        return true;
    }
}
