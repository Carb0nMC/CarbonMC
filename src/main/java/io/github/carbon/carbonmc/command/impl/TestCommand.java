package io.github.carbon.carbonmc.command.impl;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.playerstats.PlayerStats;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        return "carbon.command.test";
    }

    @Override
    public boolean execute(CommandContext context) {
        context.getCommandSender().sendMessage("Test command executed!");
        DatabaseUtil databaseUtil = CarbonMC.get().getDatabaseUtil();
        PlayerStats playerStats = databaseUtil.getPlayerStats(CarbonMC.get().getPlayerUUID(context.getCommandSender().getName()));
        long lastLogin = playerStats.getLastLogin();
        Date date = new Date(lastLogin);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        context.getCommandSender().sendMessage("Last login: " + format.format(date));

        return true;
    }
}
