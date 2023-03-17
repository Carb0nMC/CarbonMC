package io.github.carbon.carbonmc.command;

public interface ICommand {
    String getName();
    String getUsage();
    String getDescription();
    String getPermission();

    boolean execute(CommandContext context);
}
