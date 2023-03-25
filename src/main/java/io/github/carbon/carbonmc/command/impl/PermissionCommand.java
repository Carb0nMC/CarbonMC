package io.github.carbon.carbonmc.command.impl;

import io.github.carbon.carbonmc.PluginServiceProvider;
import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandArgument;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.utils.DatabaseUtil;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@CarbonCommand("permission")
public class PermissionCommand implements ICommand {
    @Override
    public String getName() {
        return "permission";
    }

    @Override
    public String getUsage() {
        return "permission <add|remove> <player> <permission>";
    }

    @Override
    public String getDescription() {
        return "Gibt oder entfernt eine bestimmte Berechtigung von einen Spieler";
    }

    @Override
    public List<CommandArgument> getArguments() {
        return Arrays.asList(
                new CommandArgument(1, "add", "remove"),
                new CommandArgument(2, PluginServiceProvider.getCarbonMC().getOnlinePlayerNames())
        );
    }

    @Override
    public String getPermission() {
        return "carbonmc.command.permission";
    }

    @Override
    public boolean execute(CommandContext context) {
        if (context.getArgs().length < 3) return false;

        String playerName = context.getArgs()[1];
        UUID playerUUID = PluginServiceProvider.getCarbonMC().getPlayerUUID(playerName);

        if (playerUUID == null) {
            context.getCommandSender().sendMessage("§cDer Spieler " + playerName + " wurde nicht gefunden!");
            return true;
        }

        String permission = context.getArgs()[2];
        boolean value = context.getArgs()[0].equalsIgnoreCase("add");

        DatabaseUtil databaseUtil = PluginServiceProvider.getCarbonMC().getDatabaseUtil();
        databaseUtil.setPermission(playerUUID, permission, value);

        //TODO: Update Permissions on join
        return true;
    }
}
