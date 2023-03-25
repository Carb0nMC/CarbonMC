package io.github.carbon.discord.command.impl;

import io.github.carbon.discord.command.DCommandContext;
import io.github.carbon.discord.command.DiscordCommand;

import java.awt.*;

public class PingCommand implements DiscordCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Pingt den Bot";
    }

    @Override
    public String getHelp() {
        return "ping";
    }

    @Override
    public void execute(DCommandContext context) {
        long ping = context.getEvent().getJDA().getGatewayPing();
        context.replyEmbed("Pong!", "Das Pong hat ganze " + ping + "ms gedauert", Color.GREEN);
    }
}
