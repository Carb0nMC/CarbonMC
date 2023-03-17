package io.github.carbon.carbonmc.command;

import io.github.carbon.carbonmc.PluginServiceProvider;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Arrays;
import java.util.HashMap;

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

                    PluginServiceProvider.getCarbonMC().getLogger().info("Registering command: " + commandName);
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

        CommandContext context = new CommandContext(sender, commandArgs, commandName);
        boolean success = command.execute(context);

        if(!success){
            String message = "§e---------------------§r[§bCarbonMC§r]§e---------------------\n" +
                    "§eUsage: §r" + command.getUsage() + "\n" +
                    "§eDescription: §r" + command.getDescription() + "\n" +
                    "§e----------------------------------------------------";
            sender.sendMessage(message);
        }

        return true;
    }
}
