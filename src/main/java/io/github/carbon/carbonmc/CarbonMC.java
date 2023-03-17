package io.github.carbon.carbonmc;

import io.github.carbon.carbonmc.command.CommandManager;

import java.util.logging.Logger;

public interface CarbonMC {
    CommandManager getCommandManager();

    Logger getLogger();

    default String getPrefix(){
        return "§8[§6Carbon§8] §r";
    }

    default void register(){
        getCommandManager().registerCommands("io.github.carbon.carbonmc.command");
    }
}
