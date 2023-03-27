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

@CarbonCommand("coins")
public class CoinCommand implements ICommand {
    @Override
    public String getName() {
        return "coins";
    }

    @Override
    public String getUsage() {
        return "coins <add|remove|set|get> <player> <amount>";
    }

    @Override
    public String getDescription() {
        return "Bearbeitet den Kontostand eines Spielers";
    }

    @Override
    public String getPermission() {
        return "carbon.command.coins";
    }

    @Override
    public List<CommandArgument> getArguments() {
        return Arrays.asList(
                new CommandArgument(1, "add", "remove", "set", "get"),
                new CommandArgument(2, CarbonMC.get().getOnlinePlayerNames())
        );
    }

    @Override
    public boolean execute(CommandContext context) {
        if(context.getArgs().length < 2){
            return false;
        }

        String uuidS = getUUID(context.getArgs()[1]);
        String prefix = CarbonMC.PREFIX;
        if(uuidS == null || uuidS.isEmpty()) {
            context.getCommandSender().sendMessage(prefix + "§cDer Spieler wurde nicht gefunden!");
            return true;
        }

        UUID uuid = UUID.fromString(uuidS);
        DatabaseUtil databaseUtil = CarbonMC.get().getDatabaseUtil();

        if (context.getArgs()[0].equalsIgnoreCase("get")) {
            int coins = databaseUtil.getCoins(uuid);
            context.getCommandSender().sendMessage(prefix + "§7Der Spieler §e" + context.getArgs()[1] + " §7hat §e" + coins + "§7 Coins.");
            return true;
        }

        if (context.getArgs().length < 3) {
            return false;
        }

        int amount = Integer.parseInt(context.getArgs()[2]);

        if (context.getArgs()[0].equalsIgnoreCase("add")) {
            databaseUtil.setCoins(uuid, databaseUtil.getCoins(uuid) + amount);
            context.getCommandSender().sendMessage(prefix + "§7Dem Spieler §e" + context.getArgs()[1] + " §7wurden §e" + amount + "§7 Coins hinzugefügt.");
            return true;
        }

        if (context.getArgs()[0].equalsIgnoreCase("remove")) {
            databaseUtil.setCoins(uuid, databaseUtil.getCoins(uuid) - amount);
            context.getCommandSender().sendMessage(prefix + "§7Dem Spieler §e" + context.getArgs()[1] + " §7wurden §e" + amount + "§7 Coins entfernt.");
            return true;
        }

        if (context.getArgs()[0].equalsIgnoreCase("set")) {
            databaseUtil.setCoins(uuid, amount);
            context.getCommandSender().sendMessage(prefix + "§7Dem Spieler §e" + context.getArgs()[1] + " §7wurden §e" + amount + "§7 Coins gesetzt.");
            return true;
        }


        return false;
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
