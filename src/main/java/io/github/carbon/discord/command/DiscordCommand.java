package io.github.carbon.discord.command;

public interface DiscordCommand {
    String getName();
    String getDescription();
    String getHelp();
    void execute(DCommandContext context);
}
