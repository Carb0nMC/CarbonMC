package io.github.carbon.discord.event;

import io.github.carbon.discord.DiscordBot;
import io.github.carbon.discord.command.DCommandManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DEventlistener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        DCommandManager commandManager = DiscordBot.getInstance().getCommandManager();
        commandManager.handle(event);
    }
}
