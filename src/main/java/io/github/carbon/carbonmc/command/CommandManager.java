package io.github.carbon.carbonmc.command;

import io.github.carbon.carbonmc.PluginServiceProvider;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandManager {
    private HashMap<String, ICommand> commands = new HashMap<>();

    public CommandManager(){
        this.commands = new HashMap<>();

        this.registerCommands("io.github.carbon.carbonmc.command.impl");
    }

    public void registerCommands(String packageName){
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        reflections.getSubTypesOf(Object.class).forEach(command -> {
            try{
                if(command.isAnnotationPresent(CarbonCommand.class)){
                    CarbonCommand carbonCommand = command.getAnnotation(CarbonCommand.class);
                    String commandName = carbonCommand.value();
                    ICommand commandInstance = (ICommand) command.newInstance();

                    PluginServiceProvider.getCarbonMC().getLogger().info("§eRegistering command: §3" + commandName);
                    this.commands.put(commandName, commandInstance);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public ICommand getCommandByName(String commandName){
        return this.commands.get(commandName);
    }

    public boolean execute(String commandName, ICommandSender sender, String[] args){
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

        ICommand command = this.getCommandByName(commandName);
        if(command == null) return false;

        if(!sender.hasPermission(command.getPermission())){
            sender.sendMessage(PluginServiceProvider.getCarbonMC().getPrefix() + "§cDu hast keine Berechtigung diesen Befehl auszuführen!");
            return true;
        }

        CommandContext context = new CommandContext(sender, commandArgs, commandName);
        boolean success = command.execute(context);

        if(!success){
            String message = "\n" +
                    "§e---------------------§r[§bCarbonMC§r]§e---------------------\n" +
                    "§eUsage: §r" + command.getUsage() + "\n" +
                    "§eDescription: §r" + command.getDescription() + "\n" +
                    "§e----------------------------------------------------";
            sender.sendMessage(message);
        }

        return true;
    }

    public ArrayList<String> getCompletions(int position, String commandName){
        ICommand command = this.getCommandByName(commandName);

        ArrayList<String> toReturn = new ArrayList<>();

        if(position == 0){
            this.commands.forEach((name, command1) -> toReturn.add(name));
            return toReturn;
        }

        if (command == null) return null;

        List<CommandArgument> arguments = command.getArguments();
        command.getArguments().forEach(argument -> {
            if(argument.getPosition() == position){
                CommandArgument argumentAtPosition = argument;
                toReturn.addAll(argumentAtPosition.getValues());
            }
        });

        return toReturn;
    }
}
