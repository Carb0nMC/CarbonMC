package io.github.carbon.carbonmc.command.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.command.CarbonCommand;
import io.github.carbon.carbonmc.command.CommandArgument;
import io.github.carbon.carbonmc.command.CommandContext;
import io.github.carbon.carbonmc.command.ICommand;
import io.github.carbon.carbonmc.utils.DatabaseUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
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
                new CommandArgument(2, CarbonMC.get().getOnlinePlayerNames())
        );
    }

    @Override
    public String getPermission() {
        return "carbon.command.permission";
    }

    @Override
    public boolean execute(CommandContext context) {
        if (context.getArgs().length < 3) return false;

        String playerName = context.getArgs()[1];
        UUID playerUUID = CarbonMC.get().getPlayerUUID(playerName);

        if(playerUUID == null){
            try{
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" +  playerName);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

                String uuidS = getUUID(playerName);
                playerUUID = UUID.fromString(uuidS);
            } catch (Exception e){
                context.getCommandSender().sendMessage("§cDer Spieler konnte nicht gefunden werden!");
                e.printStackTrace();
                return true;
            }
        }

        if(playerUUID == null) {
            context.getCommandSender().sendMessage("§cDer Spieler konnte nicht gefunden werden!");
            return true;
        }

        String permission = context.getArgs()[2];
        boolean value = context.getArgs()[0].equalsIgnoreCase("add");

        DatabaseUtil databaseUtil = CarbonMC.get().getDatabaseUtil();
        databaseUtil.setPermission(playerUUID, permission, value);
        String prefix = CarbonMC.PREFIX;
        context.getCommandSender().sendMessage(prefix + "§aDie Berechtigung §e" + permission + " §awurde " + (value ? "erfolgreich §agewährt" : "erfolgreich §centzogen") + "§a!");
        return true;
    }

    public String getUUID(String name) {
        String uuid = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            uuid = (((JsonObject)new JsonParser().parse(in)).get("id")).toString().replaceAll("\"", "");
            uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            in.close();
        } catch (Exception e) {
            System.out.println("Unable to get UUID of: " + name + "!");
            uuid = "er";
        }
        return uuid;
    }
}
