package io.github.carbon.carbonmc.command.impl;

import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandArgument;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.utils.DatabaseUtil;
import io.github.carbon.carbonmc.utils.setting.Settings;

import java.util.Arrays;
import java.util.List;

@CarbonCommand("maintenance")
public class MaintenanceCommand implements ICommand {
    @Override
    public String getName() {
        return "maintenance";
    }

    @Override
    public String getUsage() {
        return "maintenance <on|off>";
    }

    @Override
    public String getDescription() {
        return "Aktiviert oder deaktiviert den Wartungsmodus.";
    }

    @Override
    public String getPermission() {
        return "carbonmc.maintenance";
    }

    @Override
    public List<CommandArgument> getArguments() {
        return Arrays.asList(
                new CommandArgument(1, "on", "off", "status", "toggle")
        );
    }

    @Override
    public boolean execute(CommandContext context) {
        if(context.getArgs().length == 1) {

            String argument = context.getArgs()[0];

            DatabaseUtil databaseUtil = CarbonMC.get().getDatabaseUtil();
            if(argument.equalsIgnoreCase("status")){
                boolean mode = databaseUtil.getSetting(Settings.MAINTENANCE_MODE).getValue();
                String message = "§eDer Wartungsmodus ist " + (mode ? "§aaktiviert" : "§cdeaktiviert") + "§e.";
                context.getCommandSender().sendMessage(CarbonMC.PREFIX + message);
                return true;
            }

            if(argument.equalsIgnoreCase("toggle")){
                boolean mode = databaseUtil.getSetting(Settings.MAINTENANCE_MODE).getValue();
                databaseUtil.updateSettings(Settings.MAINTENANCE_MODE, !mode);
                String message = "§eDer Wartungsmodus wurde " + (!mode ? "§aaktiviert" : "§cdeaktiviert") + "§e.";
                context.getCommandSender().sendMessage(CarbonMC.PREFIX + message);
                return true;
            }

            if (!argument.equalsIgnoreCase("on") && !argument.equalsIgnoreCase("off")) {
                return false;
            }
            boolean newMode = context.getArgs()[0].equalsIgnoreCase("on");

            databaseUtil.updateSettings(Settings.MAINTENANCE_MODE, newMode);

            String message = "§eDer Wartungsmodus wurde " + (newMode ? "§aaktiviert" : "§cdeaktiviert") + "§e.";
            context.getCommandSender().sendMessage(CarbonMC.PREFIX + message);
            return true;
        }

        return false;
    }
}
