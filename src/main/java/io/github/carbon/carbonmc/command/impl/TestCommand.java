package io.github.carbon.carbonmc.command.impl;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.playerstats.PlayerStats;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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

        UUID uuid = CarbonMC.get().getPlayerUUID(context.getCommandSender().getName());
        if(context.getArgs().length == 2){
            databaseUtil.setCoins(uuid, databaseUtil.getCoins(uuid) + 5);
            context.getCommandSender().sendMessage("Du hast 5 coins bekommen!");
        }

        return true;
    }
}
