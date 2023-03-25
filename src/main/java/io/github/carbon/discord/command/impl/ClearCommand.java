package io.github.carbon.discord.command.impl;

import io.github.carbon.discord.command.DCommandContext;
import io.github.carbon.discord.command.DiscordCommand;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

import java.awt.*;

public class ClearCommand implements DiscordCommand {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Löscht eine bestimmte Anzahl an Nachrichten.";
    }

    @Override
    public String getHelp() {
        return "clear <Anzahl>";
    }

    @Override
    public void execute(DCommandContext context) {
        if(context.getArgs().length == 0){
            context.replyEmbed("Fehler", "Du musst eine Anzahl angeben.", Color.RED, true, 5);
            return;
        }

        try {
            int amount = Integer.parseInt(context.getArgs()[0]) + 1;
            if (amount > 100) {
                context.replyEmbed("Fehler", "Du kannst maximal 100 Nachrichten löschen.", Color.RED, true, 5);
                return;
            }

            context.getChannel().getHistory().retrievePast(amount).queue(messages -> context.getChannel().deleteMessages(messages).queue());
        } catch (NumberFormatException e){
            context.replyEmbed("Fehler", "Du musst eine Zahl angeben.", Color.RED, true, 5);
        }
    }
}
