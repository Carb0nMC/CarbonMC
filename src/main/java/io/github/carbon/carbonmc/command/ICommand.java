package io.github.carbon.carbonmc.command;

import java.util.ArrayList;
import java.util.List;

public interface ICommand {
    String getName();
    String getUsage();
    String getDescription();
    String getPermission();

    default List<CommandArgument> getArguments(){
        return new ArrayList<>();
    }

    boolean execute(CommandContext context);
}
